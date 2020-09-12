import React, { useState } from 'react';
import { HeaderButtons, Item } from 'react-navigation-header-buttons';
import DraggableFlatList from 'react-native-draggable-flatlist'
import {
  StyleSheet,
  View,
  Text,
  PermissionsAndroid,
  Platform,
  TouchableWithoutFeedback,
  Keyboard,
  Button,
} from 'react-native';
import { copilot, walkthroughable, CopilotStep } from "react-native-copilot";
import { NavigationEvents } from 'react-navigation';

import CustomHeaderButton from '../components/CustomHeaderButton';
import PersonCard from '../components/PersonCard';
import AddCrushButton from '../components/AddCrushButton';
import Colors from '../constants/Colors';
import Constants from '../constants/GeneralConstants';
import Contacts from 'react-native-contacts';
import { saveData, readData } from '../API/LocalStorage';
import GeneralConstants from '../constants/GeneralConstants';
import { setDocumentData } from '../API/FirebaseDB';
import { Tooltip } from '../app-tour/customTooltip';
import { emptyStepNumberComponent } from '../app-tour/customStepNumberComponent';

const CopilotText = walkthroughable(Text);

const HomeScreen = props => {
  const [crushesList, setCrushesList] = useState([]);
  const [contactsList, setContactsList] = useState([]);
  const [firstTime, setFirstTime] = useState(true);
  const [uid, setUid] = useState();
  const [currectUserData, setCurrectUserData] = useState();
  const [tutorialOn, setTutorialOn] = useState(false)

  const getContacts = () => {
    Contacts.checkPermission((err, permissionTop) => {
      if (err) throw err;
      
      if (!permissionTop === 'authorized') {
        Contacts.requestPermission((err, permission) => {
          permissionTop = permission
        })
      } 
      if (permissionTop === 'authorized') {
        Contacts.getAll((err, contacts) => {
          var arrayLength = contacts.length;
          var newContactsList = [];
          for (var i = 0; i < arrayLength; i++) {
              var currentContact = contacts[i];
              newContactsList.push({"name": currentContact.displayName, "phoneNumbers": currentContact.phoneNumbers, "priority": i});
          }
          setContactsList(newContactsList);
        });
      }
    })
  }

  const getPermissionsAndContacts = () => {
    if (Platform.OS === "android") {
      PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_CONTACTS, {
        title: "Contacts",
        message: "This app would like to view your contacts."
      }).then(() => {
        getContacts();
      });
    } else {
    }
  }

  // Updates the crushes list in all places - in the variables, in local memory, and in firebase
  const updateCrushesList = async (newCrushesList) => {
    if (uid != undefined) {
      let newUserData = currectUserData;
      newUserData[GeneralConstants.crushesListKey] = newCrushesList;
  
      saveData(GeneralConstants.userDataKey, newUserData);
      setCurrectUserData(newUserData);
      setCrushesList(newCrushesList);
  
      setDocumentData(GeneralConstants.usersCollectionName, uid, newUserData);
    }
  }

  const readDataFromFile = async () => {
    let readUserData = await readData(GeneralConstants.userDataKey);
    let readCrushes = readUserData[GeneralConstants.crushesListKey];

    setCurrectUserData(readUserData);

    if (readCrushes == null) { // There is no data stored on the device
      updateCrushesList([]);
    } else { // There was data to read, setting it to the read value
      setCrushesList(readCrushes)
    }
  }

  const readUID = async () => {
    let newUid = await readData(GeneralConstants.UIDKey);
    setUid(newUid);
  }

  if (firstTime) {
    getPermissionsAndContacts();
    readDataFromFile();
    readUID();
    setFirstTime(false);
  }

  if(tutorialOn){
    props.copilotEvents.on("stop", () => {
      addCrushPressed()
      setTutorialOn(false)
    })
  }

  const addCrushPressed = () => {
    if (crushesList.length < Constants.maxCrushesCount){
      props.navigation.navigate({
        routeName: "Contacts",
        params: {
          contacts: contactsList,
          onSelectCrushes: changeCrushesList,
          currentCrushes: crushesList,
          isTutorialOn: tutorialOn
        }
      })
    }
  }

  const updateCrushesPriority = (crushesList) => {
    // Updating the priority of the new crushes list when getting it from the contacts screen
    for (var i = 0; i < crushesList.length; i++) {
      crushesList[i].priority = i + 1; // Setting the priority to start from 1 instead of 0
    }
  }

  const onPersonDelete = (personName) => {
    var newCrushesList = crushesList.filter(crush => {
      if (crush.name !== personName) {
        return crush;
      }
    })

    updateCrushesPriority(newCrushesList);
    updateCrushesList(newCrushesList);
  }

  const changeCrushesList = (newCrushesList) => {
    updateCrushesPriority(newCrushesList);
    updateCrushesList(newCrushesList);
  }

  const renderItem = ({ item, index, drag, isActive }) => {
    return (
      <PersonCard onDelete={onPersonDelete} onLongPress={drag} personName={item.name} priority={item.priority}/>
    )
  }

  const endCrushDrag = (crushes) => {
    updateCrushesPriority(crushes);
    updateCrushesList(crushes);
  }

  const crushesSection = () => {
    if (crushesList.length > 0) {
      return (
        <View style={styles.mainCrushesView}>
          <DraggableFlatList
            data={crushesList}
            renderItem={renderItem}
            keyExtractor={(item, index) => {return item.name;}}
            onDragEnd={(crushes) => endCrushDrag(crushes.data)}
          />
        </View>
      );
    } else {
      return (
        <View style={styles.noCrushesContainer}>
          <Text style={{fontSize: 24}}>No crushes yet.</Text>
          <Text style={{fontSize: 16}}>Add some with the button below!</Text>
        </View>
      );
    }
  }

  const checkTutorialOn = async() => {
    let inTutorialKey = await readData(GeneralConstants.inTutorialKey);
    if(inTutorialKey){
      setTutorialOn(true);
      // Wait until the app tuturial loaded
      props.start();
    }
  }

  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
      <View style={styles.screen}>
        <View style={styles.upperPart}>
          <Text style={styles.hiText}>Hi!</Text>
          <Text style={styles.descText}>No one can see your selections</Text>
        </View>
        <View style={styles.peoplePart}>
          {crushesSection()}
        </View>
        <View style={styles.bottomPart}>
          <AddCrushButton onPress={() => addCrushPressed()}/>
          <CopilotStep
          text="Notice the timer!

          Tip: Hold one of your selections
          To edit priority. "
          order={1}
          name="timer"
        >
          <CopilotText style={styles.timer}>38:35</CopilotText>
        </CopilotStep>
          
          <Text style={styles.timerDescription}>Left until your last choice will be removed</Text>
        </View>
        <NavigationEvents onWillFocus={checkTutorialOn} />
      </View>
    </TouchableWithoutFeedback>
    
  );
};

HomeScreen.navigationOptions = navData => {
  return {
    headerTitle: "Home",
    headerLeft: (
      <HeaderButtons HeaderButtonComponent={CustomHeaderButton}>
        <Item
          title="favorite"
          iconName="bars"
          onPress={() => {
            navData.navigation.toggleDrawer();
          }} />
      </HeaderButtons>
    ),
  }
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    alignItems: "center",
  },
  upperPart: {
    flex: 1.5,
    width: "100%",
    borderBottomWidth: 0,
    borderBottomColor: "#ccc",
    alignItems: 'center',
    justifyContent: 'flex-end',
  },
  peoplePart: {
    flex: 7,
    width: "100%",
    alignItems: 'center',
    justifyContent: 'center',
    borderBottomWidth: 0,
    borderBottomColor: "#ccc",
  },
  bottomPart: {
    flex: 3,
    width: "100%",
    alignItems: 'center',
    borderBottomWidth: 0,
    borderBottomColor: "#E2E2E2",
  },
  hiText: {
    fontSize: 32,
    color: Colors.primaryColor,
  },
  descText: {
    fontSize: 20
  },
  timer: {
    fontSize: 26,
    marginTop: 10,
    color: Colors.primaryColor,
  },
  timerDescription: {
    fontSize: 18,
  },
  noCrushesContainer: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  mainCrushesView: {
      flex:1,
      alignItems: 'center',
      justifyContent: 'center',
  }
});

const tooltipStyle = {
  backgroundColor: "#2f3c4f",
  borderRadius: 10,
  paddingTop: 5,
  color: '#fff',
}

export default copilot({
  overlay: "view", // or 'svg'
  animated: false, // or true
  //tooltipStyle: tooltipStyle,
  tooltipComponent: Tooltip,
  verticalOffset: 25,
  stepNumberComponent: emptyStepNumberComponent
})(HomeScreen);
