import log from '@/utils/logger';
import { fetchTopics } from '@/utils/fetchTopics';
import React from "react";

/**
 * Handles the logic for fetching topics from the API.
 *
 * @param {React.Dispatch<React.SetStateAction<any[]>>} setTopics - Sets the topics state with the fetched topics.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setLoading - Sets the loading state to true while the API request is in progress.
 * @param {React.Dispatch<React.SetStateAction<Error | null>>} setError - Sets any error that occurs during the fetch process.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setIsSlow - Sets the state to indicate if the request is taking too long.
 * @returns {Promise<void>} A promise that resolves when the fetch operation is complete.
 */
export const handleFetchTopics = async (
    setTopics: React.Dispatch<React.SetStateAction<any[]>>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setError: React.Dispatch<React.SetStateAction<Error | null>>,
    setIsSlow: React.Dispatch<React.SetStateAction<boolean>> // Set the state for slow API response
): Promise<void> => {
    setLoading(true); // Show the loading indicator
    setIsSlow(false); // Reset "slow" state to false before the request
    log.info("Fetching topics in the background...");

    // Set timeout for detecting slow API
    const timeoutId = setTimeout(() => {
        setIsSlow(true); // Set the "slow" state to true after 3 seconds
    }, 3000); // 3 seconds timeout

    try {
        const topics = await fetchTopics(); // Await the result from the utility function
        setTopics(topics); // Set topics in state
    } catch (error) {
        log.error(`Error fetching topics: ${error}`);
        setError(error instanceof Error ? error : new Error(String(error))); // Set error state
    } finally {
        setLoading(false); // Hide the loading indicator after API call finishes
        clearTimeout(timeoutId); // Clear the timeout if the request finished before 3 seconds
    }
};