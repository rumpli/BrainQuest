import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Text, Dimensions, ScrollView, ActivityIndicator } from 'react-native';
import { router } from 'expo-router';

// Utils
import { StorageService } from '@/utils/StorageService';
import log from "@/utils/logger";

// Components
import ErrorView from "@/components/ErrorView";
import SmallMainLogo from "@/components/SmallMainLogo";
import ErrorBoundary from '@/components/ErrorBoundary';

// Styles
import TextStyles from '@/styles/TextStyles';
import SiteStyles from "@/styles/SiteStyles";
import TopBarStyles from "@/styles/TopBarStyles";
import MaterialIcons from "@expo/vector-icons/MaterialIcons";

const { height } = Dimensions.get('window');

export default function Index() {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<Error | null>(null);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                log.info('Checking auth token...');
                const token = await StorageService.getItem('authToken');
                log.debug(`Token: ${token}`);
                if (!token) {
                    log.info('No auth token found, redirecting to login.');
                    router.push('/(login)/login');
                } else {
                    log.debug(`Auth token found: ${token}`);
                }
            } catch (err) {
                log.error('Error checking auth token:', err);
                setError(err as Error);
            } finally {
                setLoading(false);
            }
        };

        checkAuth();
    }, []);

    if (loading) {
        return <ActivityIndicator size="large" color="#0000ff" />;
    }

    if (error) {
        const errorMessage = `Error: ${error.message}`;
        return <ErrorView errorMessage={errorMessage} onPress={() => setError(null)} />;
    }

    return (
        <ErrorBoundary fallback={<Text>Something went wrong. Please try again later.</Text>}>
            <ScrollView style={SiteStyles.fullSite}>
                <View style={SiteStyles.container}>
                    <View style={[TopBarStyles.topBar, SiteStyles.column]}>
                        <Text style={TextStyles.title}>Admin Panel</Text>
                        <SmallMainLogo/>
                        <Text style={TextStyles.title}>Hier entsteht ein Admin Panel</Text>
                        <MaterialIcons name="construction" size={100} color="black" />
                        <Text style={TextStyles.title}>Coming soon...</Text>
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
