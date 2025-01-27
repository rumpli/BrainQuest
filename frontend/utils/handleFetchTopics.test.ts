import { handleFetchTopics } from '@/utils/handleFetchTopics';
import { fetchTopics } from '@/utils/fetchTopics';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/fetchTopics', () => ({
    fetchTopics: jest.fn().mockResolvedValue([]),
}));

describe('handleFetchTopics', () => {
    const setTopics = jest.fn();
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches topics successfully', async () => {
        const mockTopics = [{ id: 1, name: 'Topic 1' }];
        fetchTopics.mockResolvedValue(mockTopics);

        await handleFetchTopics(setTopics, setLoading, setError, setIsSlow);

        expect(setTopics).toHaveBeenCalledWith(mockTopics);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setIsSlow).toHaveBeenCalledWith(false);
    });

    it('handles error during topics fetch', async () => {
        const mockError = new Error('Network error');
        fetchTopics.mockRejectedValue(mockError);

        await handleFetchTopics(setTopics, setLoading, setError, setIsSlow);

        expect(setError).toHaveBeenCalledWith(mockError);
        expect(setLoading).toHaveBeenCalledWith(false);
    });

    it('handles no topics returned', async () => {
        fetchTopics.mockResolvedValue([]);

        await handleFetchTopics(setTopics, setLoading, setError, setIsSlow);

        expect(setTopics).toHaveBeenCalledWith([]);
        expect(setLoading).toHaveBeenCalledWith(false);
    });

    it('handles null topics returned', async () => {
        fetchTopics.mockResolvedValue([]);

        await handleFetchTopics(setTopics, setLoading, setError, setIsSlow);

        expect(setTopics).toHaveBeenCalledWith([]);
        expect(setLoading).toHaveBeenCalledWith(false);
    });
});