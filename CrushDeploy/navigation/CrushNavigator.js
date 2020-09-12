import React from 'react';
import { createAppContainer } from 'react-navigation';
import { createBottomTabNavigator } from 'react-navigation-tabs';
import { createDrawerNavigator } from 'react-navigation-drawer';
import { createStackNavigator } from 'react-navigation-stack';
import Icon from 'react-native-vector-icons/dist/FontAwesome';
import EvilIcon from 'react-native-vector-icons/dist/EvilIcons';
import { copilot, walkthroughable, CopilotStep } from "react-native-copilot";
import { Button } from 'react-native';

import HomeScreen from '../screens/HomeScreen';
import StoreScreen from '../screens/StoreScreen';
import NotificationsScreen from '../screens/NotificationsScreen';
import ContacstsScreen from '../screens/ContacstsScreen';
import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants';
import Logout from '../screens/Logout';
import CheckAuthStateScreen from '../screens/CheckAuthStateScreen';
import AuthScreen from '../screens/AuthScreen';
import IntroScreen from '../screens/IntroScreen';
import FinalIntroScreen from '../screens/FinalIntroScreen'

const CopilotIcon = walkthroughable(Icon);

const defaultNavOptions = {
    headerStyle: {
        backgroundColor: 'white',
    },
    headerTintColor: Colors.primaryColor,
}

const HomeNavigator = createStackNavigator({
    Home: HomeScreen,
    Contacts: ContacstsScreen,
}, {
    defaultNavigationOptions: defaultNavOptions
});

const NotificationsNavigator = createStackNavigator({
    Notifications: NotificationsScreen,
}, {
    defaultNavigationOptions: defaultNavOptions
});

const StoreNavigator = createStackNavigator({
    Store: StoreScreen,
}, {
    defaultNavigationOptions: defaultNavOptions
});

const TabsNavigator = createBottomTabNavigator({
    Store: {
        screen: StoreNavigator,
        navigationOptions: {
            tabBarIcon: (tabInfo) => (<EvilIcon name="cart" color={tabInfo.tintColor} size={GeneralConstants.bottomTabNavigationIconSize}/>),
        },
    },
    Home: {
        screen: HomeNavigator,
        navigationOptions: {
            tabBarIcon: (tabInfo) => (
                <CopilotStep
                text="Come Pick your crush "
                order={2}
                name="crushAdd"
                >
                    <CopilotIcon name="home" color={tabInfo.tintColor} size={GeneralConstants.bottomTabNavigationIconSize}/>
                </CopilotStep>
                
            ),
        },
    },
    Notifications: {
        screen: NotificationsNavigator,
        navigationOptions: {
            tabBarIcon: (tabInfo) => (<Icon name="bell" color={tabInfo.tintColor} size={GeneralConstants.bottomTabNavigationIconSize}/>),
        },
    },
}, {
    tabBarOptions: {
        activeTintColor: Colors.primaryColor,
        style: {
            height: GeneralConstants.bottomTabNavigationHeight,
        },
        showLabel: false,
    },
    defaultNavigationOptions: defaultNavOptions,
    initialRouteName: 'Home'
}
);

const drawerNav = createDrawerNavigator({
    CheckAuthStateScreen: {
        screen: CheckAuthStateScreen,
        navigationOptions: {
            // Don't display it on the side menu
            drawerLabel: () => null,
        }
    },
    AuthScreen: {
        screen: AuthScreen,
        navigationOptions: {
            // Don't display it on the side menu
            drawerLabel: () => null,
        }
    },
    IntroScreen: {
        screen: IntroScreen,
        navigationOptions: {
            // Don't display it on the side menu
            drawerLabel: "App Tour",
        }
    },
    FinalIntroScreen:{
        screen: FinalIntroScreen,
        navigationOptions: {
            // Don't display it on the side menu
            drawerLabel: () => null,
        }
    },
    NormalScreen: {
        screen: TabsNavigator,
        navigationOptions: {
            drawerLabel: "Home Screen",
        }
    },
    Logout: {
        screen: Logout,
        navigationOptions: {
            drawerLabel: "Logout",
        }
    },
}, {
    contentOptions: {
        activeTintColor: Colors.accentColor,
    },
    defaultNavigationOptions: defaultNavOptions,
    backBehavior: 'none',
    initialRouteName: 'CheckAuthStateScreen'
});

export default copilot()(createAppContainer(drawerNav));