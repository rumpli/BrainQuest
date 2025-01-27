import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';

// Icons
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';

// Styles
import IconStyles from '@/styles/IconStyles';
import SiteStyles from "@/styles/SiteStyles";

interface OperationStatusProps {
    operationSucceeded: boolean;
}

/**
 * OperationStatus component that displays the status of an operation.
 *
 * @param {OperationStatusProps} props - The props for the component.
 * @param {boolean} props.operationSucceeded - Indicates whether the operation succeeded.
 * @returns {JSX.Element} The rendered OperationStatus component.
 */
const OperationStatus: React.FC<OperationStatusProps> = ({ operationSucceeded }) => {
    return (
        <View style={SiteStyles.column}>
            {operationSucceeded ? (
                <>
                    <TouchableOpacity style={IconStyles.kettleIcon}>
                        <MaterialCommunityIcons name="kettle-steam" size={30} color="black" />
                    </TouchableOpacity>
                    <Text style={{ marginTop: 10, color: 'green' }}>
                        Erfolgreich - du wirst gleich weitergeleitet!
                    </Text>
                </>
            ) : (
                <>
                    <TouchableOpacity style={IconStyles.kettleIcon}>
                        <MaterialCommunityIcons name="kettle-alert" size={30} color="black" />
                    </TouchableOpacity>
                    <Text style={{ marginTop: 10, color: 'orange' }}>
                        Dies dauert länger als gewöhnlich...
                    </Text>
                </>
            )}
        </View>
    );
};

export default OperationStatus;