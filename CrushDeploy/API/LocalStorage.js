import React from 'react';
import AsyncStorage from '@react-native-community/async-storage';

// Can be used with no special instructions, just import and call the function.
export const saveData = async (key, value) => {
    try {
      await AsyncStorage.setItem(key, JSON.stringify(value));
      return true;
    } catch (e) {
      console.log(e);
      return false;
    }
  };


/*
To call this function, the calling function must be async function,
and to get the result, the 'await' keyword must be used. Example: 

const readMyData = async () => {
    let readData = await readData(GeneralConstants.myDataKey);
    console.log(readData);
}

*/
export const readData = async (key) => {
    try {
      const value = await AsyncStorage.getItem(key);
      return JSON.parse(value);
    } catch (e) {
        console.log(e);
        return false;
    }
  };
