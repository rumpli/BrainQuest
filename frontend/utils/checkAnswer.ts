import log from '@/utils/logger';
import { checkCorrectAnswer } from '@/utils/api';

/**
 * Utility function to check the answer for a given question.
 *
 * @param {number} questionId - The ID of the question to check the answer for.
 * @param {number} answerId - The ID of the answer provided by the player.
 * @param {string} playerName - The name of the player providing the answer.
 * @param {number} score - The score of the player.
 * @returns {Promise<any[]>} A promise that resolves with an array of results indicating if the answer was correct.
 */
export const checkAnswer = async (
    questionId: number,
    answerId: number,
    playerName: string,
    score: number,
): Promise<any[]> => {
    try {
        // Log the start of the API fetch process
        log.info("Check the answer...");
        log.debug(`Answer ID: ${answerId}`, `Player Name: ${playerName}`, `Score: ${score}`);

        // Create the request body with the provided parameters
        const requestBody = {
            answerId: answerId,
            playerName: playerName,
            score: score,
        };

        // Call the check answer API function with the provided parameters
        return await checkCorrectAnswer(questionId, requestBody);
    } catch (err) {
        // Log any errors that occur during the fetch process
        log.error("Error checking answer:", err);

        // Throw an error to be caught by the caller
        throw new Error("Failed to check questions.");
    }
};