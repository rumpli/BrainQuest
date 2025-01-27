import { handleFetchQuestions } from '@/utils/handleFetchQuestions';
import log from '@/utils/logger';
import { fetchQuestions } from '@/utils/fetchQuestions';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/fetchQuestions', () => ({
    fetchQuestions: jest.fn().mockImplementation(() => Promise.resolve([])),
}));

describe('handleFetchQuestions', () => {
    const setQuestions = jest.fn();
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();
    const setExcludeQuestions = jest.fn();
    const topicId = 1;
    const difficultyLevel = 'easy';
    const excludeIds = [1, 2, 3];
    const playerName = 'Player1';
    const score = 100;

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches questions successfully', async () => {
        const mockQuestions = [{ id: 4, question: 'What is 2+2?' }];
        fetchQuestions.mockImplementation(() => Promise.resolve(mockQuestions));

        const result = await handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score);

        expect(setQuestions).toHaveBeenCalledWith(mockQuestions);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setIsSlow).toHaveBeenCalledWith(false);
        expect(result).toBe(true);
    });

    it('handles error during questions fetch', async () => {
        const mockError = new Error('Network error');
        fetchQuestions.mockImplementation(() => Promise.reject(mockError));

        const result = await handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score).catch(() => false);
        expect(setError).toHaveBeenCalledWith(mockError);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toBe(false);
    });

    it('handles no questions returned', async () => {
        fetchQuestions.mockImplementation(() => Promise.resolve([]));

        const result = await handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score);

        expect(setQuestions).toHaveBeenCalledWith([]);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toBe(false);
    });

    it('handles null questions returned', async () => {
        fetchQuestions.mockImplementation(() => Promise.resolve(null));

        const result = await handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score);

        expect(setQuestions).toHaveBeenCalledWith([]);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toBe(false);
    });

    // it('sets isSlow to true if request takes too long', async () => {
    //     jest.useFakeTimers();
    //     const mockQuestions = [{ id: 4, question: 'What is 2+2?' }];
    //     fetchQuestions.mockImplementation(() => new Promise(resolve => setTimeout(() => resolve(mockQuestions), 4000)));
    //
    //     const promise = handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score);
    //
    //     jest.advanceTimersByTime(3000);
    //     expect(setIsSlow).toHaveBeenCalledWith(true);
    //
    //     await promise;
    //     jest.useRealTimers();
    // }, 10000); // Increase timeout to 10 seconds

    it('handles empty questions array returned', async () => {
        fetchQuestions.mockImplementation(() => Promise.resolve([]));

        const result = await handleFetchQuestions(setQuestions, setLoading, setError, setIsSlow, setExcludeQuestions, topicId, difficultyLevel, excludeIds, playerName, score);

        expect(setQuestions).toHaveBeenCalledWith([]);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toBe(false);
    });
});