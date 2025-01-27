import React from 'react';
import { View, Text, ScrollView } from 'react-native';

// Styles
import TableStyles from '@/styles/TableStyles';

interface Highscore {
    playerName: string;
    topic: { name: string };
    difficulty: string;
    score: number;
}

interface HighscoreTableProps {
    highscores: Highscore[];
}

/**
 * HighscoreTable component that displays a table of highscores.
 *
 * @param {HighscoreTableProps} props - The props for the component.
 * @param {Highscore[]} props.highscores - An array of highscore objects.
 * @param {string} props.highscores[].playerName - The name of the player.
 * @param {Object} props.highscores[].topic - The topic of the quiz.
 * @param {string} props.highscores[].topic.name - The name of the topic.
 * @param {string} props.highscores[].difficulty - The difficulty level of the quiz.
 * @param {number} props.highscores[].score - The score achieved by the player.
 * @returns {JSX.Element} The rendered HighscoreTable component.
 */
const HighscoreTable: React.FC<HighscoreTableProps> = ({ highscores }) => {
    return (
        <View style={TableStyles.highscoreTable}>
            {highscores.length > 0 && (
                <ScrollView
                    style={TableStyles.tableScrollView}
                    contentContainerStyle={TableStyles.tableContentContainer}
                >
                    <View style={TableStyles.table}>
                        <View style={TableStyles.tableRow}>
                            <Text style={TableStyles.tableHeader}>Rang</Text>
                            <Text style={TableStyles.tableHeader}>Spielername</Text>
                            <Text style={TableStyles.tableHeader}>Themenbereich</Text>
                            <Text style={TableStyles.tableHeader}>Schwierigkeit</Text>
                            <Text style={[TableStyles.tableHeader, TableStyles.lastTableHeader]}>Punkte</Text>
                        </View>
                        {highscores.map((highscore, index) => (
                            <View key={index}
                                  style={[
                                      TableStyles.tableRow,
                                      index % 2 === 0 ? TableStyles.evenRow : TableStyles.oddRow
                                  ]}
                            >
                                <Text style={TableStyles.tableCell}>{index + 1}</Text>
                                <Text style={TableStyles.tableCell}>{highscore.playerName}</Text>
                                <Text style={TableStyles.tableCell}>{highscore.topic.name}</Text>
                                <Text style={TableStyles.tableCell}>{highscore.difficulty}</Text>
                                <Text style={[TableStyles.tableCell, TableStyles.lastTableCell]}>{highscore.score}</Text>
                            </View>
                        ))}
                    </View>
                </ScrollView>
            )}
        </View>
    );
};

export default HighscoreTable;