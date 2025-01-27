// Text styles for the app
import { Platform, StyleSheet } from "react-native";

// Styles
const TextStyles = StyleSheet.create({
    title: {
        fontSize: 24,
        fontWeight: "bold",
    },
    normalText: {
        fontSize: 16,
        fontWeight: "normal",
        alignSelf: "center",
    },
    monoText: {
        fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace'
    },
    leftText: {
        alignSelf: "flex-start",
    },
    rightText: {
        alignSelf: "flex-end",
    },
    boldText: {
        fontWeight: "bold",
    },
    smallTopBarTitle: {
        flex: 1,
        alignSelf: "flex-start",
        textAlign: "right",
        fontSize: 24,
        fontWeight: "bold",
    },
    nameText: {
        color: "darkblue",
        fontStyle: "italic",
    },
    errorText: {
        color: "red",
        fontWeight: "bold"
    },
    jokerText: {
        fontWeight: "bold",
        fontSize: 18,
    }
});

export default TextStyles;