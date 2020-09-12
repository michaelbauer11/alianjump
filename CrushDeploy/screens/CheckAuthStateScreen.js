import React from 'react'
import { View, Text, ActivityIndicator, StyleSheet, Image, Button } from 'react-native'

import firebase from 'react-native-firebase';

import Colors from '../constants/Colors';
import { getAllDocumentsData, getDocumentData, setDocumentData } from '../API/FirebaseDB';
import { saveData } from '../API/LocalStorage';
import { writeNofiticationToMemory } from '../API/FirebaseCM';
import GeneralConstants from '../constants/GeneralConstants';
import { fcmTokenAndPermissions } from '../components/NotificationPermission'


class CheckAuthStateScreen extends React.Component {
    state = {
            isLoading: false
        };

    authenticate = async () => {
        this.setState({ isLoading: true });
        firebase.auth().onAuthStateChanged(async (user) => {
          if (user) {
            // Pulling data from Firebase and saving it locally, improves loading time in the store.
            let newStoreData = await getAllDocumentsData(GeneralConstants.storeCollectionName);
            if (newStoreData) {
              saveData(GeneralConstants.localDataStoreKey, newStoreData);
            }

            let currentUserData = await getDocumentData(GeneralConstants.usersCollectionName, user.uid);
            if (currentUserData) {
              
              // set whether notifications or fcm needs to be updated on the server
              let updateRequired = false;
              // fcm token might change over time
              let fcmToken = await fcmTokenAndPermissions()
              if ( currentUserData.token != fcmToken){
               currentUserData.token = fcmToken;
               updateRequired = true;
              }
              // update user notifications with all its latest notifications:
              if (currentUserData.notifications.length > 0){
                currentUserData.notifications.forEach(element => {
                  writeNofiticationToMemory(element);
                });
                currentUserData.notifications = []
                updateRequired = true;
              }
              if (updateRequired) {await setDocumentData(GeneralConstants.usersCollectionName, user.uid, currentUserData);}

              await saveData(GeneralConstants.userDataKey, currentUserData);
              await saveData(GeneralConstants.UIDKey, user.uid)

            } else { // First time login
              let fcmToken = await fcmTokenAndPermissions()
              const newUserData = {"crushes" : [], "phoneNumber" : user.phoneNumber, "token": fcmToken, "notifications": []};
              await saveData(GeneralConstants.userDataKey, newUserData);
              await setDocumentData(GeneralConstants.usersCollectionName, user.uid, newUserData);
            }
            GeneralConstants.userPhone = user.phoneNumber;
            await saveData(GeneralConstants.UIDKey, user.uid);
            await saveData(GeneralConstants.inTutorialKey, false);
          }
          this.props.navigation.navigate(user ?  'NormalScreen' : 'IntroScreen' )
        })
    }

    render() {
        return (    
            <View style={styles.screen}>
            <Image resizeMode='contain' style={styles.crashLogo} source={require('../assets/crush-logo.png')}/>
            
            <View style={{flex: 1}}>
            <Button title="START" color={styles.startButton.color} style={styles.startButton} onPress={this.authenticate} />
            <ActivityIndicator size='large' animating={this.state.isLoading} color={styles.activityIndicator.color}></ActivityIndicator>
            </View>

          </View>
        )
    }
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    display: 'flex',
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: Colors.secondaryColor
  },
  startButton: {
    color: Colors.primaryColor,
    borderRadius: 50,
  },
  crashLogo:{
    flex:1,
    width: undefined,
    height: undefined,
    alignSelf: 'stretch',
  },
  activityIndicator:{
      color: Colors.primaryColor,
  }
})

export default CheckAuthStateScreen;
