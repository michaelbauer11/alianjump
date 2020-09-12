/*
* This Page contains all the rest API of the application.
* All HTTP/S requests API is concentrated in this file.
*/


// This function return the server current time In a Date object.
// This function should be called with an 'await' statment before it. 
export const getGlobalTimestamp = async () => {
  return fetch('https://us-central1-crush-3bd7f.cloudfunctions.net/getTimestamp')
    .then( (response) => response.json() ) 
    .then( (responseJson) => {
        return new Date(responseJson);
    })
    .catch( (error) => {
      console.error(error);
    });
}