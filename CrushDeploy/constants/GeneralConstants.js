import { Dimensions } from 'react-native';

const windowSize = Dimensions.get('window');

const GeneralConstants = {
    bottomTabNavigationIconSize: 32,
    bottomTabNavigationHeight: 60,
    addCrushButtonDiameter: windowSize.width * 0.14,
    addCrushButtonIconSize: 25,
    maxCrushesCount: 4,
    contactBarSize: {width: windowSize.width * 0.8, height: windowSize.height * 0.06},
    addCrushButtonSize: {width: windowSize.width * 0.27, height: windowSize.height * 0.05},
    addCrushButtonHeight:  windowSize.height * 0.77,
    touchableOpacityOpacity: 0.4,
    removeCrushButtonDiameter: windowSize.width * 0.08,
    tutorialFloatingBoxOnContactsScreenHeight: windowSize.height * 0.35,
    tutorialFloatingBoxOnContactsScreenTopMargin: windowSize.height * 0.32,
    numOfChosenCrushesPerLine: 3, // Used in the contacts screen, when you choose your crushes
    crushesListKey: 'crushes',
    userDataKey: 'userData',
    UIDKey: 'uid',
    inTutorialKey: 'inTutorialKey',
    nofiticationsKey: 'noftKey',
    nofiticationsArrayItem: 'noftArray',
    crushCardSize:  {width: windowSize.width * 0.55, height: windowSize.height * 0.065},
    storeCollectionName: "store",
    storeItemTypeField: "type",
    storeItemNameField: "name",
    storeItemPriceField: "price",
    storeBuyType: "buy",
    storeFreeActivitiesType: "freeActivities",
    storeCardSize:  {width: windowSize.width * 0.75, height: windowSize.height * 0.1},
    localDataStoreKey: "store",

    usersCollectionName: "users",
    usersPhoneNumberField: "phoneNumber",

    userPhone: ""
}

export default GeneralConstants;