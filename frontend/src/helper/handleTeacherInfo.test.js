import { handleTeacherInfo } from './handleTeacherInfo';

describe('handleTeacherInfo function', () => {
    it('should return teacher full info', () => {
        const teacher = {
            surname: 'Іванов',
            name: 'Петро',
            patronymic: 'Іванович',
            position: 'доцент',
        };
        expect(handleTeacherInfo(teacher)).toBe('Іванов Петро Іванович(доцент)');
    });
});
