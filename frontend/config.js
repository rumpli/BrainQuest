import { Platform } from "react-native";
import Constants from 'expo-constants';

let config;

if (Platform.OS !== 'web') {
    config = {
        API_URL: Constants.expoConfig?.extra?.API_URL || 'default-api-url',
        LOG_LEVEL: Constants.expoConfig?.extra?.LOG_LEVEL || 'info',
    };
} else {
    config = {
        API_URL: 'default-api-url',
    };
}

export default config;