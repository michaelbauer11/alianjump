import React, { Component } from 'react';
import { View, Button, Text, TextInput, Image, StyleSheet, ToastAndroid } from 'react-native';

import Colors from '../../constants/Colors';


class InsertPhone extends Component{
    constructor(props) {
        super(props);
    }

    render (){
        return (
            <View style={ styles.cotainer }>
            
            <Image resizeMode='contain' style={styles.crashLogo} source={require('../../assets/crush-logo.png')}/>
    
            <View style={styles.wrapper}>
            <Text style={styles.welcome}>Welcome</Text>
            <Text style={styles.enterPhoneNumber}>Enter phone number:</Text>
            <TextInput
                //autoFocus
                style={styles.phoneTextEdit}
                onChangeText={value => this.props.parentCallback( value )}
                placeholder={'Phone number ... '}
                value={this.props.value}
            />
            <Button title="Continue" color={styles.signInButton.color} style={styles.signInButton} onPress={this.props.onSignInPress}/>
            
            <View><Text style={styles.bottomTermsandCon}>By pressing continue you agree to our {"\n"}
            <Text style={styles.secondaryColor}>terms and conditions</Text> and <Text style={styles.secondaryColor}>privacy policy</Text></Text></View>
            </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    cotainer:{
        alignItems:'center'
    },    
    secondaryColor: {
      color: Colors.secondaryColor
    },
    signInButton: {
      color: Colors.primaryColor,
      borderRadius: 50,
      margin: 1
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
  

export default InsertPhone;