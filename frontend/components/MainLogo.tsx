import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Dimensions, StyleProp, ImageStyle, ActivityIndicator } from 'react-native';
import { Image } from 'expo-image';  // Use expo-image

// Constants
const { height } = Dimensions.get('window');
const brain_quest_logo = require('../assets/images/brain_quest_logo_live.png');  // Import the image

/**
 * MainLogo component that displays a logo image.
 *
 * @param {Object} props - The component props.
 * @param {StyleProp<ImageStyle>} [props.style] - Optional style to apply to the image.
 * @param {any} [props.imageSource] - Optional image source to use instead of the default logo.
 * @returns {JSX.Element} The rendered MainLogo component.
 */
export default function MainLogo({ style, imageSource }: { style?: StyleProp<ImageStyle>, imageSource?: any }) {
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Preload the image when the component mounts
        // This is not strictly necessary if you're using expo-image, but it's fine to leave for other logic
        const preloadImage = async () => {
            // If imageSource is passed, use it; otherwise, use the default logo
            const imageAsset = imageSource || brain_quest_logo;
            setLoading(false);  // Set loading to false as soon as the component is mounted
        };

        preloadImage();
    }, [imageSource]);

    const handleImageLoad = () => {
        setLoading(false);  // Image has finished loading
    };

    return (
        <View style={styles.imageContainer}>
            {loading && <ActivityIndicator size="large" color="#0000ff" />}
            <Image
                source={imageSource || brain_quest_logo}  // Fallback to default logo
                style={[styles.logo, style]}
                contentFit="contain"
                onLoad={handleImageLoad}  // Trigger state update when image is loaded
                transition={0}
            />
        </View>
    );
}

/**
 * Styles for the MainLogo component.
 */
const styles = StyleSheet.create({
    imageContainer: {
        width: '80%',
        maxWidth: 500,
        maxHeight: height * 0.7,
        aspectRatio: 1,
        borderRadius: 100,
        overflow: 'hidden',
        marginBottom: 30,
    },
    logo: {
        width: '100%',
        height: '100%',
    }
});
