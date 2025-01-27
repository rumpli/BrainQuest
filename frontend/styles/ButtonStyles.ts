import { StyleSheet, Dimensions } from 'react-native';

const { height } = Dimensions.get('window');

const ButtonStyles = StyleSheet.create({
    buttonContainer: {
        flexDirection: "row", // Arrange buttons horizontally
        justifyContent: "space-between",
        paddingTop: height * 0.05, // Add some space above the buttons
        width: '80%', // Set the container width
        maxWidth: 500, // Limit the container width to 700
    },
    button: {
        boxShadow: "0 2px 3px rgba(0, 0, 0, 0.2)", // Box shadow
        backgroundColor: "#6C8EBF", // Button background color
        paddingVertical: 10, // Vertical padding
        paddingHorizontal: 20, // Horizontal padding
        borderRadius: 10, // Rounded corners
        elevation: 3, // Shadow on Android
    },
    buttonText: {
        color: "#fff", // White text color
        fontSize: 16, // Font size
        fontWeight: "600", // Semi-bold font weight
        textAlign: "center", // Center text
    },
    disabledButton: {
        boxShadow: "0 2px 3px rgba(0, 0, 0, 0.2)", // Box shadow
        backgroundColor: "grey", // Button background color
        paddingVertical: 10, // Vertical padding
        paddingHorizontal: 20, // Horizontal padding
        borderRadius: 10, // Rounded corners
        elevation: 3, // Shadow on Android
    },
    highlightButton: {
        backgroundColor: "lightblue", // Button background color
    },
    quizButton: {
        backgroundColor: "#8797AD",
        margin: 10,
    },
    jokerButton: {
        backgroundColor: "#8DA7CC", // Button background color
        paddingVertical: 10, // Vertical padding
        paddingHorizontal: 20, // Horizontal padding
        borderRadius: 5, // Rounded corners
        marginLeft: 10, // Add some space to the left
    },
    quizSubmitButton: {
        margin: 10,
    },
    hideButton: {
        opacity: 0,
        pointerEvents: "none",
    },
    correctAnswerButton: {
        backgroundColor: "green",
    },
    wrongAnswerButton: {
        backgroundColor: "red",
    },
});

export default ButtonStyles;