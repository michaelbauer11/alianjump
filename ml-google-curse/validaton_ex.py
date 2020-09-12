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
from utils import read_from_file, write_to_file, preprocess_features, preprocess_targets, graph_data_examine_painter, my_input_fn, train_model_with_linear_regressor, predictions_vs_targets_graph

def main():
    # Read csv from local file
    california_housing_dataframe = pd.read_csv('california_data_house.csv')
    
    # We'll randomize the data, to ensure Stochastic Gradient Descent
    california_housing_dataframe = california_housing_dataframe.reindex(np.random.permutation(california_housing_dataframe.index))

    # For our TRAINING SET we will take the first 12000 examples
    training_examples = preprocess_features(california_housing_dataframe.head(12000))
    #print('################ TRAINING EXAMPLES:','\n', training_examples.describe(), '\n', '\n')

    training_targets = preprocess_targets(california_housing_dataframe.head(12000))
    #print('################ TRAINING TARGETS:', '\n', training_targets.describe(), '\n','\n')

    # For our VALIDATION SET we will take the last 5000 examples
    validation_examples = preprocess_features(california_housing_dataframe.tail(5000))
    #print('################ TRAINING EXAMPLES:', '\n', validation_examples.describe(), '\n', '\n')

    validation_targets = preprocess_targets(california_housing_dataframe.tail(5000))
    #print('################ VALIDATION TARGETS:', '\n', validation_targets.describe(), '\n', '\n')

    plt.figure(figsize=(13, 8)) 
    #graph_painter(plt, "Validation Data", validation_examples, "longitude", "latitude", 1, validation_targets["median_house_value"])
    #graph_painter(plt, "Training Data", training_examples, "longitude", "latitude", 2, training_targets["median_house_value"])
    #_ = plt.plot()
    # plt.show()

    linear_regressor = train_model_with_validation(
    # TWEAK THESE VALUES TO SEE HOW MUCH YOU CAN IMPROVE THE RMSE
    plt,
    learning_rate=0.001,
    steps=50,
    batch_size=10,
    training_examples=training_examples,
    training_targets=training_targets,
    validation_examples=validation_examples,
    validation_targets=validation_targets,
    )
    # plt.show()

    # Final testing part
    california_housing_test_data = pd.read_csv("https://download.mlcc.google.com/mledu-datasets/california_housing_test.csv", sep=",")

    test_features = preprocess_features(california_housing_test_data.head(10))

    test_targets = preprocess_targets(california_housing_test_data.head(10))

    test_input_fn = lambda: my_input_fn(test_features, test_targets['median_house_value'], num_epochs=1, shuffle=False)

    test_predictions = linear_regressor.predict(input_fn=test_input_fn)

    test_predictions_array = [item['predictions'][0] for item in test_predictions]
    test_predictions = np.array(test_predictions_array)

    test_root_mean_squared_error = math.sqrt(
        metrics.mean_squared_error(test_predictions, test_targets))
    


    print("  test RMSE : %f" % (test_root_mean_squared_error))

    print('type:' , type(test_targets['median_house_value']) , 'type:' , type(test_predictions))
    predictions_vs_targets_graph(plt, test_targets['median_house_value'], test_predictions)

    
if __name__ == "__main__":
    main()