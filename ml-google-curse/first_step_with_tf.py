from __future__ import print_function

import math

from IPython import display
from matplotlib import cm
from matplotlib import gridspec
from matplotlib import pyplot as plt
import numpy as np
import pandas as pd
from sklearn import metrics
import tensorflow as tf
from tensorflow.python.data import Dataset
from first_model import my_input_fn

def train_model(learning_rate, steps, batch_size, input_feature, dataset):
  """Trains a linear regression model of one feature.
  
  Args:
    learning_rate: A `float`, the learning rate.
    steps: A non-zero `int`, the total number of training steps. A training step
      consists of a forward and backward pass using a single batch.
    batch_size: A non-zero `int`, the batch size.
    input_feature: A `string` specifying a column from `california_housing_dataframe`
      to use as input feature.
  """
  california_housing_dataframe = dataset

  periods = 10
  steps_per_period = steps / periods

  my_feature = input_feature
  my_feature_data = california_housing_dataframe[[my_feature]]
  my_label = "median_house_value"
  targets = california_housing_dataframe[my_label]

  # Create feature columns.
  feature_columns = [tf.feature_column.numeric_column(my_feature)]
  
  # Create input functions.
  training_input_fn = lambda:my_input_fn(my_feature_data, targets, batch_size=batch_size, train_function=True)
  prediction_input_fn = lambda: my_input_fn(my_feature_data, targets, num_epochs=1, shuffle=False)
  
  # Create a linear regressor object.
  my_optimizer = tf.train.GradientDescentOptimizer(learning_rate=learning_rate)
  my_optimizer = tf.contrib.estimator.clip_gradients_by_norm(my_optimizer, 5.0)
  linear_regressor = tf.estimator.LinearRegressor(
      feature_columns=feature_columns,
      optimizer=my_optimizer
  )

  # Set up to plot the state of our model's line each period.
  # Top level container for all plot elements(window size):
  plt.figure(figsize=(15, 6))
  # Create sub graph under the top plot
  plt.subplot(1, 2, 1)
  plt.title("Learned Line by Period")
  plt.ylabel(my_label)
  plt.xlabel(my_feature)
  # Sample is a midgam, Basically to take a batch, in our case, of 300  
  sample = california_housing_dataframe.sample(n=300)
  # Give the plot an arrayof values for x and y axes(ztirim) 
  plt.scatter(sample[my_feature], sample[my_label])
  # set colors according to values in array
  colors = [cm.coolwarm(x) for x in np.linspace(-1, 1, periods)]


  # Train the model, but do so inside a loop so that we can periodically assess
  # loss metrics.
  print("Training model...")
  print("RMSE (on training data):")
  root_mean_squared_errors = []

  for period in range (0, periods):
    # Train the model, starting from the prior state.
    linear_regressor.train(
        input_fn=training_input_fn,
        steps=steps_per_period
    )

    # Take a break and compute predictions.
    predictions = linear_regressor.predict(input_fn=prediction_input_fn)
    predictions = np.array([item['predictions'][0] for item in predictions])
    
    # Compute loss.
    root_mean_squared_error = math.sqrt(
        metrics.mean_squared_error(predictions, targets))
    # Occasionally print the current loss.
    print("  period %02d : %0.2f" % (period, root_mean_squared_error))
    # Add the loss metrics from this period to our list.
    root_mean_squared_errors.append(root_mean_squared_error)
    # Finally, track the weights and biases over time.
    # Apply some math to ensure that the data and line are plotted neatly.
    
    # set y extents with y axis
    y_extents = np.array([0, sample[my_label].max()])
    
    # set graph weight and bias
    weight = linear_regressor.get_variable_value('linear/linear_model/%s/weights' % input_feature)[0]
    bias = linear_regressor.get_variable_value('linear/linear_model/bias_weights')

    # calculate x extents with x axis
    x_extents = np.maximum(np.minimum(x_extents,
                                      sample[my_feature].max()),
                           sample[my_feature].min())
    y_extents = weight * x_extents + bias
    x_extents = (y_extents - bias) / weight
    # cerate graph on out plot 
    plt.plot(x_extents, y_extents, color=colors[period]) 

  print("Model training finished.")


  print("Final RMSE (on training data): %0.2f" % root_mean_squared_error)
  calibration_data = pd.DataFrame()
  calibration_data["predictions"] = pd.Series(predictions)
  calibration_data["targets"] = pd.Series(targets)
  display.display(calibration_data.describe())

  return calibration_data

  # Output a graph of loss metrics over periods.
  plt.subplot(1, 2, 2)
  plt.ylabel('RMSE')
  plt.xlabel('Periods')
  plt.title("Root Mean Squared Error vs. Periods")
  plt.tight_layout()
  plt.plot(root_mean_squared_errors)
  

def main():
    tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)
    pd.options.display.max_rows = 10
    # Round the float numbers in the pandas table given from the remote web-service
    pd.options.display.float_format = '{:.1f}'.format

    california_housing_dataframe = pd.read_csv("california_data_house.csv", sep=",") 
  
    # We'll randomize the data, to ensure Stochastic Gradient Descent
    california_housing_dataframe = california_housing_dataframe.reindex(np.random.permutation(california_housing_dataframe.index))
    # Set the house values to be in thousands multiplies
    california_housing_dataframe["median_house_value"] /= 1000.0

    # print(california_housing_dataframe.head())
    # adding new feature in the data that is the ratio of two others features
    california_housing_dataframe["rooms_per_person"] = california_housing_dataframe["total_rooms"]/california_housing_dataframe["population"]
    # print(california_housing_dataframe.head())
    
    # train_model(learning_rate, steps, batch_size, input_feature, dataset)
    calibration_data = train_model(0.1, 20, 10, "rooms_per_person", california_housing_dataframe)
    plt.subplot(2, 2, 1)
    plt.scatter(calibration_data["predictions"], calibration_data["targets"], 5)

    plt.show()


if __name__ == "__main__":
    main()