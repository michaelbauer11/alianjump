import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  FlatList, 
} from 'react-native';

import CustomHeaderButton from '../components/CustomHeaderButton';
import Colors from '../constants/Colors';
import { HeaderButtons, Item } from 'react-navigation-header-buttons';
import { readData, saveData } from '../API/LocalStorage';
import GeneralConstants from '../constants/GeneralConstants'
import Nofitication from '../components/NofiticationsScreen/Nofitication'

import firebase from 'react-native-firebase';


export default class NotificationsScreen extends React.Component {
  constructor(props){
    super(props)
    this.INIT_DATA  = []
    this.state = {
      DATA: []
    }
    this.newNoftsChecker;
  }

  componentDidMount() {
    this.newNoftsChecker = setInterval(() => this.updateNofitications(), 1000);
  }

  componentWillUnmount() {
    clearInterval(this.newNoftsChecker);
  }

  updateNofitications = () => {
    readData(GeneralConstants.nofiticationsKey).then((nofitications) => {

      // Check if new nofitication arrive
      if ( this.INIT_DATA.length != nofitications.length ){
        // Receive nofitications array
        this.INIT_DATA = nofitications[GeneralConstants.nofiticationsArrayItem]
      
        // Update nofitications on FlatList
        this.setState({ DATA: [...this.INIT_DATA]})
      }

    })
  }
  
  render(){
    return (
      <View>
        <FlatList style={styles.container}
          data={this.state.DATA}
          width="100%"
          renderItem={({ item }) => <Nofitication itemData={item} />}
          keyExtractor={item => item.id}
          extraData={this.state.DATA}
        />
      </View>
    );
  }
};

NotificationsScreen.navigationOptions = navData => {
  return {
    headerTitle: "Notifications",
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
  container: {
    //flex: 1,
    //backgroundColor: '#f9c2ff'
    marginVertical: 10
  }
});