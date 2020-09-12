import React from 'react';
import {TouchableOpacity, View, Text, StyleSheet} from 'react-native';

import GeneralConstants from '../../constants/GeneralConstants';
import Colors from '../../constants/Colors';

const SetCrushesButton = props => {
    return (
        <TouchableOpacity activeOpacity={GeneralConstants.touchableOpacityOpacity} onPress={props.onPress}>
            <View style={styles.addButton}>
            <Text style={styles.addCrushText}>
                Crush
            </Text>
            </View>
        </TouchableOpacity>
    );
}

const styles = StyleSheet.create({
    addButton: {
    backgroundColor: Colors.primaryColor,
    width: GeneralConstants.addCrushButtonSize.width,
    height: GeneralConstants.addCrushButtonSize.height,
    alignItems: "center",
    justifyContent: "center",
    borderRadius: 10,
    },
    addCrushText: {
    fontSize: 18
    }
})

export default SetCrushesButton;
