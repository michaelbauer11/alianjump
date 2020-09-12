import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants'

const PersonCard = props => {
    return (
        <View style={styles.mainContainer}>
            <TouchableOpacity TouchableOpacity={GeneralConstants.touchableOpacityOpacity} onPressOut={() => props.onDelete(props.personName)}>
                <View style={styles.removeButton}>
                    <Text>X</Text>
                </View>
            </TouchableOpacity>
            <TouchableOpacity TouchableOpacity={GeneralConstants.touchableOpacityOpacity} onLongPress={props.onLongPress} style={{...styles.card, ...props.style}}>
                <Text style={styles.personName}>
                    {props.personName}
                </Text>
            </TouchableOpacity>
            <View style={styles.removeButton}>
                <Text>{props.priority}</Text>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    mainContainer: {
        alignItems: "center",
        justifyContent: "center",
        flexDirection: 'row',
    },
    card: {
        width: GeneralConstants.crushCardSize.width,
        height: GeneralConstants.crushCardSize.height,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 15,
        backgroundColor: Colors.personCardColor,
        borderRadius: 10,
        overflow: "hidden", 
        elevation: 5,
    },
    personName: {
        color: "white",
        fontSize: 24,
    },
    removeButton: {
        width: GeneralConstants.removeCrushButtonDiameter,
        height: GeneralConstants.removeCrushButtonDiameter,
        backgroundColor: Colors.primaryColor,
        borderRadius: GeneralConstants.removeCrushButtonDiameter / 2,
        alignItems: 'center',
        justifyContent: 'center',
        marginHorizontal: 10,
    }
});

export default PersonCard;