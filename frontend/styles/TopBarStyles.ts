import {Dimensions, Platform, StyleSheet} from 'react-native';

const TopBarStyles = StyleSheet.create({
    topBar: {
        flexDirection: "row",
        justifyContent: "center",
        alignItems: "center",
        width: '80%',
        maxWidth: 500,
        paddingHorizontal: 20,
        marginBottom: 50,
        marginTop: Platform.OS === 'web' ? '5%' : '20%',
        position: 'relative',
    },
    smallTopBar: {
        flexShrink: 1,
        marginTop: Platform.OS === 'web' ? '2%' : '15%',
        width: "80%",
        flexDirection: "row",
        justifyContent: "space-around",
        padding: 30,
        paddingBottom: 30,
    },
    logoContainer: {
        flex: 1,
        alignItems: 'center',
    },
    leftSection: {
        flex: 1,
        alignItems: 'flex-start',
        marginLeft: 10,
    },
    rightSection: {
        flex: 1,
        alignItems: 'flex-end',
        marginRight: 10,
    },
    homeIcon: {
        flex: 1,
        alignSelf: 'flex-start'
    },
});

export default TopBarStyles;