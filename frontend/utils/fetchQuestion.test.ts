import { fetchQuestions } from '@/utils/fetchQuestions';
import log from '@/utils/logger';
import { questions } from '@/utils/api';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/api', () => ({
    questions: jest.fn(),
}));

describe('fetchQuestions', () => {
    const topicId = 1;
    const difficultyLevel = 'easy';
    const excludeId = [1, 2, 3];
    const playerName = 'testPlayer';
    const score = 100;

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches questions successfully', async () => {
        const mockData = { id: 1, question: 'Sample question' };
        (questions as jest.Mock).mockResolvedValue(mockData);

        const result = await fetchQuestions(topicId, difficultyLevel, excludeId, playerName, score);

        expect(result).toEqual(mockData);
        expect(log.info).toHaveBeenCalledWith('Fetching quiz question from API...');
        expect(log.debug).toHaveBeenCalledWith(`Topic: ${topicId}`, `Difficulty: ${difficultyLevel}`, `Excluded IDs: ${excludeId}`);
        expect(log.debug).toHaveBeenCalledWith(`Fetched question: ${mockData.question}`);
    });

    it('returns empty array and logs info when no valid questions found', async () => {
        (questions as jest.Mock).mockResolvedValue({});

        const result = await fetchQuestions(topicId, difficultyLevel, excludeId, playerName, score);

        expect(result).toEqual([]);
        expect(log.info).toHaveBeenCalledWith('No valid questions found.');
    });

    it('logs error and throws when fetching questions fails', async () => {
        const mockError = new Error('Network error');
        (questions as jest.Mock).mockRejectedValue(mockError);

        await expect(fetchQuestions(topicId, difficultyLevel, excludeId, playerName, score)).rejects.toThrow('Failed to load questions.');
        expect(log.error).toHaveBeenCalledWith('Error fetching questions:', mockError);
    });
});