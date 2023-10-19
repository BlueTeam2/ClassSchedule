import { isNil } from 'lodash';
import { places } from '../constants/places';
import { getTeacherWithShortPosition } from './renderTeacher';
import i18n from '../i18n';

export const prepareLessonCardCell = (card) => {
    let inner = '';
    if (!isNil(card)) {
        inner = `${getTeacherWithShortPosition(card.teacher)}\n${card.subjectForSite}\n`;
    }
    return inner;
};

export const prepareLessonSubCardCell = (card, place) => {
    let inner = '';
    if (!isNil(card)) {
        const room = place !== places.ONLINE ? card.room : '';
        inner = i18n.t(`formElements:lesson_type_${card.lessonType.toLowerCase()}_label`);
        if (room !== '') {
            inner = `(${inner}, ${card.room.name})`;
        }
    }
    return inner;
};
