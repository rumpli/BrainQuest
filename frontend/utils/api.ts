import log from '@/utils/logger';

const loadConfig = async () => {
    const config = await import('../config');
    log.info('Loaded config:', config.default);
    return config.default;
};

/**
 * Handles the response from a fetch request.
 * @param {Response} response - The response object from the fetch request.
 * @returns {Promise<any>} - The parsed response data.
 * @throws {Error} - Throws an error if the response status is 401 or not ok.
 */
const handleResponse = async (response: Response) => {
    if (response.status === 401) {
        throw new Error('Unauthenticated');
    }
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    const text = await response.text();
    return text ? JSON.parse(text) : {};
};

/**
 * Fetches data from the specified endpoint.
 * @param {string} endpoint - The API endpoint to fetch data from.
 * @returns {Promise<any>} - The fetched data.
 * @throws {Error} - Throws an error if the fetch request fails.
 */
export const fetchData = async (endpoint: string): Promise<any> => {
    try {
        const config = await loadConfig();
        const fullApiUrl = `${config.API_URL}`;
        log.debug(`Fetching data from ${fullApiUrl}${endpoint}`);
        const response = await fetch(`${fullApiUrl}${endpoint}`);
        return await handleResponse(response);
    } catch (error) {
        log.error('Error fetching data:', error);
        return Promise.reject(error);
    }
};

/**
 * Posts data to the specified endpoint.
 * @param {string} endpoint - The API endpoint to post data to.
 * @param {any} data - The data to post.
 * @returns {Promise<any>} - The response data.
 * @throws {Error} - Throws an error if the post request fails.
 */
export const postData = async (endpoint: string, data: any): Promise<any> => {
    try {
        const config = await loadConfig();
        const fullApiUrl = `${config.API_URL}`;
        log.debug(`Posting data to ${fullApiUrl}${endpoint}`, data);
        const response = await fetch(`${fullApiUrl}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });
        return await handleResponse(response);
    } catch (error) {
        log.error('Error posting data:', error);
        return Promise.reject(error);
    }
};

/**
 * Logs in a user with the provided username and password.
 * @param {string} username - The username of the user.
 * @param {string} password - The password of the user.
 * @returns {Promise<any>} - The response data including the access token.
 * @throws {Error} - Throws an error if the login request fails.
 */
export const login = async (username: string, password: string): Promise<any> => {
    try {
        const config = await loadConfig();
        const fullApiUrl = `${config.API_URL}`;
        log.debug(JSON.stringify({ username, password }));
        const response = await fetch(`${fullApiUrl}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });
        if (response.status === 401) {
            throw new Error('Incorrect username or password');
        }
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();

        log.debug('Login response data:', data);

        const accessToken = data.accessToken;

        if (typeof accessToken !== 'string') {
            throw new Error('Access token is not a valid string');
        }

        return data;
    } catch (error) {
        log.error('Error posting data:', error);
        throw error;
    }
};

/**
 * Fetches topics from the API.
 * @returns {Promise<any>} - The fetched topics.
 */
export const topics = async (): Promise<any> => fetchData('/topics');

/**
 * Fetches highscores based on the provided parameters.
 * @param {number} topicId - The ID of the topic.
 * @param {string} difficultyLevel - The difficulty level.
 * @param {string} sortBy - The field to sort by.
 * @param {string} sortDir - The direction to sort (asc/desc).
 * @returns {Promise<any>} - The fetched highscores.
 */
export const highscores = async (topicId: number, difficultyLevel: string, sortBy: string, sortDir: string): Promise<any> => {
    const queryParams = `topicId=${topicId}&difficulty=${difficultyLevel}&sortBy=${sortBy}&sortDir=${sortDir}`;
    return fetchData(`/highscores?${queryParams}`);
};

/**
 * Fetches questions based on the provided parameters.
 * @param {number} topicId - The ID of the topic.
 * @param {string} difficultyLevel - The difficulty level.
 * @param {number[]} excludeIds - The IDs of questions to exclude.
 * @param {string} playerName - The name of the player.
 * @param {number} score - The score of the player.
 * @returns {Promise<any>} - The fetched questions.
 */
export const questions = async (topicId: number, difficultyLevel: string, excludeIds: number[], playerName: string, score: number): Promise<any> => {
    const queryParams = `topicId=${topicId}&difficulty=${difficultyLevel}&excludeIds=${excludeIds.join(',')}&playerName=${playerName}&score=${score}`;
    return fetchData(`/questions/quiz-question?${queryParams}`);
};

/**
 * Checks if the provided answer is correct.
 * @param {number} questionId - The ID of the question.
 * @param {any} body - The answer data.
 * @returns {Promise<any>} - The response data.
 */
export const checkCorrectAnswer = async (questionId: number, body: any): Promise<any> => postData(`/questions/${questionId}/correct`, body);

/**
 * Uses a joker for the specified question.
 * @param {number} questionId - The ID of the question.
 * @param {string} jokerType - The type of joker to use.
 * @returns {Promise<any>} - The response data.
 */
export const useJoker = async (questionId: number, jokerType: string): Promise<any> => fetchData(`/questions/${questionId}/joker?joker=${jokerType}`);
