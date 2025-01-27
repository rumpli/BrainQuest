import React, {useEffect, useState} from 'react';
import {
    View,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    Dimensions,
    ScrollView,
    ActivityIndicator,
    Modal,
    Platform,
} from 'react-native';
import { router } from 'expo-router';

// Utils
import { login } from '@/utils/api';
import { StorageService } from '@/utils/StorageService';
import log from '@/utils/logger';

// Components
import MainLogo from '@/components/MainLogo';
import ErrorBoundary from '@/components/ErrorBoundary';

// Styles
import ButtonStyles from '@/styles/ButtonStyles';
import TextStyles from '@/styles/TextStyles';
import SiteStyles from '@/styles/SiteStyles';
import TopBarStyles from '@/styles/TopBarStyles';
import ModalStyles from "@/styles/ModalStyles";
import buttonStyles from "@/styles/ButtonStyles";

// Constants
const { height } = Dimensions.get('window');

/**
 * The login screen component.
 *
 * @returns {JSX.Element} The rendered login screen.
 */
export default function Index(): JSX.Element {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [loginError, setLoginError] = useState(false);
    const [error, setError] = useState<Error | null>(null);
    const [platform, setPlatform] = useState<string | null>(null);

    /**
     * Handles the login button press event.
     * Attempts to log in the user with the provided username and password.
     */
    const handleLoginPress = async () => {
        setLoading(true);
        try {
            const response = await login(username, password);

            // Store the token using the unified StorageService
            await StorageService.setItem('authToken', response.accessToken);
            log.debug('Token set successfully');

            const token = await StorageService.getItem('authToken');
            log.debug(`Token: ${token}`);
            log.info(`Logged in successfully with username: ${username}`);
            router.push('/(admin)/admin');
        } catch (err) {
            log.error('Error logging in:', err);
            setError(err as Error);
            setLoginError(true);
        } finally {
            setLoading(false);
        }
    };

    const closeLoginError = () => {
        setLoginError(false);
    }

    useEffect(() => {
        setPlatform(Platform.OS);
    }, []);

    return (
        <ErrorBoundary fallback={<Text>Something went wrong. Please try again later.</Text>}>
            <ScrollView style={SiteStyles.fullSite}>
                <View style={SiteStyles.container}>
                    <Modal
                        animationType={ platform === 'web' ? "fade": "slide" }
                        transparent={true}
                        visible={loginError}
                        onRequestClose={closeLoginError}
                    >
                        <View style={ModalStyles.modalContainer}>
                            <View style={ModalStyles.modalView}>
                                <Text style={ModalStyles.modalText}>Fehler beim Einloggen:</Text>
                                <Text style={ModalStyles.modalText}>{error?.message}</Text>
                                <TouchableOpacity style={buttonStyles.button} onPress={closeLoginError}>
                                    <Text style={buttonStyles.buttonText}>Schließen</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </Modal>
                    <View style={TopBarStyles.topBar}>
                        <MainLogo/>
                    </View>
                    <Text style={TextStyles.title}>Login</Text>
                    <View style={styles.form}>
                        <TextInput
                            style={styles.input}
                            placeholder="Username"
                            value={username}
                            onChangeText={setUsername}
                            autoCapitalize="none"
                        />
                        <TextInput
                            style={styles.input}
                            placeholder="Password"
                            value={password}
                            onChangeText={setPassword}
                            secureTextEntry
                        />
                        <View style={SiteStyles.row}>
                            <TouchableOpacity
                                style={[ButtonStyles.button, loading && ButtonStyles.disabledButton]}
                                onPress={handleLoginPress}
                                disabled={loading}
                            >
                                {loading ? (
                                    <ActivityIndicator size="small" color="#fff" />
                                ) : (
                                    <Text style={ButtonStyles.buttonText}>Login</Text>
                                )}
                            </TouchableOpacity>
                            <TouchableOpacity
                                style={ButtonStyles.button}
                                onPress={() => router.push('/')}
                            >
                                <Text style={ButtonStyles.buttonText}>Zurück</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </ScrollView>
        </ErrorBoundary>
    );
}

const styles = StyleSheet.create({
    form: {
        width: '80%',
        maxWidth: 400,
        alignSelf: 'center',
        marginTop: height * 0.1,
    },
    input: {
        borderColor: 'gray',
        borderWidth: 1,
        padding: 10,
        marginBottom: 20,
        borderRadius: 5,
    },
});