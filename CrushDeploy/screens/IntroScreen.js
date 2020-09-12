import React, { Component } from 'react';
import { View, Button, Text, TextInput, Image, StyleSheet, ToastAndroid } from 'react-native';

import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants';
import { saveData } from '../API/LocalStorage';

class IntroScreen extends Component {
  constructor(props) {
    super(props);
  }

  async componentDidMount() {
    await saveData(GeneralConstants.inTutorialKey, true)
  }

  componentWillUnmount() {
  }

  render() { 
    return (
      <View style={styles.screen}>
        
        <View style={styles.textWrapper}>
            <Text style={styles.text}> No More {"\n"} Love Doubts </Text>
        </View>
        
        <View style={styles.skipWrapper}>
            <View style={{flex:1}}></View>
            <View style={{flex:1, alignItems:"center",justifyContent:"center"}}>
                <Text style={styles.skipButton} onPress={()=>{ this.props.navigation.navigate("FinalIntroScreen") }}>Skip</Text>
            </View>
        </View>
      </View>
    );
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
  text: {
      color: "#fff",
      fontSize: 40
  },
  skipButton: {
      color: "#fff"
  },
  textWrapper:{
      flex: 8,
      justifyContent: "center",
  },
  skipWrapper:{
      flex: 2,
      flexDirection: "row-reverse",
  },
}); 

export default IntroScreen;

