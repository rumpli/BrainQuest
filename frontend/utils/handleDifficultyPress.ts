import React from "react";

// Utils
import log from '@/utils/logger';

/**
 * Handles the logic for when a difficulty level is selected.
 *
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setDifficultySubmitted - Function to set the state indicating if the difficulty has been submitted.
 * @param {string} difficulty - The selected difficulty level.
 * @returns {Promise<string>} A promise that resolves with the selected difficulty level.
 */

export const handleDifficultyPress = async (
    setDifficultySubmitted: React.Dispatch<React.SetStateAction<boolean>>,
    difficulty: string | null
): Promise<string | null> => {
    setDifficultySubmitted(true);
    log.info(`Difficulty selected: "${String(difficulty)}"`);
    return difficulty;
};