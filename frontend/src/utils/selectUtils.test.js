import {
    setOptions,
    setSemesterOptions,
    setDepartmentOptions,
    getGroupsOptionsForSelect,
    getOptionLabelWithAvailable,
} from './selectUtils';
import { COMMON_AVAILABLE, COMMON_UNAVAILABLE } from '../constants/translationLabels/common';
import i18n from '../i18n';

const group = [
    { id: 116, disable: false, title: '100' },
    { id: 27, disable: false, title: '102-А' },
];
const expectedGroup = [
    { id: 116, value: 116, label: '100' },
    { id: 27, value: 27, label: '102-А' },
];

describe('getGroupsOptionsForSelect function', () => {
    it('should return empty array if group array is empty', () => {
        const emptyGroupArray = [];
        expect(getGroupsOptionsForSelect(emptyGroupArray)).toEqual([]);
    });
    it('should return array with correct format of group', () => {
        expect(getGroupsOptionsForSelect(group)).toEqual(expectedGroup);
    });
});

describe('getOptionLabelWithAvailable function', () => {
    it('should return option.name with COMMON_AVAILABLE if option available is true', () => {
        const optionAvailable = { id: 74, name: '1 к. 40 ауд.', available: true };
        expect(getOptionLabelWithAvailable(optionAvailable)).toEqual(
            `${optionAvailable.name} (${i18n.t(COMMON_AVAILABLE)})`,
        );
    });
    it('should return option.name with COMMON_AVAILABLE if option available is false ', () => {
        const optionUnavailable = { id: 50, name: '1 к. 7 ауд.', available: false };
        expect(getOptionLabelWithAvailable(optionUnavailable)).toEqual(
            `${optionUnavailable.name} (${i18n.t(COMMON_UNAVAILABLE)})`,
        );
    });
});

const department = [
    { id: 1, name: 'Department', disable: false },
    { id: 5, name: 'Department1', disable: false },
];
const expectedDepartment = [
    { id: 1, value: 1, label: 'Department' },
    { id: 5, value: 5, label: 'Department1' },
];

describe('setDepartmentOptions function', () => {
    it('should return array with correct format of department', () => {
        expect(setDepartmentOptions(department)).toEqual(expectedDepartment);
    });
});

const semester = [
    { id: 47, description: '1 2021- 2022' },
    { id: 9, description: '1 2020- 2021' },
];
const expectedSemester = [
    { id: 47, value: 47, label: '1 2021- 2022' },
    { id: 9, value: 9, label: '1 2020- 2021' },
];

describe('setSemesterOptions function', () => {
    it('should return null if semester is undefined', () => {
        const semesterUndefined = undefined;
        expect(setSemesterOptions(semesterUndefined)).toBe(null);
    });
    it('should return array with correct format of semester', () => {
        expect(setSemesterOptions(semester)).toEqual(expectedSemester);
    });
});

const teachers = [
    { id: 65, name: 'Ярослав', patronymic: 'Йосипович', surname: 'Бігун' },
    { id: 69, name: 'Степан', patronymic: 'Григорович', surname: 'Блажевський' },
];
const expectedTeachers = [
    { id: 65, value: 65, label: 'Бігун Я. Й.' },
    { id: 69, value: 69, label: 'Блажевський С. Г.' },
];

describe('setOptions function', () => {
    it('should return array with correct format of teachers', () => {
        expect(setOptions(teachers)).toEqual(expectedTeachers);
    });
});
