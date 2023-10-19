import { sortByName } from './sortArray';

const array = [{ name: '1 к. 11 ауд.' }, { name: '1 к. 18 ауд.' }, { name: '1 к. 15 ауд.' }];
const expectedArray = [
    { name: '1 к. 11 ауд.' },
    { name: '1 к. 15 ауд.' },
    { name: '1 к. 18 ауд.' },
];

describe('sortByName function', () => {
    it('should return sorted array', () => {
        expect(sortByName(array)).toEqual(expectedArray);
    });
});
