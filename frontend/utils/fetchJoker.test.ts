import { fetchJoker } from '@/utils/fetchJoker';
import log from '@/utils/logger';
import { useJoker } from '@/utils/api';

jest.mock('@/utils/logger', () => ({
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/api', () => ({
    useJoker: jest.fn(),
}));

describe('fetchJoker', () => {
    const questionsId = 1;
    const jokerType = '50-50';

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches joker questions successfully', async () => {
        const mockData = { question: 'Sample question' };
        useJoker.mockResolvedValue(mockData);

        const result = await fetchJoker(questionsId, jokerType);

        expect(result).toEqual(mockData);
        expect(log.debug).toHaveBeenCalledWith(`Fetched joker questions: ${mockData.question}`);
    });

    it('logs error and throws when fetching joker questions fails', async () => {
        const mockError = new Error('Network error');
        useJoker.mockRejectedValue(mockError);

        await expect(fetchJoker(questionsId, jokerType)).rejects.toThrow('Failed to load joker questions.');
        expect(log.error).toHaveBeenCalledWith('Error fetching joker questions:', mockError);
    });
});