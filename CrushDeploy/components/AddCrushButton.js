import React from 'react';
import { View, Text, StyleSheet, Dimensions } from 'react-native';
import Icon from 'react-native-vector-icons/dist/FontAwesome';

import GeneralConstants from '../constants/GeneralConstants';
import { TouchableOpacity } from 'react-native-gesture-handler';
import Colors from '../constants/Colors';

const AddCrushButton = props => {
    return (
        <TouchableOpacity activeOpacity={0.4} onPress={props.onPress}>
            <View style={styles.button}>
                <Icon name="plus" color={'white'} size={GeneralConstants.addCrushButtonIconSize} />
            </View>
        </TouchableOpacity>
    );
}

const styles = StyleSheet.create({
    button: {
        width: GeneralConstants.addCrushButtonDiameter,
        height: GeneralConstants.addCrushButtonDiameter,
        borderRadius: GeneralConstants.addCrushButtonDiameter / 2,
        alignItems: 'center',
        justifyContent: "center",
        backgroundColor: Colors.primaryColor,
    }
});

export default AddCrushButton;