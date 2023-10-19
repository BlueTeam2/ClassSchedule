import { prepareLessonCardCell, prepareLessonSubCardCell } from './prepareLessonCell';
import { places } from '../constants/places';

describe('prepareLessonCardCell function', () => {
    it('should return empty string if card is null or undefined', () => {
        let card = null;
        expect(prepareLessonCardCell(card)).toEqual('');
        card = undefined;
        expect(prepareLessonCardCell(card)).toEqual('');
    });
    it('should return string with subject data', () => {
        const teacher = {
            name: 'Роман',
            surname: 'Романюк',
            patronymic: 'Романович',
            position: 'доцент',
        };
        const card = {
            subjectForSite: 'test subject',
            teacher,
        };
        expect(prepareLessonCardCell(card)).toEqual(
            `доц. ${teacher.surname} ${teacher.name} ${teacher.patronymic}\n${card.subjectForSite}\n`,
        );
    });
});

describe('prepareLessonSubCardCell function', () => {
    const cardData = {
        room: { id: 51, name: '1 к. 11 ауд.' },
        lessonType: 'testType',
    };
    describe('should return empty string', () => {
        it('if card is null or undefined', () => {
            let card = null;
            expect(prepareLessonSubCardCell(card, places.AUDITORY)).toEqual('');
            card = undefined;
            expect(prepareLessonSubCardCell(card, places.AUDITORY)).toEqual('');
        });
        it('if card is null or undefined but place equal ONLINE', () => {
            let card = null;
            expect(prepareLessonSubCardCell(card, places.ONLINE)).toEqual('');
            card = undefined;
            expect(prepareLessonSubCardCell(card, places.ONLINE)).toEqual('');
        });
    });
    describe('should return string with data', () => {
        it('if card is not null and place not equal ONLINE', () => {
            expect(prepareLessonSubCardCell(cardData, places.AUDITORY)).toEqual(
                `(lesson_type_${cardData.lessonType.toLowerCase()}_label, ${cardData.room.name})`,
            );
        });
        it('if card is not null and place equal ONLINE', () => {
            expect(prepareLessonSubCardCell(cardData, places.ONLINE)).toEqual(
                `lesson_type_${cardData.lessonType.toLowerCase()}_label`,
            );
        });
    });
});
