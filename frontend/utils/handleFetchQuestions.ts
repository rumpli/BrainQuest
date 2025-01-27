import React from "react";

// Utils
import log from '@/utils/logger';
import { fetchQuestions } from '@/utils/fetchQuestions';

/**
 * Handles the logic for fetching questions from the API.
 *
 * @param {React.Dispatch<React.SetStateAction<any[]>>} setQuestions - Sets the questions state with the fetched questions.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setLoading - Sets the loading state to true while the API request is in progress.
 * @param {React.Dispatch<React.SetStateAction<Error | null>>} setError - Sets any error that occurs during the fetch process.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setIsSlow - Sets the state to indicate if the request is taking too long.
 * @param {React.Dispatch<React.SetStateAction<number[]>>} setExcludeQuestions - Sets the state to exclude certain questions.
 * @param {number} topicId - The ID of the topic.
 * @param {string} difficultyLevel - The difficulty level.
 * @param {number[]} excludeIds - The IDs of the questions to exclude.
 * @param playerName - The player name.
 * @param score - The player score.
 * @returns {Promise<boolean>} A promise that resolves to a boolean indicating if there are valid questions.
 */
export const handleFetchQuestions = async (
    setQuestions: React.Dispatch<React.SetStateAction<any[]>>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setError: React.Dispatch<React.SetStateAction<Error | null>>,
    setIsSlow: React.Dispatch<React.SetStateAction<boolean>>,
    setExcludeQuestions: React.Dispatch<React.SetStateAction<number[]>>,
    topicId: number,
    difficultyLevel: string,
    excludeIds: number[],
    playerName: string,
    score: number,
): Promise<boolean> => {
    setLoading(true); // Show the loading indicator
    setIsSlow(false); // Reset "slow" state to false before the request
    log.info("Fetching question in the background...");

    let hasInvalidQuestions;

    // Set timeout for detecting slow API
    const timeoutId = setTimeout(() => {
        setIsSlow(true); // Set the "slow" state to true after 3 seconds
    }, 3000); // 3 seconds timeout

    try {
        const question = await fetchQuestions(topicId, difficultyLevel, excludeIds, playerName, score); // Await the result from the utility function

        // Check if the API returned no questions
        if (!question || (Array.isArray(question) && question.length === 0)) {
            log.info('No more questions to fetch. Game successfully completed!');
            setLoading(false); // No need to load anymore
            setQuestions([]); // Set an empty array to indicate no more questions
            return false; // Exit early, no further action required
        }

        // Ensure the response is an array of questions
        const questionArray = Array.isArray(question) ? question : [question];

        questionArray['length'] === 0 ? hasInvalidQuestions = false : hasInvalidQuestions = true;

        setQuestions(questionArray); // Set questions in state (as an array)
        setExcludeQuestions((prev) => [...prev, question?.id]); // Add the new question ID to the exclude list
        return hasInvalidQuestions;
    } catch (error) {
        log.error(`Error fetching questions: ${error}`);
        setError(error instanceof Error ? error : new Error(String(error)));
        return false;
    } finally {
        setLoading(false); // Hide the loading indicator
        clearTimeout(timeoutId); // Clear the timeout
    }
};