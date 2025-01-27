import React, { useState, useEffect, useRef } from 'react';
import {
    View,
    StyleSheet,
    Text,
    TouchableOpacity,
    ActivityIndicator,
    ScrollView,
    Modal,
    Platform
} from 'react-native';
import { router, useLocalSearchParams, } from 'expo-router';

interface RouteParams {
    chosenTopicId: number;
    chosenTopic: string;
    selectedDifficulty: string;
    playerName: string;
}

// Components
import ErrorBoundary from '@/components/ErrorBoundary';
import QuizTopBar from "@/components/QuizTopBar";
import SplashScreen from "@/components/SplashScreen";
import QuizButton from "@/components/QuizButton";
import ErrorView from "@/components/ErrorView";
import OperationStatus from "@/components/OperationStatus";

// Utils
import { handleOperationSuccess } from '@/utils/handleOperationSuccess';
import { handleFetchQuestions } from "@/utils/handleFetchQuestions";
import { handleCheckAnswer } from "@/utils/handleCheckAnswer";
import { handleFetchJoker } from "@/utils/handleFetchJoker";
import log from '@/utils/logger';

// Styles
import IconStyles from '@/styles/IconStyles';
import ButtonStyles from '@/styles/ButtonStyles';
import TextStyles from '@/styles/TextStyles';
import SiteStyles from "@/styles/SiteStyles";
import ModalStyles from "@/styles/ModalStyles";
import textStyles from "@/styles/TextStyles";
import buttonStyles from '@/styles/ButtonStyles';

// Icons
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

/**
 * Main component for the main screen of the application.
 * @returns {JSX.Element} The rendered component.
 */
export default function Index() {
    const [loading, setLoading] = useState(false);
    const [isSlow, setIsSlow] = useState(false);
    const [questions, setQuestions] = useState<any[]>([]);
    const [operationSucceeded, setOperationSucceeded] = useState(false);
    const [topic, setTopic] = useState<any[]>([]);
    const [topicId, setTopicId] = useState(0);
    const [difficulty, setDifficulty] = useState('');
    const [error, setError] = useState<Error | null>(null);
    const [isJokerButtonDisabled, setJokerButtonDisabled] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [countJoker, setCountJoker] = useState(2);
    const [seconds, setSeconds] = useState(0);
    const [points, setPoints] = useState(0);
    const [selectedAnswer, setSelectedAnswer] = useState(0);
    const [showSplash, setShowSplash] = useState(true);
    const [paramsProcessed, setParamsProcessed] = useState(false);
    const [paramsProcessError, setParamsProcessError] = useState(false);
    const [excludeQuestions, setExcludeQuestions] = useState<any[]>([]);
    const [stopTimer, setStopTimer] = useState(true);
    const [allAnswerButtonsDisabled, setAllAnswerButtonsDisabled] = useState(false);
    const [score, setScore] = useState(0);
    const [questionCount, setQuestionCount] = useState(0);
    const [playerName, setPlayerName] = useState('');
    const [gameOver, setGameOver] = useState(false);
    const [gameBeaten, setGameBeaten] = useState(false);
    const [info, setInfo] = useState('');
    const [showMoreInformation, setShowMoreInformation] = useState(false);
    const [correctAnswerId, setCorrectAnswerId] = useState<number | null>(null);
    const [showSubmitButton, setShowSubmitButton] = useState(false);
    const [isInfoButtonDisabled, setIsInfoButtonDisabled] = useState(true);
    const [platform, setPlatform] = useState<string | null>(null);

    // Quiz Questions
    const [question, setQuestion] = useState('');
    const [answerOne, setAnswerOne] = useState('');
    const [answerTwo, setAnswerTwo] = useState('');
    const [answerThree, setAnswerThree] = useState('');
    const [answerFour, setAnswerFour] = useState('');

    const [answerOneId, setAnswerOneId] = useState(0);
    const [answerTwoId, setAnswerTwoId] = useState(0);
    const [answerThreeId, setAnswerThreeId] = useState(0);
    const [answerFourId, setAnswerFourId] = useState(0);

    // Quiz Buttons
    const [isAnswerOneSelected, setIsAnswerOneSelected] = useState(false);
    const [isAnswerTwoSelected, setIsAnswerTwoSelected] = useState(false);
    const [isAnswerThreeSelected, setIsAnswerThreeSelected] = useState(false);
    const [isAnswerFourSelected, setIsAnswerFourSelected] = useState(false);

    const [isAnswerOneButtonShown, setIsAnswerOneButtonShown] = useState(true);
    const [isAnswerTwoButtonShown, setIsAnswerTwoButtonShown] = useState(true);
    const [isAnswerThreeButtonShown, setIsAnswerThreeButtonShown] = useState(true);
    const [isAnswerFourButtonShown, setIsAnswerFourButtonShown] = useState(true);

    const encodedParams = useLocalSearchParams<RouteParams>();

    /**
     * Shows or hides the answer buttons based on the provided boolean values.
     * @param {boolean} showOne - Whether to show the first answer button.
     * @param {boolean} showTwo - Whether to show the second answer button.
     * @param {boolean} showThree - Whether to show the third answer button.
     * @param {boolean} showFour - Whether to show the fourth answer button.
     */
    const showButtons = (showOne: boolean, showTwo: boolean, showThree: boolean, showFour: boolean) => {
        setIsAnswerOneButtonShown(showOne);
        setIsAnswerTwoButtonShown(showTwo);
        setIsAnswerThreeButtonShown(showThree);
        setIsAnswerFourButtonShown(showFour);
    };

    /**
     * Uses the 50:50 joker to eliminate two incorrect answers.
     */
    const useJoker = async () => {
        setCountJoker((prevCount) => Math.max(prevCount - 1, 0)); // Ensure the count does not go below 0
        log.debug('Using 50:50 Joker...');

        try {
            // Fetch joker answers
            const newJokerAnswers = await handleFetchJoker(
                setLoading,
                setError,
                setIsSlow,
                questions[0].id, // Assuming questions[0] exists and has an id
                'FIFTY_FIFTY' // Type of joker
            );

            log.debug('Fetched joker answers:', newJokerAnswers);

            if (newJokerAnswers && newJokerAnswers.length > 0) {
                // Extract the answer IDs from the response
                const answerIds = newJokerAnswers[0].answers.map((answer) => answer.id); // This is an array of answer IDs

                // Initialize an array to track button visibility
                const showStates = [false, false, false, false]; // Default: hide all buttons
                setJokerButtonDisabled(true); // Disable the joker button after using it

                // Match the IDs from the API response with the answer buttons (0-based index)
                answerIds.forEach((id) => {
                    // Assuming that the answer buttons are indexed from 1 to 4
                    switch (id) {
                        case answerOneId:
                            showStates[0] = true; // Show answer 1 if the ID matches
                            break;
                        case answerTwoId:
                            showStates[1] = true; // Show answer 2 if the ID matches
                            break;
                        case answerThreeId:
                            showStates[2] = true; // Show answer 3 if the ID matches
                            break;
                        case answerFourId:
                            showStates[3] = true; // Show answer 4 if the ID matches
                            break;
                        default:
                            break;
                    }
                });

                // Update button visibility
                showButtons(showStates[0], showStates[1], showStates[2], showStates[3]);
            } else {
                log.error('Unexpected joker answers format:', newJokerAnswers);
            }
        } catch (error) {
            log.error('Failed to fetch joker answers:', error);
        }
    };

    /**
     * Handles navigation to the home screen in case of an error.
     */
    const handleErrorHome = () => {
        log.debug('Navigating to home screen...');
        router.push('/');
    }

    /**
     * Ends the quiz and navigates to the home screen.
     */
    const endQuiz = () => {
        handleHomePress();
    }

    /**
     * Handles the press event for the home button.
     */
    const handleHomePress = () => {
        setModalVisible(true);
    };

    /**
     * Confirms the end of the quiz and navigates to the highscore screen.
     */
    const handleConfirm = () => {
        setModalVisible(false);
        setGameBeaten(false);
        const encodedParams = btoa(JSON.stringify({ topicId, difficulty }));
        log.debug(`Going to highscore screen with parameters: ${encodedParams}`);
        router.push(`/(game)/highscore?params=${encodedParams}`);
    };

    /**
     * Cancels the end of the quiz and closes the modal.
     */
    const handleCancel = () => {
        setModalVisible(false);
    };

    /**
     * Selects an answer for the current question.
     * @param {number} answer - The selected answer number.
     */
    const selectAnswer = (answer: number) => {
        setIsAnswerOneSelected(answer === 1);
        setIsAnswerTwoSelected(answer === 2);
        setIsAnswerThreeSelected(answer === 3);
        setIsAnswerFourSelected(answer === 4);
    }

    /**
     * Sets the timer duration based on the selected difficulty.
     */
    const getTimes = () => {
        if (difficulty === 'EASY') {
            setSeconds(60);
        } else if (difficulty === 'MEDIUM') {
            setSeconds(45);
        } else if (difficulty === 'HARD') {
            setSeconds(30);
        }
    }

    /**
     * Formats the given time in seconds to a string in the format MM:SS.
     * @param {number} totalSeconds - The total time in seconds.
     * @returns {string} The formatted time string.
     */
    const formatTime = (totalSeconds: number) => {
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;
        return `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    };

    /**
     * Loads the next question for the quiz.
     */
    const loadQuestion = () => {
        log.debug('Loading questions...');

        if (countJoker > 0) {
            setJokerButtonDisabled(false);
        }

        selectAnswer(0);
        setIsInfoButtonDisabled(true);
        showButtons(true, true, true, true);
        setAllAnswerButtonsDisabled(false);
        getTimes();
        setQuestionCount(questionCount + 1);
        setIsSlow(false); // Reset "slow" state before starting the game
        setOperationSucceeded(false); // Reset the operation status

        setQuestions([]); // Ensure we clear previous questions before loading new ones

        handleFetchQuestions(
            setQuestions,
            setLoading,
            setError,
            setIsSlow,
            setExcludeQuestions,
            topicId,
            difficulty,
            excludeQuestions,
            playerName,
            score
        ).then((hasValidQuestions) => {
            log.debug(`Has valid questions: ${hasValidQuestions}`);
            if (hasValidQuestions) {
                log.debug('Questions loaded successfully.');
                setStopTimer(false);
            } else if (!hasValidQuestions) {
                setAllAnswerButtonsDisabled(true);
                setJokerButtonDisabled(true);
                setGameOver(true);
                setGameBeaten(true); // Game beaten should trigger here
                log.debug('Game beaten!');
                return;
            }
        }).catch((error) => {
            log.error('Error loading questions:', error);
        });
    };

    /**
     * Handles changes to the questions and updates the state accordingly.
     * @param {any[]} questions - The array of questions.
     */
    const handleQuestionChange = (questions: any[]) => {
        if (questions && questions.length > 0) {
            log.debug('Questions:', questions);

            // Get the first question from the array
            const question = questions[0];

            setQuestion(question?.question || '');

            // Access answers properly (as an array)
            setAnswerOne(question?.answers[0]?.answer || '');
            setAnswerOneId(question?.answers[0]?.id || 0);

            setAnswerTwo(question?.answers[1]?.answer || '');
            setAnswerTwoId(question?.answers[1]?.id || 0);

            setAnswerThree(question?.answers[2]?.answer || '');
            setAnswerThreeId(question?.answers[2]?.id || 0);

            setAnswerFour(question?.answers[3]?.answer || '');
            setAnswerFourId(question?.answers[3]?.id || 0);
        }
    };

    /**
     * Checks the selected answer and updates the state based on the result.
     */
    const answer = () => {
        log.info('Checking answer...');
        !stopTimer ? setStopTimer(true) : null;
        setJokerButtonDisabled(true);
        setAllAnswerButtonsDisabled(true);
        setIsInfoButtonDisabled(false);

        handleCheckAnswer(
            setCorrectAnswerId,
            setLoading,
            setError,
            setIsSlow,
            setScore,
            setInfo,
            questions[0].id,
            (
                isAnswerOneSelected ? answerOneId :
                    isAnswerTwoSelected ? answerTwoId :
                        isAnswerThreeSelected ? answerThreeId :
                            isAnswerFourSelected ? answerFourId : 0
            ),
            playerName,
            score
        ).then(({ isCorrect, correctAnswerId }) => {
            if (!isCorrect) {
                log.debug('Answer is incorrect!');
                setGameOver(true);
                setCorrectAnswerId(correctAnswerId);
                setSelectedAnswer(
                    isAnswerOneSelected ? 1 :
                        isAnswerTwoSelected ? 2 :
                            isAnswerThreeSelected ? 3 :
                                isAnswerFourSelected ? 4 : 0
                );
            } else {
                log.debug('Answer is correct!');
                setPoints(points + 1);
                setShowSubmitButton(true);
            }
        }).catch((error) => {
            log.error('Error checking answer:', error);
        });
    };

    useEffect(() => {
        if (countJoker === 0) {
            setJokerButtonDisabled(true); // Disable button when jokers run out
        }
    }, [countJoker]);

    useEffect(() => {
        handleQuestionChange(questions);
    }, [questions]);

    useEffect(() => {
        setPlatform(Platform.OS);
    }, []);

    useEffect(() => {
        if (isSlow && questions.length === 0) {
            log.debug('API call succeeded after a delay.');
            handleOperationSuccess(setOperationSucceeded, setIsSlow);
        }
    }, [isSlow, questions]);

    const intervalRef = useRef<NodeJS.Timeout | null>(null);

    useEffect(() => {
        if (!stopTimer && !paramsProcessError) {
            log.info('Starting timer...');
            intervalRef.current = setInterval(() => {
                setSeconds(prevSeconds => {
                    if (prevSeconds <= 1) {
                        clearInterval(intervalRef.current!);
                        setStopTimer(true);
                        log.info('Time is up!');
                        answer();
                        return 0;
                    }
                    return prevSeconds - 1;
                });
            }, 1000);
        }

        return () => {
            if (intervalRef.current) {
                clearInterval(intervalRef.current);
            }
        };
    }, [stopTimer]);

    useEffect(() => {
        const splashTimeout = setTimeout(() => {
            setShowSplash(false);
        }, 3000); // Show splash for 3 seconds
        return () => clearTimeout(splashTimeout); // Clear the timer when component unmounts
    }, []);

    useEffect(() => {
        const init = async () => {
            loadQuestion();
            getTimes();
        };
        init();
    }, []);

    if (encodedParams && !paramsProcessed) {
        try {
            log.debug('Encoded params:', encodedParams);
            // Decode URL-encoded Base64 string first
            const cleanEncodedParams = decodeURIComponent(encodedParams.params);
            const decodedParams = JSON.parse(atob(cleanEncodedParams));
            log.debug('Decoded params:', decodedParams);
            setTopicId(decodedParams.chosenTopicId);
            setTopic(decodedParams.chosenTopic);
            setDifficulty(decodedParams.selectedDifficulty);
            setPlayerName(decodedParams.name);
            log.debug(`
                Selected topicID: ${decodedParams.chosenTopicId},
                topic: ${decodedParams.chosenTopic},
                difficulty: ${decodedParams.selectedDifficulty}
            `);
            setParamsProcessed(true);
        } catch (error) {
            log.error('Error to decode params:', error);
            setParamsProcessed(true);
            setParamsProcessError(true);
        }
    }

    if (paramsProcessError || error) {
        const errorMessage = paramsProcessError ? 'Ein Fehler ist aufgetreten.' : `Error: ${error.message}`;
        return <ErrorView errorMessage={errorMessage} onPress={handleErrorHome} />;
    }

    if (showSplash && paramsProcessed && !paramsProcessError) {
        return <SplashScreen />;
    }

    return (
        <ErrorBoundary fallback={<Text>Something went wrong. Please try again later.</Text>}>
            <ScrollView style={SiteStyles.fullSite}>
                <View style={SiteStyles.container}>
                    <QuizTopBar topic={topic} difficulty={difficulty} points={points} handleHomePress={handleHomePress} />
                    <View style={styles.quizContainer}>
                        {loading ? (
                            <View style={{ marginVertical: 20, alignItems: 'center' }}>
                                <ActivityIndicator size="large" color="#0000ff" />
                                {isSlow && (
                                    <OperationStatus operationSucceeded={operationSucceeded}/>
                                )}
                            </View>
                        ) : gameBeaten ? (
                            <View>
                                <Modal
                                    animationType={ platform === 'web' ? "fade": "slide" }
                                    transparent={true}
                                    visible={gameBeaten}
                                    onRequestClose={handleConfirm}
                                >
                                    <View style={ModalStyles.modalContainer}>
                                        <View style={ModalStyles.modalView}>
                                            <Text style={ModalStyles.modalText}>Herzlichen Glückwunsch, {playerName}!</Text>
                                            <Text style={ModalStyles.modalText}>Du hast das Quiz erfolgreich beendet.</Text>
                                            <Text style={ModalStyles.modalText}>Dein Endstand beträgt {score} Punkte.</Text>
                                            <TouchableOpacity style={buttonStyles.button} onPress={handleConfirm}>
                                                <Text style={buttonStyles.buttonText}>Quiz beenden</Text>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                </Modal>
                            </View>
                        ) : (
                            <>
                                <Modal
                                    animationType={ platform === 'web' ? "fade": "slide" }
                                    transparent={true}
                                    visible={showMoreInformation}
                                    onRequestClose={() => setShowMoreInformation(false)}
                                >
                                    <View style={ModalStyles.modalContainer}>
                                        <View style={ModalStyles.modalView}>
                                            <View style={{ borderColor: "black" }}>
                                                <Text style={ModalStyles.modalText}>{info}</Text>
                                                <View style={ButtonStyles.buttonContainer}>
                                                    <TouchableOpacity style={ButtonStyles.button} onPress={() => setShowMoreInformation(false)}>
                                                        <Text style={ButtonStyles.buttonText}>Schliessen</Text>
                                                    </TouchableOpacity>
                                                </View>
                                            </View>
                                        </View>
                                    </View>
                                </Modal>
                                <Modal
                                    animationType={ platform === 'web' ? "fade": "slide" }
                                    transparent={true}
                                    visible={modalVisible}
                                    onRequestClose={handleCancel}
                                >
                                    <View style={ModalStyles.modalContainer}>
                                        <View style={ModalStyles.modalView}>
                                            <Text style={ModalStyles.modalText}>Zurück zum Hauptmenü?</Text>
                                            <View style={ButtonStyles.buttonContainer}>
                                                <TouchableOpacity style={ButtonStyles.button} onPress={handleConfirm}>
                                                    <Text style={ButtonStyles.buttonText}>Ja</Text>
                                                </TouchableOpacity>
                                                <TouchableOpacity style={ButtonStyles.button} onPress={handleCancel}>
                                                    <Text style={ButtonStyles.buttonText}>Nein</Text>
                                                </TouchableOpacity>
                                            </View>
                                        </View>
                                    </View>
                                </Modal>
                                <View style={[SiteStyles.row, styles.questionHeader]}>
                                    <View style={styles.leftSection}>
                                        <Text style={[TextStyles.normalText, TextStyles.boldText, textStyles.leftText]}>
                                            Frage {questionCount}
                                        </Text>
                                        { platform === 'web' ? null : <Text style={[TextStyles.normalText, TextStyles.boldText, textStyles.leftText]}>
                                            Punkte: {points}
                                        </Text> }
                                    </View>
                                    <View style={[styles.centeredElement, SiteStyles.column]}>
                                        <MaterialIcons name="access-alarm" size={30} color={!gameOver ? "black" : "red"} />
                                        <Text
                                            style={[
                                                { paddingTop: 10 },
                                                TextStyles.normalText,
                                                TextStyles.boldText,
                                                TextStyles.monoText,
                                                {
                                                    color:
                                                        seconds <=
                                                        (difficulty === 'EASY'
                                                            ? 20
                                                            : difficulty === 'MEDIUM'
                                                                ? 15
                                                                : difficulty === 'HARD'
                                                                    ? 10
                                                                    : 40)
                                                            ? 'red'
                                                            : seconds <=
                                                            (difficulty === 'EASY'
                                                                ? 40
                                                                : difficulty === 'MEDIUM'
                                                                    ? 30
                                                                    : difficulty === 'HARD'
                                                                        ? 20
                                                                        : 80)
                                                                ? 'orange'
                                                                : 'black',
                                                },
                                            ]}
                                        >
                                            { !gameOver ? formatTime(seconds) : "Game Over"}
                                        </Text>
                                    </View>
                                    <View style={[styles.rightSection]}>
                                        { platform === 'web' ? <Text style={TextStyles.jokerText}>{countJoker}x</Text> : null }
                                        <TouchableOpacity
                                            style={[
                                                ButtonStyles.button,
                                                ButtonStyles.jokerButton,
                                                isJokerButtonDisabled && ButtonStyles.disabledButton,
                                            ]}
                                            onPress={() => useJoker()}
                                            disabled={isJokerButtonDisabled}
                                        >
                                            <Text style={ButtonStyles.buttonText}>
                                                { platform === 'web' ? "50:50" : `${countJoker}`}
                                            </Text>
                                        </TouchableOpacity>
                                    </View>
                                </View>
                                <View style={[platform === 'web' ? SiteStyles.row : SiteStyles.column]}>
                                    <View style={[styles.questionBox]}>
                                        <Text style={TextStyles.normalText}>{question}</Text>
                                    </View>
                                    <TouchableOpacity
                                        style={[IconStyles.questionIcon, platform === 'web' ? null : {alignSelf: "flex-end"}]}
                                        onPress={() => setShowMoreInformation(true)}
                                        disabled={!(gameOver || !isInfoButtonDisabled)}
                                    >
                                        <MaterialCommunityIcons
                                            name="help-circle"
                                            size={30}
                                            color={!(gameOver || !isInfoButtonDisabled) ? "grey" : "#6C8EBF"}
                                        />
                                    </TouchableOpacity>

                                </View>
                                <View style={[platform === 'web' ? SiteStyles.row : SiteStyles.column]}>
                                    <View style={styles.answerBox}>
                                        <QuizButton
                                            answer={answerOne}
                                            isSelected={isAnswerOneSelected}
                                            isShown={isAnswerOneButtonShown}
                                            isCorrect={answerOneId === correctAnswerId}
                                            isWrong={selectedAnswer === 1 && correctAnswerId !== answerOneId }
                                            isDisabled={allAnswerButtonsDisabled}
                                            onPress={() => selectAnswer(1)}
                                        />
                                        <QuizButton
                                            answer={answerTwo}
                                            isSelected={isAnswerTwoSelected}
                                            isShown={isAnswerTwoButtonShown}
                                            isCorrect={answerTwoId === correctAnswerId}
                                            isWrong={selectedAnswer === 2 && correctAnswerId !== answerTwoId }
                                            isDisabled={allAnswerButtonsDisabled}
                                            onPress={() => selectAnswer(2)}
                                        />
                                    </View>
                                    <View style={styles.answerBox}>
                                        <QuizButton
                                            answer={answerThree}
                                            isSelected={isAnswerThreeSelected}
                                            isShown={isAnswerThreeButtonShown}
                                            isCorrect={answerThreeId === correctAnswerId}
                                            isWrong={selectedAnswer === 3 && correctAnswerId !== answerThreeId }
                                            isDisabled={allAnswerButtonsDisabled}
                                            onPress={() => selectAnswer(3)}
                                        />
                                        <QuizButton
                                            answer={answerFour}
                                            isSelected={isAnswerFourSelected}
                                            isShown={isAnswerFourButtonShown}
                                            isCorrect={answerFourId === correctAnswerId}
                                            isWrong={selectedAnswer === 4 && correctAnswerId !== answerFourId }
                                            isDisabled={allAnswerButtonsDisabled}
                                            onPress={() => selectAnswer(4)}
                                        />
                                    </View>
                                </View>
                                <TouchableOpacity
                                    style={[
                                        ButtonStyles.button,
                                        ButtonStyles.quizSubmitButton,
                                        allAnswerButtonsDisabled ? ButtonStyles.disabledButton : null,
                                        gameOver ? ButtonStyles.hideButton : null, // Hide if game is over
                                        showSubmitButton ? ButtonStyles.hideButton : null, // Hide if "Next Question" button is shown
                                        isAnswerOneSelected ||
                                        isAnswerTwoSelected ||
                                        isAnswerThreeSelected ||
                                        isAnswerFourSelected
                                            ? null
                                            : ButtonStyles.disabledButton, // Disable when no answer is selected
                                    ]}
                                    onPress={() => answer()}
                                    disabled={
                                        allAnswerButtonsDisabled ||
                                        gameOver ||
                                        showSubmitButton || // Disable when "Next Question" button is shown
                                        !(isAnswerOneSelected || isAnswerTwoSelected || isAnswerThreeSelected || isAnswerFourSelected) // Disable if no answer selected
                                    }
                                >
                                    <Text style={[ButtonStyles.buttonText]}>Bestätigen</Text>
                                </TouchableOpacity>

                                <TouchableOpacity
                                    style={[
                                        ButtonStyles.button,
                                        ButtonStyles.quizSubmitButton,
                                        !showSubmitButton && !gameOver ? ButtonStyles.hideButton : null,
                                    ]}
                                    onPress={() => {
                                        if (gameOver) {
                                            endQuiz(); // End the quiz if game over
                                        } else {
                                            setShowSubmitButton(false); // Hide "Next Question" button after clicking
                                            loadQuestion(); // Load the next question
                                        }
                                    }}
                                >
                                    <Text style={ButtonStyles.buttonText}>
                                        { gameOver ? "Quiz beenden" : "Nächste Frage"}
                                    </Text>
                                </TouchableOpacity>
                            </>
                        )}
                    </View>
                </View>
            </ScrollView>
        </ErrorBoundary>
    );
}

const styles = StyleSheet.create({
    quizContainer: {
        flex: 1,
        flexDirection: "column",
        width: "80%",
        padding: 20,
    },
    questionHeader: {
        marginBottom: 20,
    },
    flexShrink: {
        flexShrink: 1,
    },
    leftSection: {
        flex: 1, // This ensures it takes up as much space as the right section
        alignItems: 'flex-start',
        marginLeft: 10,
    },
    rightSection: {
        flex: 1, // This ensures it takes up as much space as the left section
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginRight: 10,
    },
    centeredElement: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    questionBox: {
        flex: 1,
        borderWidth: 1,
        borderColor: "#6C8EBF",
        borderRadius: 10,
        margin: 10,
        padding: 10,
    },
    answerBox: {
        flex: 1,
        flexDirection: "column",
        justifyContent: "space-between",
    },
});
