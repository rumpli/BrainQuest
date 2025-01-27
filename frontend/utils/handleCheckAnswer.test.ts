import { handleCheckAnswer } from '@/utils/handleCheckAnswer';
import log from '@/utils/logger';
import { checkAnswer } from '@/utils/checkAnswer';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/checkAnswer', () => ({
    checkAnswer: jest.fn(),
}));

describe('handleCheckAnswer', () => {
    const setCorrectAnswer = jest.fn();
    const setLoading = jest.fn();
    const setError = jest.fn();
    const setIsSlow = jest.fn();
    const setScore = jest.fn();
    const setInfo = jest.fn();
    const questionId = 1;
    const answerId = 1;
    const playerName = 'testPlayer';
    const score = 0;

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('handles correct answer successfully', async () => {
        const mockResponse = { correct: true, correctAnswerId: 1, info: 'Correct!' };
        checkAnswer.mockResolvedValue(mockResponse);

        const result = await handleCheckAnswer(
            setCorrectAnswer,
            setLoading,
            setError,
            setIsSlow,
            setScore,
            setInfo,
            questionId,
            answerId,
            playerName,
            score
        );

        expect(result).toEqual({ isCorrect: true, correctAnswerId: 1 });
        expect(setCorrectAnswer).toHaveBeenCalledWith(1);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setInfo).toHaveBeenCalledWith('Correct!');
        expect(setScore).toHaveBeenCalledWith(expect.any(Function));
    });

    it('handles incorrect answer successfully', async () => {
        const mockResponse = { correct: false, correctAnswerId: 2, info: 'Incorrect!' };
        checkAnswer.mockResolvedValue(mockResponse);

        const result = await handleCheckAnswer(
            setCorrectAnswer,
            setLoading,
            setError,
            setIsSlow,
            setScore,
            setInfo,
            questionId,
            answerId,
            playerName,
            score
        );

        expect(result).toEqual({ isCorrect: false, correctAnswerId: 2 });
        expect(setCorrectAnswer).toHaveBeenCalledWith(2);
        expect(setLoading).toHaveBeenCalledWith(false);
        expect(setInfo).toHaveBeenCalledWith('Incorrect!');
    });

    it('handles error during answer check', async () => {
        const mockError = new Error('Network error');
        checkAnswer.mockRejectedValue(mockError);

        const result = await handleCheckAnswer(
            setCorrectAnswer,
            setLoading,
            setError,
            setIsSlow,
            setScore,
            setInfo,
            questionId,
            answerId,
            playerName,
            score
        );

        expect(result).toEqual({ isCorrect: false, correctAnswerId: -1 });
        expect(setError).toHaveBeenCalledWith(mockError);
        expect(setLoading).toHaveBeenCalledWith(false);
    });

    // it('sets isSlow to true if request takes too long', async () => {
    //     jest.useFakeTimers();
    //     const mockResponse = { correct: true, correctAnswerId: 1, info: 'Correct!' };
    //     checkAnswer.mockImplementation(() => new Promise(resolve => setTimeout(() => resolve(mockResponse), 4000)));
    //
    //     const promise = handleCheckAnswer(
    //         setCorrectAnswer,
    //         setLoading,
    //         setError,
    //         setIsSlow,
    //         setScore,
    //         setInfo,
    //         questionId,
    //         answerId,
    //         playerName,
    //         score
    //     );
    //
    //     jest.advanceTimersByTime(3000);
    //     expect(setIsSlow).toHaveBeenCalledWith(true);
    //
    //     await promise;
    //     jest.useRealTimers();
    // }, 10000); // Increase timeout to 10 seconds
});