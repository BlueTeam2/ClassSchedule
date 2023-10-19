import { firstStringLetterCapital } from './strings';

describe('firstStringLetterCapital function', () => {
    it('should return word with capitalize letter', () => {
        const str = 'hello';
        expect(firstStringLetterCapital(str)).toEqual('Hello');
    });
    it('should return first  word with capitalize letter', () => {
        const str = 'hello world';
        expect(firstStringLetterCapital(str)).toEqual('Hello world');
    });
});
