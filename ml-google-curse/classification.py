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
from utils import preprocess_features, my_input_fn, train_model_with_linear_regressor, get_california, predictions_vs_targets_graph, train_linear_classifier_model
tf.logging.set_verbosity(tf.logging.ERROR)

def preprocess_targets(california_housing_dataframe):
  """Prepares target features (i.e., labels) from California housing data set.

  Args:
    california_housing_dataframe: A Pandas DataFrame expected to contain data
      from the California housing data set.
  Returns:
    A DataFrame that contains the target feature.
  """
  output_targets = pd.DataFrame()
  # Create a boolean categorical feature representing whether the
  # median_house_value is above a set threshold.
  output_targets["median_house_value_is_high"] = (
    california_housing_dataframe["median_house_value"] > 265000).astype(float)
  return output_targets

def testing(ml_model):
    california_housing_test_data = pd.read_csv('https://download.mlcc.google.com/mledu-datasets/california_housing_test.csv', sep=',')
    test_features = preprocess_features(california_housing_test_data.head(10))
    test_targets = preprocess_targets(california_housing_test_data.head(10))
    test_input_fn = lambda: my_input_fn(test_features, test_targets['median_house_value_is_high'], num_epochs=1, shuffle=False)
    test_predictions = ml_model.predict(input_fn=test_input_fn)
    test_predictions_array = [item['probabilities'][0] for item in test_predictions]
    test_predictions = np.array(test_predictions_array)
    test_root_mean_squared_error = math.sqrt(
    metrics.mean_squared_error(test_predictions, test_targets))

    print("  test RMSE : %f" % (test_root_mean_squared_error))
    predictions_vs_targets_graph(plt, test_targets['median_house_value_is_high'], test_predictions)

def main():
    california_housing_dataframe = get_california()
    # Create training and validation 
    training_examples = preprocess_features(california_housing_dataframe.head(12000))
    training_targets = preprocess_targets(california_housing_dataframe.head(12000))
    validation_examples = preprocess_features(california_housing_dataframe.tail(5000))
    validation_targets = preprocess_targets(california_housing_dataframe.tail(5000))


    linear_classifier = train_linear_classifier_model(
    # TWEAK THESE VALUES TO SEE HOW MUCH YOU CAN IMPROVE THE RMSE
    plt,
    learning_rate=0.0000001,
    steps=100,
    batch_size=20,
    training_examples=training_examples,
    training_targets=training_targets,
    validation_examples=validation_examples,
    validation_targets=validation_targets,
    )

    testing(linear_classifier)



main()