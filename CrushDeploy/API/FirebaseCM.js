import React from 'react';
import 'react-native-gesture-handler';
import firebase from 'react-native-firebase';

// import NotificationsScreen from '../screens/NotificationsScreen';
import { saveData, readData } from './LocalStorage';
import GeneralConstants from '../constants/GeneralConstants'

export const writeNofiticationToMemory = async (data) => {
    
    var nofitications = await readData(GeneralConstants.nofiticationsKey)
    var dateObj = new Date()
    var curr_date = dateObj.getDate() + '/' + [1,2,3,4,5,6,7,8,9,11,12][dateObj.getMonth()] + '/' + dateObj.getFullYear() + ' ' + dateObj.getHours() + ':' + dateObj.getMinutes() + ':' + dateObj.getSeconds()
    
    // Generate new nofitication with given data 
    var newNotf = {
        id: data.id, 
        title: data.title,
        date: curr_date
    }
    //console.log("msg date:", curr_date)

    if(!nofitications[GeneralConstants.nofiticationsArrayItem].map(notf => {return notf.id}).includes(newNotf.id)){
        // Add nofitication to nofitications
        nofitications[GeneralConstants.nofiticationsArrayItem].push(newNotf)
        // Save updated nofitications
        saveData( GeneralConstants.nofiticationsKey, nofitications )
    } else {
        console.log("same id detected:", newNotf.id)
    }
}

export const nofiticationsListener = async () => {

    var nofitications = await readData(GeneralConstants.nofiticationsKey)

    // Initializing object for the first time
    if ( !nofitications || !(GeneralConstants.nofiticationsArrayItem in nofitications) ){
        nofitications = {}
        nofitications[GeneralConstants.nofiticationsArrayItem] = []
        saveData(GeneralConstants.nofiticationsKey, nofitications)
    }

    // Handle nofitications while app on foreground
    firebase.notifications().onNotification((nofitication) => {
        const { title, body , data} = nofitication;
        // Ensure nofitications is not empty
        if(data.title != undefined){
            writeNofiticationToMemory(data)
        }
    });


    firebase.notifications().onNotificationOpened((notificationOpen) => {
        const { title, body, data } = notificationOpen.notification;
        // Ensure nofitications is not empty
        if(data.title != undefined){
            writeNofiticationToMemory(data)
        }
    });

    
    // Handle opened nofitications while app on background
    firebase.notifications().getInitialNotification().then((notificationOpen) => {
        if (notificationOpen) {
            const { title, body, data } = notificationOpen.notification;
            // Ensure nofitications is not empty
            if(data.title != undefined){
                writeNofiticationToMemory(data)
            }
        }
    })

    
    firebase.messaging().onMessage((message) => {
        const { data } = message;
        writeNofiticationToMemory(data)
    });
}