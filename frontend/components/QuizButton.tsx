import React from 'react';
import { TouchableOpacity, Text } from 'react-native';

// Styles
import ButtonStyles from '@/styles/ButtonStyles';

interface QuizButtonProps {
    answer: string;
    isSelected: boolean;
    isShown: boolean;
    isCorrect: boolean;
    isWrong: boolean;
    isDisabled: boolean;
    onPress: () => void;
}

/**
 * QuizButton component that displays a button for quiz answers.
 *
 * @param {QuizButtonProps} props - The props for the component.
 * @param {string} props.answer - The answer text to display on the button.
 * @param {boolean} props.isSelected - Whether the button is selected.
 * @param {boolean} props.isShown - Whether the button is shown.
 * @param {boolean} props.isCorrect - Whether the answer is correct.
 * @param {boolean} props.isWrong - Whether the answer is wrong.
 * @param {boolean} props.isDisabled - Whether the button is disabled.
 * @param {() => void} props.onPress - Function to handle the button press.
 * @returns {JSX.Element} The rendered QuizButton component.
 */
const QuizButton: React.FC<QuizButtonProps> = ({
                                                   answer,
                                                   isSelected,
                                                   isShown,
                                                   isCorrect,
                                                   isWrong,
                                                   isDisabled,
                                                   onPress
                                               }) => {
    return (
        <TouchableOpacity
            style={[
                ButtonStyles.button,
                ButtonStyles.quizButton,
                isDisabled ? ButtonStyles.disabledButton : null,
                isSelected && ButtonStyles.highlightButton,
                isShown ? null : ButtonStyles.hideButton,
                isCorrect ? ButtonStyles.correctAnswerButton : null,
                isWrong ? ButtonStyles.wrongAnswerButton : null,
            ]}
            onPress={onPress}
            disabled={isDisabled || !isShown}
        >
            <Text style={ButtonStyles.buttonText}>{answer}</Text>
        </TouchableOpacity>
    );
};

export default QuizButton;