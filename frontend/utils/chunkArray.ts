// Helper function to chunk the array into smaller arrays
import logger from "@/utils/logger";

/**
 * Chunks an array into smaller arrays of a specified size.
 *
 * @param {any[]} array - The array to be chunked.
 * @param {number} size - The size of each chunk.
 * @returns {any[][]} - An array of chunked arrays.
 */
export const chunkArray = (array: any[], size: number): any[][] => {
    const chunked = [];
    for (let i = 0; i < array.length; i += size) {
        chunked.push(array.slice(i, i + size));
    }
    for (let i = 0; i < chunked.length; i++) {
        logger.debug(`Chunk ${i}: ${JSON.stringify(chunked[i])}`);
    }
    return chunked;
};