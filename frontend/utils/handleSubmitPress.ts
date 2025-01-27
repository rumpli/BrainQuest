import log from '@/utils/logger';

/**
 * Handles the logic for when the user presses the submit button.
 *
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setNameSubmitted - Function to set the name submitted state.
 * @param {string} name - The name that is being submitted.
 */
export const handleSubmitPress = (
    setNameSubmitted: React.Dispatch<React.SetStateAction<boolean>>,
    name: string
) => {
    setNameSubmitted(true);
    log.debug(`Name submitted: ${name}`);
};