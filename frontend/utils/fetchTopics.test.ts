import { fetchTopics } from '@/utils/fetchTopics';
import log from '@/utils/logger';
import { topics } from '@/utils/api';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/api', () => ({
    topics: jest.fn(),
}));

describe('fetchTopics', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches topics successfully', async () => {
        const mockData = [{ name: 'Sample topic' }];
        topics.mockImplementation(() => Promise.resolve(mockData));

        const result = await fetchTopics();

        expect(result).toEqual(mockData);
        expect(log.info).toHaveBeenCalledWith('Fetching topics from API...');
        expect(log.debug).toHaveBeenCalledWith(`Fetched topics: ${mockData.map(topic => topic.name)}`);
    });

    it('logs error and throws when fetching topics fails', async () => {
        const mockError = new Error('Network error');
        topics.mockImplementation(() => Promise.reject(mockError));

        await expect(fetchTopics()).rejects.toThrow('Failed to load topics.');
        expect(log.error).toHaveBeenCalledWith('Error fetching topics:', mockError);
    });
});