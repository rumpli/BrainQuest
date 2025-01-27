import React from "react";
import {View, StyleSheet, Text, TouchableOpacity} from 'react-native';
import { router } from 'expo-router';

// Components
import MainLogo from "@/components/MainLogo";

// Styles
import ButtonStyles from "@/styles/ButtonStyles";

// Utils
import logger from "@/utils/logger";

// Images
const NotFoundLogo = require('@/assets/images/brain_quest_logo_404.png');

/**
 * NotFoundScreen component that displays a 404 error message and a button to navigate back to the home screen.
 *
 * @returns {JSX.Element} The rendered NotFoundScreen component.
 */
export default function NotFoundScreen() {
    logger.warn('404 page not found');

    return (
        <View style={styles.container}>
            <View style={styles.topBar}>
                <Text style={styles.title}>Dies ist nicht die Seite die Ihr sucht.</Text>
            </View>
            <MainLogo imageSource={NotFoundLogo} />
            <View style={[ButtonStyles.buttonContainer, styles.customButtonContainer]}>
                <TouchableOpacity style={ButtonStyles.button} onPress={() => router.push('/')}>
                    <Text style={ButtonStyles.buttonText}>Zurück zum Home Screen!</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: "100%",
        height: "100%",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "#fff",
    },
    topBar: {
        justifyContent: "space-between",
    },
    title: {
        fontSize: 24,
        fontWeight: "bold",
        marginBottom: 50,
    },
    customButtonContainer: {
        justifyContent: "center",
    },
});