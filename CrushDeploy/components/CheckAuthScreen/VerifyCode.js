import React, { Component } from 'react';
import { View, Button, Text, TextInput, Image, StyleSheet, ToastAndroid } from 'react-native';

import Colors from '../../constants/Colors';


class VerifyCode extends Component{
    constructor(props) {
        super(props);
    }

    render (){
        return (
            <View style={{ alignItems: 'center' }}>
      
              <Image resizeMode='contain' style={styles.crashLogo} source={require('../../assets/crush-logo.png')}/>
      
              <View style={styles.wrapper}>
              <Text style={styles.welcome}>Welcome</Text>
              <Text style={styles.confirmCode}>Enter confirmation code</Text>
              <Text style={styles.verificationNumber}>Please enter the verification code that {'\n'}
                  was sent to {this.props.phoneNumber}</Text>
              <TextInput
                //autoFocus   
                style={ styles.phoneTextEdit }
                onChangeText={value => this.props.parentCallback( value )}
                placeholder={'Code ... '}
                value={this.props.codeInput}
              />
              <Button title="OK" color={styles.okButton.color} style={styles.okButton} onPress={this.props.onOkPress} />
              <Text style={styles.verificationNumber}>Didn't get a code? <Text style={styles.primaryColor} onPress={this.props.onSignInPress}>Resend</Text></Text>
              </View>
            </View>
          );
    }
}

const styles = StyleSheet.create({
    primaryColor: {
      color: Colors.primaryColor,
    },
    secondaryColor: {
      color: Colors.secondaryColor
    },
    screen: {
      flex: 1,
      display: 'flex',
      alignItems: "center",
      justifyContent: "center"
    },
    signInButton: {
      color: Colors.primaryColor,
      borderRadius: 50,
      margin: 1
    },
    okButton: {
      color: Colors.primaryColor,
      borderRadius: 50,
      margin: 1,
      width: 1000
    },
    welcome:{
      color: Colors.primaryColor,
      margin: 10,
      fontSize: 25,
    },
    enterPhoneNumber:{
      fontSize: 20,
      color: Colors.secondaryColor
    },
    confirmCode:{
      fontSize: 20,
      color: Colors.secondaryColor,
      marginBottom: 10
    },
    verificationNumber:{
      fontSize: 14,
      color: Colors.secondaryColor
  
    },
    phoneTextEdit: {
      height: 60,
      fontSize: 20,
      marginTop: 15, 
      marginBottom: 15,
      color: Colors.secondaryColor,
      borderBottomWidth:1,
      borderBottomColor: Colors.primaryColor,
    },
    bottomTermsandCon:{
      color: Colors.secondaryColor,
      fontSize: 14,
      margin: 10,
      marginHorizontal: -30,
      
    },
    bottomTermsandConPrim:{
      color: Colors.primaryColor
    },
    crashLogo:{
      flex: 1,
      width: undefined,
      height: undefined,
      alignSelf: 'stretch'
    },
    wrapper: {
      flex: 2, 
      alignItems: 'center' 
    }
  }); 
  

export default VerifyCode;