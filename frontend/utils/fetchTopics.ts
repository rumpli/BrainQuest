import log from '@/utils/logger';
import { topics } from '@/utils/api';

/**
 * Utility function to fetch topics from the API.
 *
 * @returns {Promise<any[]>} A promise that resolves with an array of topics.
 * @throws {Error} Throws an error if fetching topics fails.
 */
export const fetchTopics = async (): Promise<any[]> => {
    try {
        // Log the start of the API fetch process
        log.info("Fetching topics from API...");

        // Call the topics API function
        const data = await topics();

        // Log the fetched topics
        log.debug(`Fetched topics: ${data.map((topic: { name: string }) => topic.name)}`);

        // Return the data to the caller
        return data;
    } catch (err) {
        // Log any errors that occur during the fetch process
        log.error("Error fetching topics:", err);

        // Throw an error to be caught by the caller
        throw new Error("Failed to load topics.");
    }
};