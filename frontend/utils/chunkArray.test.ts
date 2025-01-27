import { chunkArray } from '@/utils/chunkArray';
import logger from '@/utils/logger';

jest.mock('@/utils/logger', () => ({
    debug: jest.fn(),
}));

describe('chunkArray', () => {
    it('chunks array into smaller arrays of specified size', () => {
        const array = [1, 2, 3, 4, 5];
        const size = 2;
        const expected = [[1, 2], [3, 4], [5]];

        const result = chunkArray(array, size);

        expect(result).toEqual(expected);
        expect(logger.debug).toHaveBeenCalledWith('Chunk 0: [1,2]');
        expect(logger.debug).toHaveBeenCalledWith('Chunk 1: [3,4]');
        expect(logger.debug).toHaveBeenCalledWith('Chunk 2: [5]');
    });

    it('returns empty array when input array is empty', () => {
        const array = [];
        const size = 2;
        const expected = [];

        const result = chunkArray(array, size);

        expect(result).toEqual(expected);
    });

    it('returns the same array when chunk size is greater than array length', () => {
        const array = [1, 2, 3];
        const size = 5;
        const expected = [[1, 2, 3]];

        const result = chunkArray(array, size);

        expect(result).toEqual(expected);
        expect(logger.debug).toHaveBeenCalledWith('Chunk 0: [1,2,3]');
    });

    it('handles chunk size of 1 correctly', () => {
        const array = [1, 2, 3];
        const size = 1;
        const expected = [[1], [2], [3]];

        const result = chunkArray(array, size);

        expect(result).toEqual(expected);
        expect(logger.debug).toHaveBeenCalledWith('Chunk 0: [1]');
        expect(logger.debug).toHaveBeenCalledWith('Chunk 1: [2]');
        expect(logger.debug).toHaveBeenCalledWith('Chunk 2: [3]');
    });

    it('handles chunk size equal to array length correctly', () => {
        const array = [1, 2, 3];
        const size = 3;
        const expected = [[1, 2, 3]];

        const result = chunkArray(array, size);

        expect(result).toEqual(expected);
        expect(logger.debug).toHaveBeenCalledWith('Chunk 0: [1,2,3]');
    });
});