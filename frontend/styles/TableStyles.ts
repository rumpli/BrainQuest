import {Dimensions, StyleSheet} from 'react-native';

const TableStyles = StyleSheet.create({
    table: {
        borderWidth: 1,
        borderColor: 'gray',
    },
    tableRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        borderBottomWidth: 1,
        borderBottomColor: 'gray',
    },
    tableHeader: {
        flex: 1,
        fontWeight: 'bold',
        textAlign: 'center',
        borderRightWidth: 1,
        borderRightColor: 'darkgray',
        paddingVertical: 10,
        backgroundColor: 'grey',
    },
    lastTableHeader: {
        borderRightWidth: 0, // No border for the last header
    },
    tableCell: {
        flex: 1,
        textAlign: 'center',
        borderRightWidth: 1,
        borderRightColor: 'gray',
        paddingVertical: 10,
    },
    lastTableCell: {
        borderRightWidth: 0, // No border for the last cell
    },
    evenRow: {
        backgroundColor: 'lightgray',
    },
    oddRow: {
        backgroundColor: 'white',
    },
    highscoreTable: {
        marginTop: 20,
    },
    tableScrollView: {
        flexGrow: 0,
        height: Dimensions.get('window').height * 0.6,
    },
    tableContentContainer: {
        paddingBottom: 20,
    },
});

export default TableStyles;