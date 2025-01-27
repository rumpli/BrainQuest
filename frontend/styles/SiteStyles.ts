import { StyleSheet } from 'react-native';

const SiteStyles = StyleSheet.create({
    fullSite: {
        width: '100%',
        height: '100%',
        backgroundColor: "#fff"
    },
    container: {
        flex: 1,
        width: "100%",
        height: "100%",
        maxWidth: 1000,
        alignSelf: "center",
        alignItems: "center",
        justifyContent: "center",
        paddingTop: 20,
        paddingBottom: 20,
    },
    highscoreContainer: {
        flex: 1,
        flexDirection: "column",
        width: "80%",
    },
    row: {
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "space-between",
    },
    column: {
        flexDirection: "column",
    },
});

export default SiteStyles;