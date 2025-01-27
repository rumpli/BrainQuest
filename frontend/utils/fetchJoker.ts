import log from '@/utils/logger';
import { useJoker } from '@/utils/api';

/**
 * Utility function to fetch joker questions from the API.
 *
 * @param {number} questionsId - The ID of the question.
 * @param {string} jokerType - The type of joker to use.
 * @returns {Promise<any[]>} A promise that resolves with an array of questions.
 * @throws {Error} Throws an error if fetching joker questions fails.
 */
export const fetchJoker = async (
    questionsId: number,
    jokerType: string
): Promise<any[]> => {
    try {
        // Fetch the joker questions from the API
        const data = await useJoker(questionsId, jokerType);

        // Log the fetched questions
        // Since 'data' is a single object, access 'data.question' directly
        log.debug(`Fetched joker questions: ${data?.question}`);

        // Return the fetched data
        return data;
    } catch (err) {
        // Log any errors that occur during the fetch process
        log.error("Error fetching joker questions:", err);

        // Throw an error to be caught by the caller
        throw new Error("Failed to load joker questions.");
    }
};