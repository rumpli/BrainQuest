import log from '@/utils/logger';
import { highscores } from '@/utils/api';

/**
 * Utility function to fetch highscores from the API.
 *
 * @param {number} topicId - The ID of the topic.
 * @param {string} difficultyLevel - The difficulty level.
 * @param {string} sortBy - The field to sort by.
 * @param {string} sortDir - The direction to sort (asc/desc).
 * @returns {Promise<any[]>} A promise that resolves with an array of highscores.
 * @throws {Error} Throws an error if fetching highscores fails.
 */
export const fetchHighscores = async (topicId: number, difficultyLevel: string, sortBy: string, sortDir: string): Promise<any[]> => {
    try {
        log.info("Fetching highscores from API...");
        const data = await highscores(topicId, difficultyLevel, sortBy, sortDir); // Pass parameters to highscores function
        log.debug(`Fetched highscores: ${JSON.stringify(data)}`);
        return data; // Return the data to the caller
    } catch (err) {
        log.error("Error fetching highscores:", err);
        throw new Error("Failed to load highscores."); // Throw an error to be caught by the caller
    }
};