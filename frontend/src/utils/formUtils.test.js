import {
    initialCheckboxesStateForClasses,
    createClasslabel,
    checkSemesterYears,
} from './formUtils';

const lessons = [
    {
        id: 1,
        startTime: '08:20',
        endTime: '09:40',
        class_name: '1',
    },
    {
        id: 2,
        startTime: '09:50',
        endTime: '11:10',
        class_name: '2',
    },
];

describe('behavior of initialCheckboxesStateForClasses function', () => {
    test('should return object with each value of key "false"', () => {
        expect(initialCheckboxesStateForClasses(lessons)).toEqual({
            1: false,
            2: false,
        });
    });
});

describe('behavior of createClasslabel function', () => {
    test('should return class label ', () => {
        expect(createClasslabel(lessons, 1)).toBe('1 (08:20-09:40)');
    });
});

describe('behavior of checkSemesterYears function', () => {
    test('it does not show confirm if year is the same', () => {
        expect(checkSemesterYears('24/12/2021', '26/11/2021', 2021)).toBeTruthy();
    });
    test('it shows confirm if year is not the same', () => {
        window.confirm = jest.fn(() => true);
        expect(checkSemesterYears('24/01/2022', '26/11/2021', 2021)).toBeTruthy(
            expect(window.confirm).toBeCalled(),
        );
    });
});
