import React from "react";
import { handleFetchTopics } from "@/utils/handleFetchTopics";

/**
 * Handles the logic for when the user presses the start button.
 *
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setStarted - Sets the started state to true.
 * @param {React.Dispatch<React.SetStateAction<any[]>>} setTopics - Sets the topics state with the fetched topics.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setLoading - Sets the loading state to true while the API request is in progress.
 * @param {React.Dispatch<React.SetStateAction<Error | null>>} setError - Sets any error that occurs during the fetch process.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setIsSlow - Sets the state to indicate if the request is taking too long.
 * @returns {Promise<void>} A promise that resolves when the start press handling is complete.
 */
export const handleStartPress = async (
    setStarted: React.Dispatch<React.SetStateAction<boolean>>,
    setTopics: React.Dispatch<React.SetStateAction<any[]>>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setError: React.Dispatch<React.SetStateAction<Error | null>>,
    setIsSlow: React.Dispatch<React.SetStateAction<boolean>> // Set the state for slow API response
): Promise<void> => {
    setStarted(true); // Mark the game as started
    try {
        await handleFetchTopics(setTopics, setLoading, setError, setIsSlow);
    } catch (error) {
        setError(error instanceof Error ? error : new Error(String(error)));
    }
};