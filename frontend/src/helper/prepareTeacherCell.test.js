import {
    prepareTeacherCardCell,
    buildLessonWithRoom,
    prepareTeacherCardRegularCell,
    buildGroupNumber,
} from './prepareTeacherCell';
import { places } from '../constants/places';

const cardData = {
    room: '1 к. 11 ауд.',
    lessonType: 'testType',
    subjectForSite: 'test',
    group: { title: 'testGroup' },
};

describe('prepareTeacherCardCell function', () => {
    it('should return empty string if card is null or undefined', () => {
        let card = null;
        expect(prepareTeacherCardCell(card)).toEqual('');
        card = undefined;
        expect(prepareTeacherCardCell(card)).toEqual('');
    });
    it('should return string with subject data', () => {
        const card = {
            subjectForSite: 'test subject',
        };
        expect(prepareTeacherCardCell(card)).toEqual('test subject');
    });
});

describe('buildLessonWithRoom function', () => {
    describe('should return string with label', () => {
        it('if place equal AUDITORY', () => {
            expect(buildLessonWithRoom(cardData, places.AUDITORY)).toEqual(
                `test\n(lesson_type_testtype_label, ${cardData.room})\n`,
            );
        });
        it('if place equal ONLINE', () => {
            expect(buildLessonWithRoom(cardData, places.ONLINE)).toEqual(
                `test\nlesson_type_testtype_label\n`,
            );
        });
    });
});

describe('prepareTeacherCardRegularCell function', () => {
    describe('should return string with label and group title', () => {
        it('if place is AUDITORY', () => {
            expect(prepareTeacherCardRegularCell(cardData, places.AUDITORY)).toEqual(
                `test\n(lesson_type_testtype_label, ${cardData.room})\n\n${cardData.group.title}\n`,
            );
        });
        it('if card is null or undefined but place equal ONLINE', () => {
            expect(prepareTeacherCardRegularCell(cardData, places.ONLINE)).toEqual(
                `test\nlesson_type_testtype_label\n\n${cardData.group.title}\n`,
            );
        });
    });
});

describe('buildGroupNumber function', () => {
    it('should return group title', () => {
        expect(buildGroupNumber(cardData)).toEqual(`${cardData.group.title}\n`);
    });
});
