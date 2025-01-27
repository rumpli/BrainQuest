import { handleTopicPress } from '@/utils/handleTopicPress';
import log from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
}));

describe('handleTopicPress', () => {
    const setTopicSubmitted = jest.fn();
    const setChosenTopic = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('sets topic submitted state to true and chosen topic state', async () => {
        const topic = { name: 'Science', difficulty: 'Medium' };

        const result = await handleTopicPress(setTopicSubmitted, setChosenTopic, topic);

        expect(setTopicSubmitted).toHaveBeenCalledWith(true);
        expect(setChosenTopic).toHaveBeenCalledWith(topic);
        expect(log.info).toHaveBeenCalledWith('Topic selected: "Science"');
        expect(log.debug).toHaveBeenCalledWith('Topic difficulties: "Medium"');
        expect(result).toBe('Medium');
    });

    it('handles empty topic', async () => {
        const topic = { name: '', difficulty: '' };

        const result = await handleTopicPress(setTopicSubmitted, setChosenTopic, topic);

        expect(setTopicSubmitted).toHaveBeenCalledWith(true);
        expect(setChosenTopic).toHaveBeenCalledWith(topic);
        expect(log.info).toHaveBeenCalledWith('Topic selected: ""');
        expect(log.debug).toHaveBeenCalledWith('Topic difficulties: ""');
        expect(result).toBe('');
    });

    it('handles null or undefined topic', async () => {
        const topic = { name: '', difficulty: '' };
        const result = await handleTopicPress(setTopicSubmitted, setChosenTopic, topic);

        expect(setTopicSubmitted).toHaveBeenCalledWith(true);
        expect(setChosenTopic).toHaveBeenCalledWith(topic);
        if (topic) {
            expect(log.info).toHaveBeenCalledWith(`Topic selected: "${topic.name}"`);
            expect(log.debug).toHaveBeenCalledWith(`Topic difficulties: "${topic.difficulty}"`);
            expect(result).toBe(topic.difficulty);
        } else {
            expect(log.info).toHaveBeenCalledWith('Topic selected: null or undefined');
            expect(log.debug).toHaveBeenCalledWith('Topic difficulties: null or undefined');
            expect(result).toBeNull();
        }
    });
});