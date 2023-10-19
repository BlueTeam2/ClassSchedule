import { sortStrings } from './sortStrings';

describe('sortStrings function', () => {
    it('should return -1 if the first string is less than the second string', () => {
        const firstString = 'first string';
        const secondString = 'second string';
        expect(sortStrings(firstString, secondString)).toEqual(-1);
    });
    it('should return 1 if the second string is less than the first string', () => {
        const firstString = 'first string longer';
        const secondString = 'second string';
        expect(sortStrings(firstString, secondString)).toEqual(1);
    });
    it('should return 0 if the second string is equal the first string', () => {
        const firstString = 'test';
        const secondString = 'test';
        expect(sortStrings(firstString, secondString)).toEqual(0);
    });
});
