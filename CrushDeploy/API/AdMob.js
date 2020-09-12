
import firebase from 'react-native-firebase';

export const advert = firebase.admob().rewarded('ca-app-pub-3940256099942544/5224354917');

const AdRequest = firebase.admob.AdRequest;
const request = new AdRequest();
request.addKeyword('foo').addKeyword('bar');

// Load the advert with our AdRequest
advert.loadAd(request.build());

advert.on('onAdLoaded', () => {
});

advert.on('onRewarded', (event) => {
  // User now should be rewarded, decrease time of loading
});

export const storeItemPressed = () => {
    if (advert.isLoaded()) {
      advert.show();
    } 
  }
