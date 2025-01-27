import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';

// Styles
import SiteStyles from '@/styles/SiteStyles';
import TextStyles from '@/styles/TextStyles';
import buttonStyles from '@/styles/ButtonStyles';

interface ErrorViewProps {
    errorMessage: string;
    onPress: () => void;
}

/**
 * ErrorView component that displays an error message and a button to go back.
 *
 * @param {ErrorViewProps} props - The props for the component.
 * @param {string} props.errorMessage - The error message to display.
 * @param {() => void} props.onPress - Function to handle the button press.
 * @returns {JSX.Element} The rendered ErrorView component.
 */
const ErrorView: React.FC<ErrorViewProps> = ({ errorMessage, onPress }) => {
    return (
        <View style={SiteStyles.container}>
            <Text style={TextStyles.errorText}>{errorMessage}</Text>
            <TouchableOpacity style={buttonStyles.button} onPress={onPress}>
                <Text style={buttonStyles.buttonText}>Zurück</Text>
            </TouchableOpacity>
        </View>
    );
};

export default ErrorView;