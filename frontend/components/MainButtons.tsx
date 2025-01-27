import React from 'react';
import { View, TouchableOpacity, Text } from 'react-native';
import { useRouter } from 'expo-router';

// Styles
import ButtonStyles from "@/styles/ButtonStyles";

// Props
interface ButtonProps {
    /**
     * Optional callback for "Spiel Starten" button press.
     */
    onPressStart?: () => void;

    /**
     * Optional callback for "Highscore" button press.
     */
    onPressHighscore?: () => void;
}

// MainButtons component
/**
 * MainButtons component for the main menu of the game.
 *
 * @param {ButtonProps} props - The props for the component.
 * @param {() => void} [props.onPressStart] - Optional callback for "Spiel Starten" button press.
 * @param {() => void} [props.onPressHighscore] - Optional callback for "Highscore" button press.
 * @returns {JSX.Element} The rendered MainButtons component.
 */
export default function MainButtons({ onPressStart, onPressHighscore }: ButtonProps) {
    const router = useRouter();
    return (
        <View style={ButtonStyles.buttonContainer}>
            {/* "Spiel Starten" Button */}
            <TouchableOpacity style={ButtonStyles.button} onPress={onPressStart}>
                <Text style={ButtonStyles.buttonText}>Spiel Starten</Text>
            </TouchableOpacity>

            {/* "Highscore" Button */}
            <TouchableOpacity style={ButtonStyles.button} onPress={onPressHighscore || (() => router.push("/(highscore)/highscore"))}>
                <Text style={ButtonStyles.buttonText}>Highscore</Text>
            </TouchableOpacity>
        </View>
    );
}