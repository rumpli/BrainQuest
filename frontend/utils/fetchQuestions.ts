import log from '@/utils/logger';
import { questions } from '@/utils/api';

/**
 * Utility function to fetch questions from the API.
 *
 * @param {number} topicId - The ID of the topic to fetch questions for.
 * @param {string} difficultyLevel - The difficulty level of the questions to fetch.
 * @param {number[]} excludeId - An array of question IDs to exclude from the results.
 * @param playerName - The player name.
 * @param score - The player score.
 * @returns {Promise<any[]>} A promise that resolves with an array of questions.
 * @throws {Error} Throws an error if fetching questions fails.
 */
export const fetchQuestions = async (
    topicId: number,
    difficultyLevel: string,
    excludeId: number[],
    playerName: string,
    score: number,
): Promise<any[]> => {
    try {
        // Log the start of the API fetch process
        log.info("Fetching quiz question from API...");
        log.debug(`Topic: ${topicId}`, `Difficulty: ${difficultyLevel}`, `Excluded IDs: ${excludeId}`);

        // Call the questions API function with the provided parameters
        const data = await questions(topicId, difficultyLevel, excludeId, playerName, score)

        if (!data || (typeof data === 'object' && Object.keys(data).length === 0 && data.constructor === Object) || (Array.isArray(data) && data.length === 0)) {
            // Log a message if no questions were fetched or data is an empty object
            log.info("No valid questions found.");
            return [];
        }

        // Log the fetched questions
        // Since 'data' is a single object, access 'data.question' directly
        log.debug(`Fetched question: ${data?.question}`);

        // Return the fetched data
        return data;
    } catch (err) {
        // Log any errors that occur during the fetch process
        log.error("Error fetching questions:", err);

        // Throw an error to be caught by the caller
        throw new Error("Failed to load questions.");
    }
};