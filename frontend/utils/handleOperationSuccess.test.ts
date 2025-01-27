import { handleOperationSuccess } from '@/utils/handleOperationSuccess';
import log from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    debug: jest.fn(),
}));

describe('handleOperationSuccess', () => {
    const setOperationSucceeded = jest.fn();
    const setIsSlow = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
        jest.useRealTimers();
    });

    it('sets operation succeeded state to true and then false after delay', () => {
        jest.useFakeTimers();

        handleOperationSuccess(setOperationSucceeded, setIsSlow);

        expect(setOperationSucceeded).toHaveBeenCalledWith(true);
        jest.advanceTimersByTime(2000);
        expect(setOperationSucceeded).toHaveBeenCalledWith(false);
    });

    it('sets isSlow state to false after delay', () => {
        jest.useFakeTimers();

        handleOperationSuccess(setOperationSucceeded, setIsSlow);

        jest.advanceTimersByTime(2000);
        expect(setIsSlow).toHaveBeenCalledWith(false);
    });

    it('logs debug messages', () => {
        jest.useFakeTimers();

        handleOperationSuccess(setOperationSucceeded, setIsSlow);

        expect(log.debug).toHaveBeenCalledWith('Operation succeeded, displaying success message...');
        jest.advanceTimersByTime(2000);
        expect(log.debug).toHaveBeenCalledWith('Continue...');
    });
});