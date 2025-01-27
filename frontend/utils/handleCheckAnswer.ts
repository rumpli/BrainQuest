import log from '@/utils/logger';
import React from "react";
import { checkAnswer } from "@/utils/checkAnswer";

/**
 * Handles the logic for checking the answer to a question.
 *
 * @param {React.Dispatch<React.SetStateAction<number>>} setCorrectAnswer - Function to set the correct answer ID.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setLoading - Function to set the loading state.
 * @param {React.Dispatch<React.SetStateAction<Error | null>>} setError - Function to set any error that occurs.
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setIsSlow - Function to set the state indicating if the request is slow.
 * @param {React.Dispatch<React.SetStateAction<number>>} setScore - Function to set the score.
 * @param {React.Dispatch<React.SetStateAction<string>>} setInfo - Function to set additional information.
 * @param {number} questionId - The ID of the question being answered.
 * @param {number} answerId - The ID of the selected answer.
 * @param {string} playerName - The name of the player.
 * @param {number} score - The current score of the player.
 * @returns {Promise<{isCorrect: boolean, correctAnswerId: number}>} An object containing whether the answer is correct and the correct answer ID.
 */
export const handleCheckAnswer = async (
    setCorrectAnswer: React.Dispatch<React.SetStateAction<number>>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setError: React.Dispatch<React.SetStateAction<Error | null>>,
    setIsSlow: React.Dispatch<React.SetStateAction<boolean>>,
    setScore: React.Dispatch<React.SetStateAction<number>>,
    setInfo: React.Dispatch<React.SetStateAction<string>>,
    questionId: number,
    answerId: number,
    playerName: string,
    score: number,
): Promise<{ isCorrect: boolean; correctAnswerId: number; }> => {
    setLoading(true); // Show the loading indicator
    setIsSlow(false); // Reset "slow" state to false before the request
    log.info("Check answer in the background...");

    // Set timeout for detecting slow API
    const timeoutId = setTimeout(() => {
        setIsSlow(true); // Set the "slow" state to true after 3 seconds
    }, 3000); // 3 seconds timeout

    try {
        log.debug(`Checking answer for question ID: ${questionId}, answer ID: ${answerId}, player name: ${playerName}, score: ${score}`);
        const correctAnswer = await checkAnswer(questionId, answerId, playerName, score); // Await the result from the utility function

        // Log the correct answer ID
        log.debug(`Correct answer ID: ${correctAnswer.correctAnswerId}`);
        setCorrectAnswer(correctAnswer.correctAnswerId);

        // Log the current score
        log.debug(`Current score: ${score}`);

        log.debug(`Info: ${correctAnswer.info}`);
        setInfo(correctAnswer.info ? correctAnswer.info : "Keine Infos vorhanden.");

        if (correctAnswer.correct) setScore((score) => score + 1);
        return {
            isCorrect: correctAnswer.correct,
            correctAnswerId: correctAnswer.correctAnswerId
        };
    } catch (error) {
        log.error(`Error fetching questions: ${error}`);
        setError(error instanceof Error ? error : new Error(String(error))); // Set error state
        return { isCorrect: false, correctAnswerId: -1 };
    } finally {
        setLoading(false); // Hide the loading indicator after API call finishes
        clearTimeout(timeoutId); // Clear the timeout if the request finished before 3 seconds
    }
};