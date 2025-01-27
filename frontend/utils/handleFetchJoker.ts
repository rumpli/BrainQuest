import log from '@/utils/logger';
import React from "react";
import { fetchJoker } from "@/utils/fetchJoker";

/**
 * Handles the logic for fetching joker answers from the API.
 *
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setLoading - Sets the loading state to true while the API request is in progress.
 * @param {React.Dispatch<React.SetStateAction<Error | null>>} setError - Sets any error that occurs during the fetch process.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setIsSlow - Sets the state to indicate if the request is taking too long.
 * @param {number} questionId - The current question's ID to fetch the joker answers for.
 * @param {string} jokerType - The type of joker being used (e.g., 'FIFTY_FIFTY').
 * @returns {Promise<any[]>} A promise that resolves with the fetched joker answers.
 */
export const handleFetchJoker = async (
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setError: React.Dispatch<React.SetStateAction<Error | null>>,
    setIsSlow: React.Dispatch<React.SetStateAction<boolean>>,
    questionId: number,
    jokerType: string
): Promise<any[] | undefined> => {
    setLoading(true); // Show the loading indicator
    setIsSlow(false); // Reset "slow" state to false before the request
    log.info("Fetching joker answers in the background...");

    // Set timeout for detecting slow API
    const timeoutId = setTimeout(() => {
        setIsSlow(true); // Set the "slow" state to true after 3 seconds
    }, 3000); // 3 seconds timeout

    try {
        const jokerAnswers = await fetchJoker(questionId, jokerType); // Await the result from the utility function

        if (!jokerAnswers || (Array.isArray(jokerAnswers) && jokerAnswers.length === 0)) {
            log.info('No more joker answers to fetch. Game successfully completed!');
            setLoading(false); // No need to load anymore
            return []; // Exit early, no further action required
        }

        const jokerArray = Array.isArray(jokerAnswers) ? jokerAnswers : [jokerAnswers];
        log.debug(`Joker answers fetched: ${jokerAnswers}`);

        return jokerArray; // Return joker array for further usage
    } catch (error) {
        log.error(`Error fetching joker answers: ${error}`);
        setError(error instanceof Error ? error : new Error(String(error))); // Set error state
    } finally {
        setLoading(false); // Hide the loading indicator after API call finishes
        clearTimeout(timeoutId); // Clear the timeout if the request finished before 3 seconds
    }
};