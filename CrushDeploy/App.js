
import React from 'react';
import CrushNavigator from './navigation/CrushNavigator';
import 'react-native-gesture-handler';

import { nofiticationsListener } from './API/FirebaseCM'

export default class App extends React.Component {
  constructor(props){
    super(props)
    nofiticationsListener() 
  }
  
  render(){
    return (
      <CrushNavigator />
    );
  }
}