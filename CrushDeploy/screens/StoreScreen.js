import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  ScrollView,
} from 'react-native';
import { HeaderButtons, Item } from 'react-navigation-header-buttons';

import { readData } from '../API/LocalStorage';

import CustomHeaderButton from '../components/CustomHeaderButton';
import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants';
import StoreItem from '../components/StoreItem';
import { storeItemPressed } from '../API/AdMob'

const StoreScreen = () => {
  const [firstTime, setFirstTime] = useState(true);
  const [storeData, setStoreData] = useState();
  const [freeActivites, setFreeActivities] = useState([]);
  const [buy, setBuy] = useState([]);

  const onFirstTime = async () => {
    // Disabling first time
    setFirstTime(false);

    // Reading store offers from firebase
    let newStoreData = await readData(GeneralConstants.localDataStoreKey);

    let newFreeActivities = [];
    let newBuy = [];

    for (var i = 0; i < newStoreData.length; i++) {
      let currentOfferType = newStoreData[i].type;
      if (currentOfferType === GeneralConstants.storeBuyType) {
        newBuy.push(newStoreData[i]);
      } else if (currentOfferType === GeneralConstants.storeFreeActivitiesType) {
        newFreeActivities.push(newStoreData[i]);
      }
    }

    setStoreData(newStoreData);
    setFreeActivities(newFreeActivities);
    setBuy(newBuy);
  }

  // First time the user visits the store in this session (Resets only when closing the app)
  if (firstTime) {
    onFirstTime();
  }

  const onFreeActivityItemPressed = (offer) => {
    storeItemPressed()
    console.log(offer);
  }

  const onBuyItemPressed = (offer) => {
    console.log(offer);
  }

  return (
    <ScrollView>
      <View style={styles.mainView}>
        <Text style={styles.headerText}>
          Free Activities
        </Text>
        {freeActivites.map(offer => {
          return (
            <StoreItem key={offer.title} itemName={offer.title} onBuy={() => onFreeActivityItemPressed(offer)}/>
          );
        })}
        <Text style={styles.headerText}>
          Buy
        </Text>
        {buy.map(offer => {
          return (
            <StoreItem key={offer.title} itemName={offer.title} price={offer.price} onBuy={() => onBuyItemPressed(offer)}/>
          );
        })}
      </View>
    </ScrollView>
  );
};

StoreScreen.navigationOptions = navData => {
  return {
    headerTitle: "Store",
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
  mainView: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerText: {
    fontSize: 32,
    color: Colors.primaryColor,
    marginTop: 30,
    marginBottom: 10,
  }
});

export default StoreScreen;
