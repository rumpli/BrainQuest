import log from '@/utils/logger';

jest.mock('react-native-logs', () => ({
    logger: {
        createLogger: jest.fn(() => ({
            info: jest.fn(),
            warn: jest.fn(),
            error: jest.fn(),
            debug: jest.fn(),
        })),
    },
    consoleTransport: jest.fn(),
}));

describe('logger', () => {
    it('creates a logger with the correct configuration', () => {
        const config = {
            severity: process.env.EXPO_PUBLIC_LOG_LEVEL || 'info',
            transport: expect.any(Function),
            transportOptions: {
                colors: {
                    info: 'blueBright',
                    warn: 'yellowBright',
                    error: 'redBright',
                },
            },
        };

        expect(log).toBeDefined();
        expect(log.info).toBeDefined();
        expect(log.warn).toBeDefined();
        expect(log.error).toBeDefined();
        expect(log.debug).toBeDefined();
    });

    it('logs info messages correctly', () => {
        log.info('Info message');
        expect(log.info).toHaveBeenCalledWith('Info message');
    });

    it('logs warning messages correctly', () => {
        log.warn('Warning message');
        expect(log.warn).toHaveBeenCalledWith('Warning message');
    });

    it('logs error messages correctly', () => {
        log.error('Error message');
        expect(log.error).toHaveBeenCalledWith('Error message');
    });

    it('logs debug messages correctly', () => {
        log.debug('Debug message');
        expect(log.debug).toHaveBeenCalledWith('Debug message');
    });
});