import pandas as pd 

print pd.__version__

# Columns:
city_names = pd.Series(['San fransisco', 'San Jose', 'Sacramento'])
population = pd.Series(['8887766', '77766765', '55432445'])

# Colmuns and Rows:
data_set = pd.DataFrame({'City Name': city_names, 'Population': population})


# access remote data-set
california_housing_dataframe = pd.read_csv("https://download.mlcc.google.com/mledu-datasets/california_housing_train.csv", sep=",")

# short describe about the table object and its values
print california_housing_dataframe.describe()
# display complit table
print california_housing_dataframe.head()

# display in graph - requires another library
# print california_housing_dataframe.hist('housing_median_age')

