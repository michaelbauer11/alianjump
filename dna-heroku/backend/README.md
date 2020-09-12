# Exploit the backend
our django server consists of apps. each app responsible for different services in our app.

### dna app
Our dna app(**dna directory**) is our main app.  
In this app all of our main settings is setup - 
- **dna/settings.py**:
  Contains all the app settings - including db settings.
- **dna/urls.py**:
  Conatins the app main url. This file calls the other application url.
  
### forms app
  This app responsible for all the forms management in our app.
  - **forms/urls**:  
    Contains the relevant urls for the forms app - explenation for all of the url is inside this file.
  - **forms/views**:  
    Contains all the functions handling the requests coming to the server. each url has its own function.
    Those function responsible, among other things, to read and write data to the database.
  - **forms/models**:
    Contains the app main table. Each model is equivilent to a table in the specified db.  
    All of our forms, has one model - one table. All of the app's forms saved in the same table -  
    the difference between them is that not all of their fields are filled in the table.  
  - **forms/serializers**:  
    Contains the model's serilalizer.  
    The serializer are responsible for the parsing of the http requests content into a model object, And vice versa.


### backend flow - request handling
e.g form create request:
dna/urls.py -> forms/urls.py -> forms/views.py -> forms/serializer.py -> forms/models.py
