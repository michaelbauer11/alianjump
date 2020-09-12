import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

import Colors from '../constants/Colors';
import GeneralConstants from '../constants/GeneralConstants'

const StoreItem = props => {
    return (
        <View style={styles.mainContainer}>
            <TouchableOpacity TouchableOpacity={GeneralConstants.touchableOpacityOpacity} onPress={props.onBuy} style={{...styles.card, ...props.style}}>
                <Text style={styles.itemName}>
                    {props.itemName + (props.price == undefined ? "" : " \t - \t " + props.price + "$")}
                </Text>
            </TouchableOpacity>
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
        width: GeneralConstants.storeCardSize.width,
        height: GeneralConstants.storeCardSize.height,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 15,
        backgroundColor: Colors.personCardColor,
        borderRadius: 8,
        overflow: "hidden", 
        elevation: 5,
    },
    itemName: {
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

export default StoreItem;