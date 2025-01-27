import { handleFetchJoker } from '@/utils/handleFetchJoker';
import log from '@/utils/logger';
import { fetchJoker } from '@/utils/fetchJoker';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    error: jest.fn(),
    debug: jest.fn(),
}));

jest.mock('@/utils/fetchJoker', () => ({
    fetchJoker: jest.fn(),
}));

describe('handleFetchJoker', () => {
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();
    const questionId = 1;
    const jokerType = 'FIFTY_FIFTY';

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('fetches joker answers successfully', async () => {
        const mockJokerAnswers = [{ id: 1, answer: 'A' }];
        (fetchJoker as jest.Mock).mockResolvedValue(mockJokerAnswers);

        const result = await handleFetchJoker(setLoading, setError, setIsSlow, questionId, jokerType);

        expect(setLoading).toHaveBeenCalledWith(true);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setIsSlow).toHaveBeenCalledWith(false);
        expect(result).toEqual(mockJokerAnswers);
    });

    it('handles error during joker answers fetch', async () => {
        const mockError = new Error('Network error');
        (fetchJoker as jest.Mock).mockRejectedValue(mockError);

        const result = await handleFetchJoker(setLoading, setError, setIsSlow, questionId, jokerType);

        expect(setError).toHaveBeenCalledWith(mockError);
        expect(setLoading).toHaveBeenCalledWith(true);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toBeUndefined();
    });

    it('handles no joker answers returned', async () => {
        (fetchJoker as jest.Mock).mockResolvedValue([]);

        const result = await handleFetchJoker(setLoading, setError, setIsSlow, questionId, jokerType);

        expect(setLoading).toHaveBeenCalledWith(true);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toEqual([]);
    });

    it('handles null joker answers returned', async () => {
        (fetchJoker as jest.Mock).mockResolvedValue(null);

        const result = await handleFetchJoker(setLoading, setError, setIsSlow, questionId, jokerType);

        expect(setLoading).toHaveBeenCalledWith(true);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toEqual([]);
    });

    it('sets isSlow to true if request takes too long', async () => {
        jest.useFakeTimers();
        (fetchJoker as jest.Mock).mockImplementation(() => new Promise(resolve => setTimeout(() => resolve([{ id: 1, answer: 'A' }]), 4000)));

        const promise = handleFetchJoker(setLoading, setError, setIsSlow, questionId, jokerType);
        jest.advanceTimersByTime(3000);

        expect(setIsSlow).toHaveBeenCalledWith(true);

        jest.advanceTimersByTime(1000);
        const result = await promise;

        expect(setLoading).toHaveBeenCalledWith(false);
        expect(result).toEqual([{ id: 1, answer: 'A' }]);
    });
});