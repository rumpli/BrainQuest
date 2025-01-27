import React from 'react';
import { View, StyleSheet, Dimensions, StyleProp, ImageStyle } from 'react-native';
import { Image } from 'expo-image';

// Constants
const { height } = Dimensions.get('window');
const DefaultLogo = require('@/assets/images/brain_quest_logo.png');

/**
 * SmallMainLogo component that displays a logo image.
 *
 * @param {Object} props - The component props.
 * @param {StyleProp<ImageStyle>} [props.style] - Optional style to apply to the image.
 * @param {any} [props.imageSource] - Optional image source to use instead of the default logo.
 * @returns {JSX.Element} The rendered SmallMainLogo component.
 */
export default function SmallMainLogo({ style, imageSource }: { style?: StyleProp<ImageStyle>, imageSource?: any }) {
    return (
        <View style={styles.imageContainer}>
            <Image
                source={imageSource || DefaultLogo}
                style={[styles.logo, style]}
                contentFit="contain"  // Fit the image appropriately inside the container
                transition={0}  // Optional, adds a smooth transition on load
            />
        </View>
    );
}

/**
 * Styles for the SmallMainLogo component.
 */
const styles = StyleSheet.create({
    imageContainer: {
        width: '80%',
        maxWidth: 140,
        maxHeight: height * 0.7,
        aspectRatio: 1,
        borderRadius: 20,
        overflow: 'hidden',
        marginTop: 10,
    },
    logo: {
        width: '100%',
        height: '100%',
    }
});
