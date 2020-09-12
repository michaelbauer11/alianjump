import React from 'react';
import { HeaderButton } from 'react-navigation-header-buttons';
import Icon from 'react-native-vector-icons/dist/FontAwesome';
import Colors from '../constants/Colors';

const CustomHeaderButton = props => {
    return (
        <HeaderButton
            {...props}
            IconComponent={Icon}
            iconSize={23}
            color={Colors.primaryColor}
        />
    );
}

export default CustomHeaderButton;