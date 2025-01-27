import React from 'react';
import { View, TouchableOpacity, Text, Platform } from 'react-native';

// Components
import SmallMainLogo from '@/components/SmallMainLogo';

// Styles
import TextStyles from '@/styles/TextStyles';
import TopBarStyles from '@/styles/TopBarStyles';

// Icons
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

interface QuizTopBarProps {
    topic: string;
    difficulty: string;
    points: number;
    handleHomePress: () => void;
}

/**
 * QuizTopBar component that displays a top bar with home button, topic, difficulty, points, and logo.
 *
 * @param {QuizTopBarProps} props - The props for the component.
 * @param {string} props.topic - The topic of the quiz.
 * @param {string} props.difficulty - The difficulty level of the quiz.
 * @param {number} props.points - The current points scored in the quiz.
 * @param {() => void} props.handleHomePress - Function to handle the home button press.
 * @returns {JSX.Element} The rendered QuizTopBar component.
 */
export default function QuizTopBar({topic, difficulty, points, handleHomePress}: QuizTopBarProps) {
    return (
        <View style={TopBarStyles.smallTopBar}>
            <View style={TopBarStyles.leftSection}>
                <TouchableOpacity style={TopBarStyles.homeIcon} onPress={handleHomePress}>
                    <MaterialIcons name="home" size={30} color="black"/>
                </TouchableOpacity>
                <View>
                    <View style={{flexDirection: 'row', justifyContent: 'space-between'}}>
                        <Text style={[TextStyles.normalText, TextStyles.leftText]}>Kategorie: </Text>
                        <Text style={[TextStyles.normalText, TextStyles.boldText, TextStyles.leftText]}>{topic}</Text>
                    </View>
                    <View style={{flexDirection: 'row', justifyContent: 'space-between'}}>
                        <Text style={[TextStyles.normalText, TextStyles.leftText]}>Schwierigkeit: </Text>
                        <Text style={[TextStyles.normalText, TextStyles.boldText, TextStyles.leftText]}>{difficulty}</Text>
                    </View>
                </View>
            </View>
            { Platform.OS === 'web' ?
                <View style={[TopBarStyles.logoContainer]}>
                    <SmallMainLogo/>
                </View> : null }
            { Platform.OS === 'web' ?
            <View style={TopBarStyles.rightSection}>
                <Text style={[TextStyles.smallTopBarTitle, TextStyles.rightText]}>Brain Quest</Text>
                <Text style={[TextStyles.normalText, TextStyles.rightText]}>Punkte: {points}</Text>
            </View> : null }
        </View>
    );
}
