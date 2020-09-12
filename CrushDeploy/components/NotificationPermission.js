
import firebase from 'react-native-firebase';

export const fcmTokenAndPermissions = async () => {
    return await firebase.messaging().hasPermission()
    .then(async (enabled) => {
      if (enabled) {
        return await firebase.messaging().getToken()
          .then(fcmToken => {
            if (fcmToken) {
                return fcmToken
            } else {
                return "no token"
            } 
          });
      } else {
        console.log("has no permissions")
      }
    }).catch(e => console.log(e));
  }