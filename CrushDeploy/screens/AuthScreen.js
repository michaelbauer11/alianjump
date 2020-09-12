import React, { Component } from 'react';
import { View, Button, Text, TextInput, Image, StyleSheet, ToastAndroid } from 'react-native';

import Colors from '../constants/Colors';
import InsertPhoneScreen from '../components/CheckAuthScreen/InsertPhone'
import VerifyCodeScreen from '../components/CheckAuthScreen/VerifyCode'

import firebase from 'react-native-firebase';
import GeneralConstants from '../constants/GeneralConstants';

const phoneNumberRegex = "^\\+[0-9]?()[0-9](\\s|\\S)(\\d[0-9]{9})$"

class PhoneAuth extends Component {
  constructor(props) {
    super(props);
    this.unsubscribe = null;
    this.state = {
      user: null,
      codeInput: '',
      phoneNumber: '+972',
      confirmResult: null,
    };
  }

  toast(msg) {
    ToastAndroid.showWithGravity(
      msg,
      ToastAndroid.SHORT,
      ToastAndroid.BOTTOM,
    )
  }

  componentDidMount() {
    this.unsubscribe = firebase.auth().onAuthStateChanged((user) => {
      if (user) {
        this.setState({ user: user.toJSON() });
      } else {
        // User has been signed out, reset the state
        this.setState({
          user: null,
          codeInput: '',
          phoneNumber: '+972',
          confirmResult: null,
        });
      }
    });
  }

  componentWillUnmount() {
     if (this.unsubscribe) this.unsubscribe();
  }

  signIn = () => {
    if(this.phoneNumberNotValid()){
      this.toast('Insert Valid Phone Number')
      return
    }

    const { phoneNumber } = this.state;
    this.toast('Sending code ...')

    firebase.auth().signInWithPhoneNumber(phoneNumber)
      .then(confirmResult =>
        this.setState({ confirmResult }),
        this.toast('Code has been sent!')
      )
      .catch(error => 
          this.toast('Phone Number Error')
        )
  };

  phoneNumberNotValid(){
      const phoneNumberReg = new RegExp(phoneNumberRegex, "g")
      return !phoneNumberReg.test(this.state.phoneNumber)
  }

  confirmCode = async () => {
    const { codeInput, confirmResult } = this.state;

    if (confirmResult && codeInput.length) {
      confirmResult.confirm(codeInput)
        .then(async (user) => {
          this.setState( this.toast('Code Confirmed!') );
        })
        .catch(error => 
          this.toast(`Code Confirm Error: ${error.message}`)
        );
    }
  };

  signOut = () => {
    firebase.auth().signOut();
  }

  updatePhone = (childData) => {
    this.setState({phoneNumber: childData})
  }

  updateCode = (childData) => {
    this.setState({codeInput: childData})
  }

  render() {
    const { user, confirmResult } = this.state;
    
    return (
      <View style={styles.screen}>
        
        {!user && !confirmResult && 
          (<InsertPhoneScreen 
            parentCallback={this.updatePhone} 
            value={this.state.phoneNumber} 
            onSignInPress={this.signIn}/>)
        }
        
        {!user && confirmResult && 
          (<VerifyCodeScreen
            parentCallback={this.updateCode} 
            codeInput={this.state.codeInput}
            phoneNumber={this.state.phoneNumber}
            onSignInPress={this.signIn} 
            onOkPress={this.confirmCode}/>)
        }

      </View>
    );
  }
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    display: 'flex',
    alignItems: "center",
    justifyContent: "center"
  }
}); 

export default PhoneAuth;

