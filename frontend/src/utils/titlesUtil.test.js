import { getGroupScheduleTitle, getSemesterTitle, getTeacherScheduleTitle } from './titlesUtil';

const semester = {
    id: 1,
    description: '1 2020- 2021',
    endDay: '31/12/2020',
    startDay: '31/08/2020',
};

const semesterTitle = '1 2020- 2021 (31/08/2020-31/12/2020) : ';

describe('getSemesterTitle function', () => {
    it('should return semester title', () => {
        expect(getSemesterTitle(semester)).toBe(semesterTitle);
    });

    it('should return empty string if no semester', () => {
        expect(getSemesterTitle(!semester)).toBe('');
    });
});

describe('getGroupScheduleTitle function', () => {
    it('should return groupScheduleTitle', () => {
        const group = {
            id: 34,
            title: '341',
        };

        expect(getGroupScheduleTitle(semester, group)).toBe(semesterTitle + group.title);
    });

    it('should return semesterTitle if no group', () => {
        expect(getGroupScheduleTitle(semester)).toBe(semesterTitle);
    });
});

describe('getTeacherScheduleTitle function', () => {
    const teacher = {
        name: 'Олександр',
        patronymic: 'Петрович',
        position: 'Професор',
        surname: 'Рурський',
    };

    it('should return teacherScheduleTitle', () => {
        const teacherTitle = 'Професор Рурський Олександр Петрович';
        expect(getTeacherScheduleTitle(semester, teacher)).toBe(semesterTitle + teacherTitle);
    });

    it('should return semesterTitle if no teacher', () => {
        expect(getTeacherScheduleTitle(semester)).toBe(semesterTitle);
    });
});
