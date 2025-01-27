import log from '@/utils/logger';

/**
 * Handles the success of an operation by setting the operation succeeded state and resetting it after a delay.
 *
 * @param {function(boolean): void} setOperationSucceeded - Function to set the operation succeeded state.
 * @param {function(boolean): void} setIsSlow - Function to set the is slow state.
 */
export const handleOperationSuccess = (
    setOperationSucceeded: (value: boolean) => void,
    setIsSlow: (value: boolean) => void
) => {
    log.debug('Operation succeeded, displaying success message...');
    setOperationSucceeded(true);
    setTimeout(() => {
        setOperationSucceeded(false);
        setIsSlow(false);
        log.debug('Continue...');
    }, 2000); // Wait 2 seconds to show the success message
};