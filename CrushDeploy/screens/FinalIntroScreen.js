import React, { Component } from 'react';
import { View, Button, Text, TextInput, Image, StyleSheet, ToastAndroid } from 'react-native';
import firebase from 'react-native-firebase';
import { NavigationEvents } from 'react-navigation';


import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants';

import { saveData } from '../API/LocalStorage';

class FinalIntroScreen extends Component {
  constructor(props) {
    super(props);
    this.updateTutorialOn = this.updateTutorialOn.bind(this);
  }

  async componentDidMount() {
    
  }

  componentWillUnmount() {
  }

  async updateTutorialOn() {
    await saveData(GeneralConstants.inTutorialKey, true)
  }

  render() { 
    return (
 <View style={ styles.cotainer }>
    <Image resizeMode='contain' style={styles.crashLogo} source={require('../assets/crush-logo.png')}/>

    <View style={styles.wrapper}>
    <Text style={styles.title}>NO MORE LOVE DOUBTS!</Text>
    <Text style={styles.paragraph}>
      Find out which contacts marked{"\n"}
            You as a CRUSH.{"\n"}
        It׳s secret, easy and free
    </Text>

    <Button title="ok" color={styles.okButton.color} style={styles.okButton} onPress={async() => {await this.props.navigation.navigate(await firebase.auth().currentUser ? 'NormalScreen':'AuthScreen' )}}/>
    </View>
    <NavigationEvents onWillFocus={this.updateTutorialOn} />
    </View> 
    );
  }
}

const styles = StyleSheet.create({
  cotainer:{
      flex:1,
      alignItems:'center',
      display: 'flex',
      justifyContent: "center"
    },    
    secondaryColor: {
      color: Colors.secondaryColor
    },
    okButton: {
      color: Colors.primaryColor,
      borderRadius: 50,
      margin: 1,
      justifyContent: "center"
    },
    title:{
      color: Colors.primaryColor,
      margin: 10,
      fontSize: 25,
    },
    paragraph:{
      fontSize: 20,
      color: Colors.secondaryColor
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

export default FinalIntroScreen;

