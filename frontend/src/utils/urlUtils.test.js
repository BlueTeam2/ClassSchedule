import { getDataFromParams } from './urlUtils';

describe('getDataFromParams function', () => {
    const location = {};
    it('should return object with params semester teacher group', () => {
        location.search = '?semester=43&teacher=12&group=11';
        expect(getDataFromParams(location)).toEqual({
            group: '11',
            semester: '43',
            teacher: '12',
        });
    });
});
