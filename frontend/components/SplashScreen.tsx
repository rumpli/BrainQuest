import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { router } from 'expo-router';

/**
 * SplashScreen component that displays a countdown timer before navigating to the game screen.
 *
 * @returns {JSX.Element} The rendered SplashScreen component.
 */
const SplashScreen = () => {
    const [countdown, setCountdown] = useState(3);

    useEffect(() => {
        const interval = setInterval(() => {
            setCountdown(prevCountdown => prevCountdown - 1);
        }, 1000);

        if (countdown === 0) {
            clearInterval(interval);
            router.push('/game'); // Navigate to the quiz screen
        }

        return () => clearInterval(interval);
    }, [countdown]);

    return (
        <View style={styles.container}>
            <Text style={styles.text}>Quiz startet in {countdown} Sekunden...</Text>
        </View>
    );
};

/**
 * Styles for the SplashScreen component.
 */
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    text: {
        fontSize: 24,
        fontWeight: 'bold',
    },
});

export default SplashScreen;