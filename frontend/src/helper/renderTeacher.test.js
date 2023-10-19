import {
    getFirstLetter,
    getTeacherName,
    getTeacherFullName,
    getTeacherForSite,
    handleTeacherInfo,
    getTeacherWithPosition,
    getTeacherWithShortPosition,
    parseShortPosition,
} from './renderTeacher';

const teacher = {
    name: 'Олександр',
    patronymic: 'Петрович',
    position: 'Професор',
    surname: 'Рурський',
};

describe('behavior of parseShortPosition function', () => {
    test('it shows short teachers position', () => {
        expect(parseShortPosition(teacher.position)).toBe('проф.');
    });
});

describe('behavior of getFirstLetter function', () => {
    test('it shows first letter if word exist', () => {
        expect(getFirstLetter('Іліка')).toBe('І.');
    });
    test('it shows empty string if word equal null', () => {
        expect(getFirstLetter(null)).toBe('');
    });
});

describe('behavior of getTeacherName function', () => {
    test('it shows surname with initials', () => {
        expect(getTeacherName(teacher)).toBe('Рурський О. П.');
    });
});

describe('behavior of getTeacherFullName function', () => {
    test('it shows teachers full name', () => {
        expect(getTeacherFullName(teacher)).toBe('Рурський Олександр Петрович');
    });
});

describe('behavior of getTeacherForSite function', () => {
    test('it shows teachers position and surname with initials', () => {
        expect(getTeacherForSite(teacher)).toBe('Професор Рурський О. П.\n');
    });
});

describe('behavior of handleTeacherInfo function', () => {
    test('it shows teachers full name and position', () => {
        expect(handleTeacherInfo(teacher)).toBe('Рурський Олександр Петрович (Професор)');
    });
});

describe('behavior of getTeacherWithPosition function', () => {
    test('it shows teachers position and full name', () => {
        expect(getTeacherWithPosition(teacher)).toBe('Професор Рурський Олександр Петрович');
    });
});

describe('behavior of getTeacherWithShortPosition function', () => {
    test('it shows teachers short position and full name', () => {
        expect(getTeacherWithShortPosition(teacher)).toBe('проф. Рурський Олександр Петрович');
    });
});
