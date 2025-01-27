import { handleStartPress } from '@/utils/handleStartPress';
import { handleFetchTopics } from '@/utils/handleFetchTopics';

jest.mock('@/utils/handleFetchTopics', () => ({
    handleFetchTopics: jest.fn(),
}));

describe('handleStartPress', () => {
    const setStarted = jest.fn();
    const setTopics = jest.fn();
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('sets started state to true and fetches topics successfully', async () => {
        (handleFetchTopics as jest.Mock).mockResolvedValueOnce(undefined);

        await handleStartPress(setStarted, setTopics, setLoading, setError, setIsSlow);

        expect(setStarted).toHaveBeenCalledWith(true);
        expect(handleFetchTopics).toHaveBeenCalledWith(setTopics, setLoading, setError, setIsSlow);
    });

    it('handles error during topics fetch', async () => {
        const mockError = new Error('Network error');
        (handleFetchTopics as jest.Mock).mockRejectedValueOnce(mockError);

        await handleStartPress(setStarted, setTopics, setLoading, setError, setIsSlow);

        expect(setStarted).toHaveBeenCalledWith(true);
        expect(handleFetchTopics).toHaveBeenCalledWith(setTopics, setLoading, setError, setIsSlow);
        expect(setError).toHaveBeenCalledWith(mockError);
    });

    // it('handles slow API response', async () => {
    //     jest.useFakeTimers();
    //     (handleFetchTopics as jest.Mock).mockImplementationOnce(() => new Promise(resolve => setTimeout(resolve, 4000)));
    //
    //     const promise = handleStartPress(setStarted, setTopics, setLoading, setError, setIsSlow);
    //
    //     jest.advanceTimersByTime(3000);
    //     expect(setIsSlow).toHaveBeenCalledWith(true);
    //
    //     await promise;
    //     jest.useRealTimers();
    // });
});