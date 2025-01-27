import { handleFetchHighscores } from '@/utils/handleFetchHighscores';
import log from '@/utils/logger';
import { fetchHighscores } from '@/utils/fetchHighscores';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/fetchHighscores', () => ({
    fetchHighscores: jest.fn(),
}));

describe('handleFetchHighscores', () => {
    const setHighscores = jest.fn();
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();
    const topicId = 1;
    const difficultyLevel = 'easy';
    const sortBy = 'score';
    const sortDir = 'desc';

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches highscores successfully', async () => {
        const mockHighscores = [{ id: 1, score: 100 }];
        fetchHighscores.mockResolvedValue(mockHighscores);

        await handleFetchHighscores(setHighscores, setLoading, setError, setIsSlow, topicId, difficultyLevel, sortBy, sortDir);

        expect(setHighscores).toHaveBeenCalledWith(mockHighscores);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setIsSlow).toHaveBeenCalledWith(false);
    });

    it('handles error during highscores fetch', async () => {
        const mockError = new Error('Network error');
        fetchHighscores.mockRejectedValue(mockError);

        await handleFetchHighscores(setHighscores, setLoading, setError, setIsSlow, topicId, difficultyLevel, sortBy, sortDir);

        expect(setError).toHaveBeenCalledWith(mockError);
        expect(setLoading).toHaveBeenCalledWith(false);
    });

    // it('sets isSlow to true if request takes too long', async () => {
    //     jest.useFakeTimers();
    //     const mockHighscores = [{ id: 1, score: 100 }];
    //     fetchHighscores.mockImplementation(() => new Promise(resolve => setTimeout(() => resolve(mockHighscores), 4000)));
    //
    //     const promise = handleFetchHighscores(setHighscores, setLoading, setError, setIsSlow, topicId, difficultyLevel, sortBy, sortDir);
    //
    //     jest.advanceTimersByTime(3000);
    //     expect(setIsSlow).toHaveBeenCalledWith(true);
    //
    //     await promise;
    //     jest.useRealTimers();
    // });
});