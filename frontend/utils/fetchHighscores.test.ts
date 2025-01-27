import { fetchHighscores } from '@/utils/fetchHighscores';
import log from '@/utils/logger';
import { highscores } from '@/utils/api';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/api', () => ({
    highscores: jest.fn(),
}));

describe('fetchHighscores', () => {
    const topicId = 1;
    const difficultyLevel = 'easy';
    const sortBy = 'score';
    const sortDir = 'desc';

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches highscores successfully', async () => {
        const mockData = [{ id: 1, score: 100 }];
        highscores.mockResolvedValue(mockData);

        const result = await fetchHighscores(topicId, difficultyLevel, sortBy, sortDir);

        expect(result).toEqual(mockData);
        expect(log.info).toHaveBeenCalledWith('Fetching highscores from API...');
        expect(log.debug).toHaveBeenCalledWith(`Fetched highscores: ${JSON.stringify(mockData)}`);
    });

    it('logs error and throws when fetching highscores fails', async () => {
        const mockError = new Error('Network error');
        highscores.mockRejectedValue(mockError);

        await expect(fetchHighscores(topicId, difficultyLevel, sortBy, sortDir)).rejects.toThrow('Failed to load highscores.');
        expect(log.error).toHaveBeenCalledWith('Error fetching highscores:', mockError);
    });
});