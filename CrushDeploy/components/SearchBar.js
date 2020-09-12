import React, { useState } from 'react';
import { View, TextInput, StyleSheet } from 'react-native';

import GeneralConstants from '../constants/GeneralConstants';

const SearchBar = props => {
    const [searchText, setSearchText] = useState('');

    const searchTextChanged = (text) => {
        setSearchText(text);
        props.onSearchTextChanged(text);
    }

    return (
        <View style={styles.searchBarView}>
            <TextInput
            style={styles.searchBar}
            onChangeText={text => searchTextChanged(text)}
            value={searchText}
            placeholder={"Search"} />
        </View>
    );
}

const styles = StyleSheet.create({
    searchBarView: {
        width: GeneralConstants.contactBarSize.width,
        height: 30,
        borderRadius: 10,
        marginTop: 15,
        marginBottom: 5,
    },
    searchBar: {
        backgroundColor: "#ccc",
        borderRadius: 15,
        padding: 7,
        textAlign: 'left',
    },
});

export default SearchBar;
