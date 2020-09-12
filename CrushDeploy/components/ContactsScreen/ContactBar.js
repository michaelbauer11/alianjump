import React from 'react';
import { View, Text, StyleSheet} from 'react-native';

import Constants from '../../constants/GeneralConstants';
import { TouchableOpacity } from 'react-native-gesture-handler';

const ContactBar = props => {
    return (
        <TouchableOpacity activeOpacity={Constants.touchableOpacityOpacity} onPress={props.onPress}>
            <View style={styles.contact}>
                <Text style={styles.contactNameText}>
                    {props.name}
                </Text>                
            </View>
        </TouchableOpacity>
    );
}

const styles = StyleSheet.create({
    contact: {
        width: Constants.contactBarSize.width,
        height: Constants.contactBarSize.height,
        borderColor: "#ccc",
        borderBottomWidth: 1,
        justifyContent: "center",
    },
    contactNameText: {
        fontSize: 20,
        textAlign: "left",
        marginHorizontal: 10,
        marginVertical: 10,
    }
});

export default ContactBar;
