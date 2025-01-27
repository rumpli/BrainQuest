import React, { useState, useEffect } from 'react';
import {
    View,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    Dimensions,
    ActivityIndicator,
    ScrollView
} from 'react-native';
import { router } from 'expo-router';

// Components
import MainButtons from '@/components/MainButtons';
import MainLogo from '@/components/MainLogo';
import ErrorView from "@/components/ErrorView";
import ErrorBoundary from '@/components/ErrorBoundary';

// Utils
import { handleStartPress } from '@/utils/handleStartPress';
import { handleSubmitPress } from '@/utils/handleSubmitPress';
import { handleTopicPress } from "@/utils/handleTopicPress";
import { chunkArray } from "@/utils/chunkArray";
import { handleDifficultyPress } from "@/utils/handleDifficultyPress";
import { handleOperationSuccess } from '@/utils/handleOperationSuccess';
import { validatePlayerName } from '@/utils/validatePlayerName';
import log from "@/utils/logger";

// Styles
import IconStyles from '@/styles/IconStyles';
import ButtonStyles from '@/styles/ButtonStyles';
import TextStyles from '@/styles/TextStyles';
import SiteStyles from "@/styles/SiteStyles";
import TopBarStyles from "@/styles/TopBarStyles";

// Icons
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';

// Constants
const { height } = Dimensions.get('window');

/**
 * Main component for the main screen of the application.
 * @returns {JSX.Element} The rendered component.
 */
export default function Main() {
    const [started, setStarted] = useState(false);
    const [loading, setLoading] = useState(false);
    const [isSlow, setIsSlow] = useState(false);
    const [operationSucceeded, setOperationSucceeded] = useState(false);
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [nameSubmitted, setNameSubmitted] = useState(false);
    const [topicSubmitted, setTopicSubmitted] = useState(false);
    const [difficultySubmitted, setDifficultySubmitted] = useState(false);
    const [name, setName] = useState<string>('');
    const [topics, setTopics] = useState<string[]>([]);
    const [chosenTopic, setChosenTopic] = useState<any>(null);
    const [chosenTopicId, setChosenTopicId] = useState<number | null>(null);
    const [difficulties, setDifficulties] = useState<string[]>([]);
    const [error, setError] = useState<Error | null>(null);
    const [inputError, setInputError] = useState<string | null>(null);
    const [selectedDifficulty, setSelectedDifficulty] = useState<string | null>(null);
    const [isLoaded, setIsLoaded] = useState(false);

    /**
     * Resets the state of the component to its initial values.
     */
    const resetState = () => {
        setStarted(false);
        setIsSlow(false);
        setOperationSucceeded(false);
        setIsButtonDisabled(true);
        setNameSubmitted(false);
        setTopicSubmitted(false);
        setDifficultySubmitted(false);
        setName('');
        setTopics([]);
        setChosenTopic(null);
        setChosenTopicId(null);
        setDifficulties([]);
        setError(null);
        setInputError(null);
        setSelectedDifficulty(null);
    }

    /**
     * Starts the game by fetching topics and updating the state.
     */
    const startGame = () => {
        setIsSlow(false); // Reset "slow" state before starting the game
        setOperationSucceeded(false); // Reset the operation status
        handleStartPress(setStarted, setTopics, setLoading, setError, setIsSlow);
    };

    /**
     * Handles the selection of a topic.
     * @param {any} topic - The selected topic.
     * @returns {Promise<string>} The name of the selected topic.
     */
    const handleTopicSelection = async (topic: any) => {
        const difficultyArray = await handleTopicPress(setTopicSubmitted, setChosenTopic, topic);
        setDifficulties(difficultyArray);
        return topic.name;
    };

    /**
     * Handles changes to the player name input.
     * @param {string} input - The input value.
     */
    const handleNameChange = (input: string) => {
        const { isValid, error } = validatePlayerName(input);

        setName(input); // Update input in real-time
        if (!isValid) {
            setInputError(error);
            setIsButtonDisabled(true);
        } else {
            setInputError('');
            setIsButtonDisabled(false);
        }
    };

    // useEffects
    useEffect(() => {
        resetState();
        console.log('Reset state on HomePage mount');
    }, []);

    useEffect(() => {
        handleNameChange(name);
    }, [name]);

    useEffect(() => {
        if (isSlow && topics.length > 0) {
            log.debug('Topics fetched successfully after a delay.');
            // If topics are fetched successfully and `isSlow` is true, show success
            handleOperationSuccess(setOperationSucceeded, setIsSlow);
        }
    }, [isSlow, topics]);

    useEffect(() => {
        // Force a re-render or trigger the loading state immediately on mount
        setIsLoaded(true);
    }, []);

    // Error handling
    if (error) {
        const errorMessage = `Error: ${error.message}`;
        return <ErrorView errorMessage={errorMessage} onPress={() => router.push('/')} />;
    }

    // render
    return (
        <ErrorBoundary fallback={<Text>Something went wrong. Please try again later.</Text>}>
            <ScrollView style={SiteStyles.fullSite}>
                <View style={SiteStyles.container}>
                    <View style={TopBarStyles.topBar}>
                        { nameSubmitted ? (
                            <Text style={TextStyles.title}>
                                Willkommen <Text style={TextStyles.nameText}>{name}</Text>
                            </Text>
                        ) : (
                            <Text style={TextStyles.title}>Brain Quest</Text>
                        )}
                        { started ? (
                            <TouchableOpacity style={IconStyles.homeIcon} onPress={() => {
                                router.push('/')
                            }}>
                                <MaterialIcons name="home" size={30} color="black" />
                            </TouchableOpacity>
                        ) : (
                            <TouchableOpacity style={IconStyles.loginIcon} onPress={() => {
                                router.push("/(login)/login")
                            }}>
                                <MaterialIcons name="login" size={30} color="black" />
                            </TouchableOpacity>
                        )}
                    </View>
                    <MainLogo/>
                    { nameSubmitted && topicSubmitted && difficultySubmitted ? (
                        // Start game
                        <View style={styles.column}>
                            <View style={styles.column}>
                                <View style={styles.row}>
                                    <Text style={TextStyles.normalText}>Spielername:</Text>
                                    <TouchableOpacity style={ButtonStyles.button} onPress={() => setNameSubmitted(false)}>
                                        <Text style={ButtonStyles.buttonText}>{name}</Text>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.row}>
                                    <Text style={TextStyles.normalText}>Thema:</Text>
                                    <TouchableOpacity style={ButtonStyles.button} onPress={() => {
                                        setTopicSubmitted(false);
                                        setDifficultySubmitted(false)}}>
                                        <Text style={ButtonStyles.buttonText}>{chosenTopic}</Text>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.row}>
                                    <Text style={TextStyles.normalText}>Schwierigkeit:</Text>
                                    <TouchableOpacity style={ButtonStyles.button} onPress={() => setDifficultySubmitted(false)}>
                                        <Text style={ButtonStyles.buttonText}>{selectedDifficulty}</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>
                            <TouchableOpacity
                                style={ButtonStyles.button}
                                onPress={() => {
                                    const encodedParams = btoa(JSON.stringify({ chosenTopicId, chosenTopic, selectedDifficulty, name }));
                                    log.debug(`Starting game with parameters: ${encodedParams}`);
                                    router.push(`/(game)/game?params=${encodedParams}`);
                                }}
                            >
                                <Text style={ButtonStyles.buttonText}>Jetzt starten</Text>
                            </TouchableOpacity>
                        </View>
                    ) : nameSubmitted && !topicSubmitted? (
                        // Topic selection
                        <View style={styles.askUser}>
                            {chunkArray(topics, 3).map((row, rowIndex) => (
                                <View key={rowIndex} style={styles.row}>
                                    {row.map((topic, index) => (
                                        <TouchableOpacity
                                            key={topic.id}
                                            style={ButtonStyles.button}
                                            onPress={async () => {
                                                const chosenTopic = await handleTopicSelection(topic);
                                                log.debug(`Chosen topic: ${chosenTopic}`);
                                                setChosenTopic(chosenTopic);
                                                log.debug(`Chosen topic ID: ${topic.id}`);
                                                setChosenTopicId(topic.id);
                                            }}>
                                            <Text style={ButtonStyles.buttonText}>{topic.name}</Text>
                                        </TouchableOpacity>
                                    ))}
                                </View>
                            ))}
                        </View>
                    ) : nameSubmitted && !difficultySubmitted && topicSubmitted ? (
                        // Difficulty selection
                        <View style={styles.askUser}>
                            {chunkArray(difficulties, 3).map((row, rowIndex) => (
                                <View key={rowIndex} style={styles.row}>
                                    {row.map((difficulty, index) => (
                                        <TouchableOpacity
                                            key={index}
                                            style={ButtonStyles.button}
                                            onPress={async () => {
                                                const difficultyValue = await handleDifficultyPress(setDifficultySubmitted, difficulty);
                                                setSelectedDifficulty(difficultyValue);
                                            }}>
                                            <Text style={ButtonStyles.buttonText}>{difficulty}</Text>
                                        </TouchableOpacity>
                                    ))}
                                </View>
                            ))}
                        </View>
                    ) : (
                        started && !loading && !operationSucceeded ? (
                            // Player name input
                            <View style={styles.askPlayerNameContainer}>
                                <View style={styles.askPlayerName}>
                                    <TextInput
                                        style={[styles.input, inputError ? styles.inputError : null]}
                                        value={name}
                                        onChangeText={(text) => setName(text)}
                                        placeholder="Spielername eingeben (max. 20 Zeichen)"
                                        placeholderTextColor={'gray'}
                                        maxLength={20}
                                    />
                                    <TouchableOpacity style={[ButtonStyles.button, isButtonDisabled && ButtonStyles.disabledButton]}
                                                      onPress={() => handleSubmitPress(setNameSubmitted, name)}
                                                      disabled={isButtonDisabled}
                                    >
                                        <Text style={ButtonStyles.buttonText}>Bestätigen</Text>
                                    </TouchableOpacity>
                                </View>
                                {inputError ? <Text style={TextStyles.errorText}>{inputError}</Text> : null}
                            </View>
                        ) : loading || operationSucceeded ? (
                            <View style={{ marginVertical: 20, alignItems: 'center' }}>
                                <ActivityIndicator size="large" color="#0000ff" />
                                { isSlow && (
                                    operationSucceeded ? (
                                        <View style={styles.column}>
                                            <TouchableOpacity style={IconStyles.kettleIcon}>
                                                <MaterialCommunityIcons name="kettle-steam" size={30} color="black" />
                                            </TouchableOpacity>
                                            <Text style={{ marginTop: 10, color: 'green' }}>
                                                Erfolgreich - du wirst gleich weitergeleitet!
                                            </Text>
                                        </View>
                                    ) : (
                                        <View style={styles.column}>
                                            <TouchableOpacity style={IconStyles.kettleIcon}>
                                                <MaterialCommunityIcons name="kettle-alert" size={30} color="black" />
                                            </TouchableOpacity>
                                            <Text style={{ marginTop: 10, color: 'orange' }}>
                                                Dies dauert länger als gewöhnlich...
                                            </Text>
                                        </View>
                                    )
                                )}
                            </View>
                        ) : (
                            <MainButtons
                                onPressStart={startGame}
                            />
                        )
                    )}
                </View>
            </ScrollView>
        </ErrorBoundary>
    );
}

// Styles
const styles = StyleSheet.create({
    input: {
        flex: 1,
        borderColor: 'gray',
        borderWidth: 1,
        paddingLeft: 10,
        marginRight: 20,
    },
    inputError: {
        borderColor: 'red',
    },
    askPlayerName: {
        flex: 1,
        flexDirection: "row",
        paddingTop: height * 0.05,
        paddingBottom: 10,
        justifyContent: "space-between",
    },
    askPlayerNameContainer: {
        flex: 1,
        flexDirection: "column",
        maxWidth: 500,
        width: '80%',
        justifyContent: "space-between",
    },
    askUser: {
        flexDirection: "column",
        paddingTop: height * 0.05,
        justifyContent: "space-between",
        width: '80%',
        maxWidth: 500,
    },
    row: {
        flexDirection: "row",
        justifyContent: "space-between",
        width: '100%',
        marginBottom: 10,
    },
    column: {
        flexDirection: "column",
        justifyContent: "space-between",
        width: '100%',
        maxWidth: 500,
    },
});