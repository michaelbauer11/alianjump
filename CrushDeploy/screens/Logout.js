import React, { Component } from 'react';
import firebase from 'react-native-firebase';

export default class Logout extends Component{

    componentDidMount(){
        firebase.auth().signOut()
        this.props.navigation.navigate( 'CheckAuthStateScreen' )
    }

    render(){
        return (null);
    }
}
