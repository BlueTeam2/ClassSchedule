import { getShortTitle } from './shortTitle';

describe('getShortTitle function', () => {
    it('should return short title with 2 symbols', () => {
        const title = '23(203)';
        expect(getShortTitle(title, 2)).toEqual('23...');
    });
    it('should return short title with 6 symbols', () => {
        const title = '27(207)';
        expect(getShortTitle(title, 6)).toEqual('27(207...');
    });
});
