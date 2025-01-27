import React from "react";
import log from '@/utils/logger';

/**
 * Handles the logic for when the user presses a topic.
 *
 * @param {React.Dispatch<React.SetStateAction<boolean>>} setTopicSubmitted - Function to set the topic submitted state.
 * @param {React.Dispatch<React.SetStateAction<any>>} setChosenTopic - Function to set the chosen topic state.
 * @param {any} topic - The topic that is being selected.
 * @returns {Promise<any>} A promise that resolves with the topic difficulty.
 */
export const handleTopicPress = async (
    setTopicSubmitted: React.Dispatch<React.SetStateAction<boolean>>,
    setChosenTopic: React.Dispatch<React.SetStateAction<any>>,
    topic: any
): Promise<any> => {
    if (!topic) {
        log.error('Topic is null or undefined');
        return Promise.reject('Topic is null or undefined');
    }
    setTopicSubmitted(true);
    setChosenTopic(topic);
    log.info(`Topic selected: ${JSON.stringify(topic.name)}`);
    log.debug(`Topic difficulties: ${JSON.stringify(topic.difficulty)}`);
    return topic.difficulty;
};