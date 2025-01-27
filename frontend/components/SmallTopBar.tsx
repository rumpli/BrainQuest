import React from 'react';
import { View, TouchableOpacity, Text } from 'react-native';
import { router } from 'expo-router';

// Icon
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

// Components
import SmallMainLogo from '@/components/SmallMainLogo';

// Styles
import TextStyles from '@/styles/TextStyles';
import TopBarStyles from '@/styles/TopBarStyles';

/**
 * SmallTopBar component that displays a top bar with a home button, logo, and title.
 *
 * @returns {JSX.Element} The rendered SmallTopBar component.
 */
export default function SmallTopBar() {
    return (
        <View style={TopBarStyles.smallTopBar}>
            <TouchableOpacity style={TopBarStyles.homeIcon} onPress={() => router.push('/')}>
                <MaterialIcons name="home" size={30} color="black"/>
            </TouchableOpacity>
            <View style={TopBarStyles.logoContainer}>
                <SmallMainLogo/>
            </View>
            <Text style={TextStyles.smallTopBarTitle}>Brain Quest</Text>
        </View>
    );
}