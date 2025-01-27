import React, { useState, useEffect } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    ActivityIndicator,
    ScrollView,
} from 'react-native';
import { router, useLocalSearchParams } from "expo-router";

interface RouteParams {
    topicId: number;
    difficulty: string;
}

// Components
import ErrorBoundary from '@/components/ErrorBoundary';
import SmallTopBar from '@/components/SmallTopBar';
import ErrorView from '@/components/ErrorView';
import HighscoreTable from "@/components/HighscoreTable";
import OperationStatus from "@/components/OperationStatus";

// Utils
import { handleOperationSuccess } from '@/utils/handleOperationSuccess';
import { handleFetchHighscores } from '@/utils/handleFetchHighscores';
import log from '@/utils/logger';

// Styles
import ButtonStyles from '@/styles/ButtonStyles';
import TextStyles from '@/styles/TextStyles';
import SiteStyles from "@/styles/SiteStyles";
import buttonStyles from "@/styles/ButtonStyles";

/**
 * Main component for the main screen of the application.
 * @returns {JSX.Element} The rendered component.
 */
export default function Index() {
    const [loading, setLoading] = useState(false);
    const [loadingTable, setLoadingTable] = useState(false);
    const [isSlow, setIsSlow] = useState(false);
    const [highscores, setHighscores] = useState<any[]>([]);
    const [operationSucceeded, setOperationSucceeded] = useState(false);
    const [error, setError] = useState<Error | null>(null);
    const [difficulty, setDifficulty] = useState<string>('');
    const [topicId, setTopicId] = useState<number | null>(null);
    const [paramsProcessed, setParamsProcessed] = useState(false);
    const [paramsProcessError, setParamsProcessError] = useState(false);

    const encodedParams = useLocalSearchParams<RouteParams>();

    /**
     * Fetches the highscores based on the selected topic and difficulty.
     */
    const getHighscores = () => {
        setHighscores([]);
        setIsSlow(false); // Reset "slow" state before starting the game
        setOperationSucceeded(false); // Reset the operation status
        handleFetchHighscores(setHighscores, setLoadingTable, setError, setIsSlow, topicId, difficulty, 'SCORE', 'DESC');
    }

    /**
     * Handles navigation to the home screen in case of an error.
     */
    const handleErrorHome = () => {
        log.debug('Navigating to home screen...');
        router.push('/');
    }

    useEffect(() => {
        getHighscores();
    }, []);

    useEffect(() => {
        if (isSlow && highscores.length > 0) {
            log.debug('API call succeeded after a delay.');
            handleOperationSuccess(setOperationSucceeded, setIsSlow);
        }
    }, [isSlow, highscores]);

    if (encodedParams && !paramsProcessed) {
        try {
            log.debug('Encoded params:', encodedParams);
            // Decode URL-encoded Base64 string first
            const cleanEncodedParams = decodeURIComponent(encodedParams.params);
            const decodedParams = JSON.parse(atob(cleanEncodedParams));
            log.debug('Decoded params:', decodedParams);
            setTopicId(decodedParams.topicId);
            setDifficulty(decodedParams.difficulty);
            log.debug(`
                Selected topicID: ${decodedParams.topicId},
                difficulty: ${decodedParams.difficulty}
            `);
            setParamsProcessed(true);
        } catch (error) {
            log.error('Error to decode params:', error);
            setParamsProcessed(true);
            setParamsProcessError(true);
        }
    }

    if (paramsProcessError) {
        return (
            <View style={SiteStyles.container}>
                <Text style={TextStyles.errorText}>Ein Fehler ist aufgetreten.</Text>
                <TouchableOpacity style={buttonStyles.button} onPress={handleErrorHome}>
                    <Text style={buttonStyles.buttonText}>Zurück</Text>
                </TouchableOpacity>
            </View>
        );
    }

    if (paramsProcessError || error) {
        const errorMessage = paramsProcessError ? 'Ein Fehler ist aufgetreten.' : `Error: ${error.message}`;
        return <ErrorView errorMessage={errorMessage} onPress={handleErrorHome} />;
    }

    return (
        <ErrorBoundary fallback={<Text>Something went wrong. Please try again later.</Text>}>
            <ScrollView style={SiteStyles.fullSite}>
                <View style={SiteStyles.container}>
                    <SmallTopBar/>
                    <View style={SiteStyles.highscoreContainer}>
                        { loading ? (
                            <View style={{ marginVertical: 20, alignItems: 'center' }}>
                                <ActivityIndicator size="large" color="#0000ff" />
                                { isSlow && (
                                    <OperationStatus operationSucceeded={operationSucceeded}/>
                                )}
                            </View>
                        ) : ( loadingTable || (operationSucceeded && !loading)) ? (
                            <View style={{ marginVertical: 20, alignItems: 'center' }}>
                                <ActivityIndicator size="large" color="#0000ff" />
                                { isSlow && (
                                    <OperationStatus operationSucceeded={operationSucceeded}/>
                                )}
                            </View>
                        ) : (
                            <HighscoreTable highscores={highscores}/>
                        )}
                    </View>
                    <TouchableOpacity style={[ButtonStyles.button, {marginTop: 10}]} onPress={() => router.push('/')}>
                        <Text style={ButtonStyles.buttonText}>Hauptmenü</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>
        </ErrorBoundary>
    );
}