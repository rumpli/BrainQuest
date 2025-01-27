import React, { useState, useEffect } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    ActivityIndicator,
    ScrollView,
    Modal,
    Button
} from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { router } from "expo-router";
import RNModal from 'react-native-modal';

// Components
import ErrorBoundary from '@/components/ErrorBoundary';
import SmallTopBar from '@/components/SmallTopBar';
import ErrorView from "@/components/ErrorView";
import HighscoreTable from "@/components/HighscoreTable";
import OperationStatus from "@/components/OperationStatus";

// Utils
import { handleOperationSuccess } from '@/utils/handleOperationSuccess';
import { handleFetchHighscores } from '@/utils/handleFetchHighscores';
import { handleFetchTopics } from "@/utils/handleFetchTopics";
import log from '@/utils/logger';

// Styles
import ButtonStyles from '@/styles/ButtonStyles';
import TextStyles from '@/styles/TextStyles';
import SiteStyles from '@/styles/SiteStyles';
import { usePickerStyles } from "@/styles/PickerStyles";

// Hooks
import { usePlatformStyles } from "@/hooks/usePlatformStyles";

export default function Index() {
    const PickerStyles = usePickerStyles();
    const { platform } = usePlatformStyles();
    const [loading, setLoading] = useState(false);
    const [loadingTable, setLoadingTable] = useState(false);
    const [isSlow, setIsSlow] = useState(false);
    const [highscores, setHighscores] = useState<any[]>([]);
    const [operationSucceeded, setOperationSucceeded] = useState(false);
    const [topics, setTopics] = useState<string[]>([]);
    const [difficulties, setDifficulties] = useState<string[]>([]);
    const [error, setError] = useState<Error | null>(null);
    const [selectedDifficulty, setSelectedDifficulty] = useState<string>('');
    const [selectedTopic, setSelectedTopic] = useState<string | null>(null);
    const [isTopicModalVisible, setIsTopicModalVisible] = useState(false);
    const [isDifficultyModalVisible, setIsDifficultyModalVisible] = useState(false);
    const isButtonDisabled = !selectedTopic || !selectedDifficulty;
    const selectedTopicId = selectedTopic ? topics.find(t => t.name === selectedTopic)?.id : null;

    const loadTopics = () => {
        setTopics([]);
        setIsSlow(false);
        setOperationSucceeded(false);
        handleFetchTopics(setTopics, setLoading, setError, setIsSlow);
    };

    const getHighscores = () => {
        setHighscores([]);
        setIsSlow(false);
        setOperationSucceeded(false);
        handleFetchHighscores(setHighscores, setLoadingTable, setError, setIsSlow, selectedTopicId, selectedDifficulty, 'SCORE', 'DESC');
    }

    const handleTopicChange = (itemValue: string) => {
        setSelectedTopic(itemValue);
        const topic = topics.find(t => t.name === itemValue);
        const newDifficulties = topic ? topic.difficulty : [];
        setDifficulties(newDifficulties);
        if (!newDifficulties.includes(selectedDifficulty)) {
            setSelectedDifficulty('');
        }
        setIsTopicModalVisible(false);
    };

    useEffect(() => {
        loadTopics();
    }, []);

    useEffect(() => {
        if ((isSlow && topics.length > 0 && highscores.length < 0) || (isSlow && highscores.length > 0)) {
            log.debug('API call succeeded after a delay.');
            handleOperationSuccess(setOperationSucceeded, setIsSlow);
        }
    }, [isSlow, topics, highscores]);

    if (error) {
        const errorMessage = `Error: ${error.message}`;
        return <ErrorView errorMessage={errorMessage} onPress={() => router.push('/')} />;
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
                        ) : (
                            <View style={platform === 'web' ? SiteStyles.row : SiteStyles.column}>
                                <View style={PickerStyles.pickSelection}>
                                    <Text style={TextStyles.normalText}>Themenbereich</Text>
                                    {platform === 'ios' ? (
                                        <>
                                            <TouchableOpacity onPress={() => setIsTopicModalVisible(true)}>
                                                <Text style={PickerStyles.iosPicker.inputIOS}>
                                                    {selectedTopic || 'Select a topic'}
                                                </Text>
                                            </TouchableOpacity>
                                            <RNModal isVisible={isTopicModalVisible} propagateSwipe={true}>
                                                <View style={PickerStyles.iosPicker.inputIOSContainer}>
                                                    <Picker
                                                        selectedValue={selectedTopic || ''}
                                                        onValueChange={(itemValue) => handleTopicChange(itemValue)}
                                                    >
                                                        <Picker.Item label="-" value="" />
                                                        {topics.map((topic, index) => (
                                                            <Picker.Item key={index} label={topic.name} value={topic.name} />
                                                        ))}
                                                    </Picker>
                                                    <Button title="Done" onPress={() => setIsTopicModalVisible(false)} />
                                                </View>
                                            </RNModal>
                                        </>
                                    ) : (
                                        <Picker
                                            selectedValue={selectedTopic || ''}
                                            style={platform === 'android' ? PickerStyles.androidPicker : PickerStyles.webPicker}
                                            onValueChange={(itemValue) => handleTopicChange(itemValue)}
                                        >
                                            <Picker.Item label="-" value="" />
                                            {topics.map((topic, index) => (
                                                <Picker.Item key={index} label={topic.name} value={topic.name} />
                                            ))}
                                        </Picker>
                                    )}
                                </View>
                                <View style={PickerStyles.pickSelection}>
                                    <Text style={TextStyles.normalText}>Schwierigkeit</Text>
                                    {platform === 'ios' ? (
                                        <>
                                            <TouchableOpacity onPress={() => setIsDifficultyModalVisible(true)}>
                                                <Text style={PickerStyles.iosPicker.inputIOS}>
                                                    {selectedDifficulty || 'Select a difficulty'}
                                                </Text>
                                            </TouchableOpacity>
                                            <RNModal isVisible={isDifficultyModalVisible} propagateSwipe={true}>
                                                <View style={PickerStyles.iosPicker.inputIOSContainer}>
                                                    <Picker
                                                        selectedValue={selectedDifficulty || ''}
                                                        onValueChange={(itemValue) => {
                                                            setSelectedDifficulty(itemValue);
                                                            setIsDifficultyModalVisible(false);
                                                        }}
                                                    >
                                                        <Picker.Item label="-" value="" />
                                                        {difficulties.map((difficulty, index) => (
                                                            <Picker.Item key={index} label={difficulty} value={difficulty} />
                                                        ))}
                                                    </Picker>
                                                    <Button title="Done" onPress={() => setIsDifficultyModalVisible(false)} />
                                                </View>
                                            </RNModal>
                                        </>
                                    ) : (
                                        <Picker
                                            selectedValue={selectedDifficulty || ''}
                                            style={platform === 'android' ? PickerStyles.androidPicker : PickerStyles.webPicker}
                                            onValueChange={(itemValue) => setSelectedDifficulty(itemValue)}
                                        >
                                            <Picker.Item label="-" value="" />
                                            {difficulties.map((difficulty, index) => (
                                                <Picker.Item key={index} label={difficulty} value={difficulty} />
                                            ))}
                                        </Picker>
                                    )}
                                </View>
                                <TouchableOpacity
                                    style={[ButtonStyles.button, isButtonDisabled && ButtonStyles.disabledButton]}
                                    onPress={() => getHighscores()}
                                    disabled={isButtonDisabled}
                                >
                                    <Text style={ButtonStyles.buttonText}>Anzeigen</Text>
                                </TouchableOpacity>
                            </View>
                        )}
                        { loadingTable || (operationSucceeded && !loading) ? (
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
                </View>
            </ScrollView>
        </ErrorBoundary>
    );
}