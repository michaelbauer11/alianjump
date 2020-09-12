import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity} from 'react-native';

import GeneralConstants from '../../constants/GeneralConstants';
import Colors from '../../constants/Colors';

const crushItem = (crush, onChosenPressed) => {
    return (
        <TouchableOpacity key={crush.name + Math.random()} onPress={() => onChosenPressed(crush.name)}>
            <View style={styles.crushItemContainer}>
                <Text>{crush.name}</Text>
            </View>
        </TouchableOpacity>
    );
}

const ChosenCrushes = props => {
    const crushes = props.currentCrushes;

    if (crushes.length <= 3) {
        return (
            <View style={styles.chosenCrushes}>
                {crushes.map(crush => {return crushItem(crush, props.onChosenPressed);})}
            </View>
        );
    } else {
        return (
            <View>
                <View style={styles.chosenCrushes}>
                    {crushes.slice(0, GeneralConstants.numOfChosenCrushesPerLine).map(crush => crushItem(crush, props.onChosenPressed))}
                </View>
                <View style={styles.chosenCrushesBottom}>
                    {crushes.slice(GeneralConstants.numOfChosenCrushesPerLine ,crushes.length).map(crush => {return crushItem(crush, props.onChosenPressed);})}
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    chosenCrushes: {
        width: GeneralConstants.contactBarSize.width,
        alignItems: 'center',
        justifyContent: 'space-around',
        flexDirection: 'row',
        marginVertical: 5,
    },
    chosenCrushesBottom: {
        width: GeneralConstants.contactBarSize.width,
        alignItems: 'center',
        justifyContent: 'flex-start',
        flexDirection: 'row',
        marginTop: 5,
        marginVertical: 5,
        marginBottom: 10,
    },
    crushItemContainer: {
        width: 100,
        height: 25,
        borderRadius: 5,
        marginHorizontal: 5,
        backgroundColor: Colors.primaryColor,
        alignItems: 'center',
        justifyContent: 'center',
    }
});

export default ChosenCrushes;
