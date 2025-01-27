import { fetchData, postData, topics, highscores, questions, checkCorrectAnswer, useJoker, login } from './api';
import log from '@/utils/logger';
import { Platform } from 'react-native';
import * as SecureStore from 'expo-secure-store';

jest.mock('@/utils/logger', () => ({
    debug: jest.fn(),
    error: jest.fn(),
}));

describe('api', () => {
    const originalPlatform = Platform.OS;
    const apiUrl = process.env.EXPO_PUBLIC_API_URL;

    afterEach(() => {
        Platform.OS = originalPlatform;
        jest.clearAllMocks();
    });

    describe('fetchData', () => {
        it('logs error and rejects promise on fetch failure', async () => {
            Platform.OS = 'ios';
            const endpoint = '/test-endpoint';
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(fetchData(endpoint)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error fetching data:', mockError);
        });
    });

    describe('postData', () => {
        it('logs error and rejects promise on post failure', async () => {
            Platform.OS = 'ios';
            const endpoint = '/test-endpoint';
            const data = { key: 'value' };
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(postData(endpoint, data)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error posting data:', mockError);
        });
    });

    describe('topics', () => {
        it('logs error and rejects promise on fetch failure', async () => {
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(topics()).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error fetching data:', mockError);
        });
    });

    describe('highscores', () => {
        it('logs error and rejects promise on fetch failure', async () => {
            const topicId = 1;
            const difficultyLevel = 'easy';
            const sortBy = 'score';
            const sortDir = 'desc';
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(highscores(topicId, difficultyLevel, sortBy, sortDir)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error fetching data:', mockError);
        });
    });

    describe('questions', () => {
        it('logs error and rejects promise on fetch failure', async () => {
            const topicId = 1;
            const difficultyLevel = 'easy';
            const excludeIds = [1, 2, 3];
            const playerName = 'testplayer';
            const score = 100;
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(questions(topicId, difficultyLevel, excludeIds, playerName, score)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error fetching data:', mockError);
        });
    });

    describe('checkCorrectAnswer', () => {
        it('logs error and rejects promise on post failure', async () => {
            const questionId = 1;
            const body = { answer: 'test' };
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(checkCorrectAnswer(questionId, body)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error posting data:', mockError);
        });
    });

    describe('useJoker', () => {
        it('logs error and rejects promise on fetch failure', async () => {
            const questionId = 1;
            const jokerType = '50-50';
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(useJoker(questionId, jokerType)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error fetching data:', mockError);
        });
    });

    describe('login', () => {
        it('logs error and rejects promise on login failure', async () => {
            const username = 'testuser';
            const password = 'testpassword';
            const mockError = new Error('Network error');
            global.fetch = jest.fn().mockRejectedValue(mockError);

            await expect(login(username, password)).rejects.toThrow(mockError);
            expect(log.error).toHaveBeenCalledWith('Error posting data:', mockError);
        });
    });
});