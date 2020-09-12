  import React from 'react';
import {
  StyleSheet,
  View,
  Text
} from 'react-native';

import Colors from '../../constants/Colors';

class Nofitication extends React.Component {
    constructor(props){
        super(props)
    }
    
    render(){
        return (
            <View style={styles.item}>
                <Text>{this.props.itemData.date}</Text>
                <View style={styles.noftItem}>
                <Text style={styles.title}>{this.props.itemData.title}</Text>
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    item:{
      marginHorizontal: 10,
      marginVertical: 2.5
    },
    noftItem: {
      backgroundColor: Colors.secondaryColor,
      height: 30,
      borderRadius:10,
    },
    title: {
      fontSize: 16,
      color: '#ffffff',
      marginHorizontal: 10,
      marginTop: 5
    }
});

export default Nofitication;