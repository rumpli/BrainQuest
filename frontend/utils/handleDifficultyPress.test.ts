import { handleDifficultyPress } from '@/utils/handleDifficultyPress';
import log from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
}));

describe('handleDifficultyPress', () => {
    const setDifficultySubmitted = jest.fn();
    const difficulty = 'easy';

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('sets difficulty submitted and logs the selected difficulty', async () => {
        const result = await handleDifficultyPress(setDifficultySubmitted, difficulty);

        expect(result).toBe(difficulty);
        expect(setDifficultySubmitted).toHaveBeenCalledWith(true);
        expect(log.info).toHaveBeenCalledWith(`Difficulty selected: "${difficulty}"`);
    });

    it('handles empty difficulty', async () => {
        const emptyDifficulty = '';
        const result = await handleDifficultyPress(setDifficultySubmitted, emptyDifficulty);

        expect(result).toBe(emptyDifficulty);
        expect(setDifficultySubmitted).toHaveBeenCalledWith(true);
        expect(log.info).toHaveBeenCalledWith('Difficulty selected: ""');
    });

    it('handles null difficulty', async () => {
        const nullDifficulty = null;
        const result = await handleDifficultyPress(setDifficultySubmitted, nullDifficulty);

        expect(result).toBe(nullDifficulty);
        expect(setDifficultySubmitted).toHaveBeenCalledWith(true);
        expect(log.info).toHaveBeenCalledWith('Difficulty selected: "null"');
    });
});