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

from utils import read_from_file, preprocess_features, preprocess_targets, train_model_with_linear_regressor, historigam, my_input

tf.logging.set_verbosity(tf.logging.ERROR)
pd.options.display.max_rows = 10
pd.options.display.float_format = '{:.1f}'.format

california_housing_dataframe = pd.read_csv("https://download.mlcc.google.com/mledu-datasets/california_housing_train.csv", sep=",")

california_housing_dataframe = california_housing_dataframe.reindex(
    np.random.permutation(california_housing_dataframe.index))

# Choose the first 12000 (out of 17000) examples for training.
training_examples = preprocess_features(california_housing_dataframe.head(12000))
training_targets = preprocess_targets(california_housing_dataframe.head(12000))

#plt.figure(figsize=(13, 8))
#historigam(plt, training_examples['total_rooms'], 'latitude')
plt.scatter(training_examples["latitude"], training_targets["median_house_value"])
#plt.show()

def select_and_transform_features(source_df):
  '''
  Scale latitude feature to two new feature of two others ranges like in the binning trick
  Each feature example transmited to boolean feature - whether it appears in that or not
  '''
  LATITUDE_RANGES = zip(range(32, 44), range(33, 45))
  selected_examples = pd.DataFrame()
  selected_examples["median_income"] = source_df["median_income"]
  for r in LATITUDE_RANGES:
    print(r)
    selected_examples["latitude_%d_to_%d" % r] = source_df["latitude"].apply(
      lambda l: 1.0 if l >= r[0] and l < r[1] else 0.0)
  return selected_examples

# Choose the last 5000 (out of 17000) examples for validation.
validation_examples = preprocess_features(california_housing_dataframe.tail(5000))
validation_targets = preprocess_targets(california_housing_dataframe.tail(5000))


selected_training_examples = select_and_transform_features(training_examples)
selected_validation_examples = select_and_transform_features(validation_examples)


correlation_dataframe = training_examples.copy()
correlation_dataframe["latitude_"] = training_examples["latitude"]

correlation_dataframe.corr()
print(correlation_dataframe.describe())
print(training_examples.describe())

minimal_features = [
    'latitude',
    "median_income"
]

assert minimal_features, "You must select at least one feature!"

minimal_training_examples = training_examples[minimal_features]
minimal_validation_examples = validation_examples[minimal_features]

train_model_with_linear_regressor(
    plt,
    learning_rate=0.001,
    steps=500,
    batch_size=10,
    training_examples=minimal_training_examples,
    training_targets=training_targets,
    validation_examples=minimal_validation_examples,
    validation_targets=validation_targets)
plt.show()