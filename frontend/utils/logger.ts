import { logger, consoleTransport, configLoggerType, transportFunctionType } from 'react-native-logs';
import { ConsoleTransportOptions } from "react-native-logs/src/transports/consoleTransport";

const level = process.env.EXPO_PUBLIC_LOG_LEVEL || 'info';

/**
 * Configuration object for the logger.
 *
 * @type {configLoggerType<transportFunctionType<ConsoleTransportOptions>, "info" | "error" | "warn" | "debug">}
 */
const config: configLoggerType<transportFunctionType<ConsoleTransportOptions>, "info" | "error" | "warn" | "debug"> = {
    severity: level,
    transport: consoleTransport,
    transportOptions: {
        colors: {
            info: 'blueBright',
            warn: 'yellowBright',
            error: 'redBright',
        },
    },
};

/**
 * Logger instance configured with the specified settings.
 *
 * @type {logger}
 */
const log = logger.createLogger(config);

export default log;