import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  TextInput,
  Text,
  ToastAndroid
} from 'react-native';
import { copilot, walkthroughable, CopilotStep } from "react-native-copilot";
import { FlatList } from 'react-native-gesture-handler';

import ContactBar from '../components/ContactsScreen/ContactBar';
import ChosenCrushes from '../components/ContactsScreen/ChosenCrushes';
import SetCrushesButton from '../components/ContactsScreen/SetCrushesButton';
import SearchBar from '../components/SearchBar';
import GeneralConstants from '../constants/GeneralConstants';
import { saveData, readData } from '../API/LocalStorage';
import { Tooltip } from '../app-tour/customTooltip';
import { emptyStepNumberComponent } from '../app-tour/customStepNumberComponent';

import { NavigationEvents } from 'react-navigation';

const CopilotView = walkthroughable(View);
import { parsePhoneNumberFromString } from 'libphonenumber-js';

const Contacts = props => {
  const contacts = props.navigation.getParam('contacts');
  const [chosenCrushes, setChosenCrushes] = useState(props.navigation.getParam('currentCrushes'));
  const [filteredContacts, setFilteredContacts] = useState(contacts);
  const isTutorialOn = props.navigation.getParam('isTutorialOn');
  const [tutorialOn, setTutorialOn] = useState(isTutorialOn)


  if(tutorialOn){
    props.copilotEvents.on("stop", () => {
      saveData(GeneralConstants.inTutorialKey, false);
      setTutorialOn(false)
    })
  }

  const getPhoneNumber = (phoneNumber) => {
    /*
    * Return phone number in E.164 format. If number doesn't have international code,
    * Will asume contact and user are from the same country
    */
    
    let parsedPhone = parsePhoneNumberFromString(phoneNumber ? phoneNumber : "undefined")
    if (!parsedPhone){
      // Trying to fix contact phone number:
    
      // Ensure number is international - assuming both contact and user are from the same country
      let userCountryCallingCode = parsePhoneNumberFromString(GeneralConstants.userPhone).countryCallingCode;
      if ( !phoneNumber.startsWith(userCountryCallingCode) ){
        phoneNumber = userCountryCallingCode + phoneNumber
      }
      
      // Number which didn't parsed in parsePhoneNumberFromString is not having a '+' in their beginning
      // Add '+' and try parse phone again
      parsedPhone = parsePhoneNumberFromString('+' + phoneNumber)
      if ( !parsedPhone ){
        // couldn't parse number
        return
      }
    }
    return parsedPhone.number
  }

  const toast = (msg) => {
    ToastAndroid.showWithGravity(
      msg,
      ToastAndroid.LONG,
      ToastAndroid.BOTTOM,
    )
  }

  const onContactPressed = (contact) => {
    const contactIndex = chosenCrushes.indexOf(contact);
    
    // Orgenize phone numbers list in contact.phoneNumbers
    let finalPhoneNumbersContact = []
    contact.phoneNumbers.forEach((phoneObject) => {
      let phoneNumber = getPhoneNumber(phoneObject.number)
      if (phoneNumber && !finalPhoneNumbersContact.includes(phoneNumber)){
          finalPhoneNumbersContact.push(phoneNumber)
      } 
    })
    
    // No valid phone found in contact numbers, contact will not be added
    if (!finalPhoneNumbersContact.length) {
      toast("Contact phone number should be in international format!")
    } 
    // Valid Phone found in contact numbers, contact will be added to chosen crushes
    else{
      
    // Changing the array object - is destroying the contact object and than it can be reloaded to flat list - David?
      contact['validPhoneNumbers'] = finalPhoneNumbersContact

      if (contactIndex === -1 && chosenCrushes.length < GeneralConstants.maxCrushesCount){
        setChosenCrushes([...chosenCrushes, contact]);
      } else if (contactIndex !== -1) {
        const slicedArr = chosenCrushes.filter(crush => {
          return (crush !== contact);
        });
        setChosenCrushes(slicedArr);
      }
    }
  }

  const onChosenPressed = (name) => {
    const slicedArr = chosenCrushes.filter(crush => {
      return (crush.name !== name);
    });
    setChosenCrushes(slicedArr);
  }

  const onSearchTextChanged = (text) => {
    const newFilteredContacts = contacts.filter(contact => {
      if (contact !== null && contact.name !== null){
        return contact.name.includes(text);
      }
    })
    setFilteredContacts(newFilteredContacts);
  }
  
  const saveCrushes = () => {
    props.navigation.getParam('onSelectCrushes')(chosenCrushes);
    props.navigation.pop();
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
    <View style={styles.screen}>
      <CopilotStep
          text="Notice the timer!

          Tip: Hold one of your selections
          To edit priority. "
          order={1}
          name="timer"
        >
          <CopilotView style={styles.floating_view}></CopilotView>
        </CopilotStep>
      <SearchBar onSearchTextChanged={onSearchTextChanged}/>
      <ChosenCrushes currentCrushes={chosenCrushes} onChosenPressed={onChosenPressed}/>
      <FlatList data={filteredContacts}
      renderItem={(contact) => 
        <ContactBar 
        name={contact.item.name}
        onPress={() => onContactPressed(contact.item)}
        />}
      keyExtractor={contact => (contact.name + Math.random())} />
      <View style={styles.addButtonTouchable}>
        <SetCrushesButton onPress={saveCrushes}/>
      </View>
      <NavigationEvents onWillFocus={checkTutorialOn} />
    </View>
  );
};

Contacts.navigationOptions = navData => {
  return {
    headerTitle: "Choose Your Crushes",
  }
}

const styles = StyleSheet.create({
    screen: {
        width: "100%",
        alignItems: "center",
    },
    addButtonTouchable: {                                  
        position: 'absolute',                                          
        top: GeneralConstants.addCrushButtonHeight,                                                    
        right: 30,
    },
    floating_view: {
        position: 'absolute',
        justifyContent: 'center',
        alignItems: 'center',
        height: GeneralConstants.tutorialFloatingBoxOnContactsScreenHeight,
        top: GeneralConstants.tutorialFloatingBoxOnContactsScreenTopMargin,
        width: '80%',
        zIndex: 1000
    }
});

export default copilot({
  overlay: "view", // or 'svg'
  animated: false, // or true
  //tooltipStyle: tooltipStyle,
  tooltipComponent: Tooltip,
  verticalOffset: 25,
  stepNumberComponent: emptyStepNumberComponent
})(Contacts);
