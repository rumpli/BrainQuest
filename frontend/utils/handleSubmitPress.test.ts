import { handleSubmitPress } from '@/utils/handleSubmitPress';
import log from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    debug: jest.fn(),
}));

describe('handleSubmitPress', () => {
    const setNameSubmitted = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('sets name submitted state to true', () => {
        handleSubmitPress(setNameSubmitted, 'John Doe');

        expect(setNameSubmitted).toHaveBeenCalledWith(true);
    });

    it('logs the submitted name', () => {
        handleSubmitPress(setNameSubmitted, 'John Doe');

        expect(log.debug).toHaveBeenCalledWith('Name submitted: John Doe');
    });

    it('handles empty name submission', () => {
        handleSubmitPress(setNameSubmitted, '');

        expect(setNameSubmitted).toHaveBeenCalledWith(true);
        expect(log.debug).toHaveBeenCalledWith('Name submitted: ');
    });
});