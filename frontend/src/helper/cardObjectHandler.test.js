import { cardObjectHandler } from './cardObjectHandler';

describe('cardObjectHandler function', () => {
    const card = {
        lessonCardId: 12,
        hours: 2,
        subject: { id: 2 },
        type: 'lecture',
        subjectForSite: 'Алгебра',
        teacher: {
            id: 4,
            name: 'Іван',
            surname: 'Блажевський',
            patronymic: 'Іванович',
            position: 'доцент',
        },
        groups: [{ id: 12, title: '123' }],
        grouped: true,
    };
    const link = 'http://youtube.com';
    const semester = { id: 6 };

    it('should return correct values', () => {
        expect(cardObjectHandler(card, semester, link)).toEqual({
            id: card.lessonCardId,
            hours: card.hours,
            subject: { id: card.subject.id },
            lessonType: card.type,
            subjectForSite: card.subjectForSite,
            teacher: card.teacher,
            linkToMeeting: link,
            groups: card.groups,
            grouped: card.grouped,
            semester,
        });
    });
});
