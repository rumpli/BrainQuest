import { checkAnswer } from '@/utils/checkAnswer';
import { checkCorrectAnswer } from '@/utils/api';
import log from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

jest.mock('@/utils/api', () => ({
    checkCorrectAnswer: jest.fn(),
}));

describe('checkAnswer', () => {
    const questionId = 1;
    const answerId = 2;
    const playerName = 'TestPlayer';
    const score = 100;

    it('returns correct answer result successfully', async () => {
        const mockResponse = [{ correct: true }];
        checkCorrectAnswer.mockResolvedValue(mockResponse);

        const result = await checkAnswer(questionId, answerId, playerName, score);

        expect(log.info).toHaveBeenCalledWith("Check the answer...");
        expect(log.debug).toHaveBeenCalledWith(`Answer ID: ${answerId}`, `Player Name: ${playerName}`, `Score: ${score}`);
        expect(checkCorrectAnswer).toHaveBeenCalledWith(questionId, { answerId, playerName, score });
        expect(result).toEqual(mockResponse);
    });

    it('throws an error if checkCorrectAnswer fails', async () => {
        const mockError = new Error('API error');
        checkCorrectAnswer.mockRejectedValue(mockError);

        await expect(checkAnswer(questionId, answerId, playerName, score)).rejects.toThrow('Failed to check questions.');
        expect(log.error).toHaveBeenCalledWith("Error checking answer:", mockError);
    });
});