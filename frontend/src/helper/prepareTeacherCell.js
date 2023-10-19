import { isNil } from 'lodash';
import { places } from '../constants/places';
import {
    COMMON_REGULAR_LESSON_LABEL,
    COMMON_VACATION_LABEL,
} from '../constants/translationLabels/common';
import i18n from '../i18n';
import { getTeacherFullName } from './renderTeacher';

export const prepareTeacherCardCell = (card) => {
    let inner = '';
    if (!isNil(card)) {
        inner = card.subjectForSite;
    }
    return inner;
};

export const buildLessonWithRoom = (card, place) => {
    const room = place !== places.ONLINE ? card.room : '';
    let inner = '';
    inner += `${prepareTeacherCardCell(card)}\n`;
    inner +=
        room !== ''
            ? `(${i18n.t(`formElements:lesson_type_${card.lessonType.toLowerCase()}_label`)}, ${
                  card.room
              })\n`
            : `${i18n.t(`formElements:lesson_type_${card.lessonType.toLowerCase()}_label`)}\n`;
    return inner;
};

export const prepareTeacherCardRegularCell = (card, place) => {
    let inner = buildLessonWithRoom(card, place);
    inner += `\n${card.group.title}\n`;
    return inner;
};

export const buildGroupNumber = (card) => {
    return `${card.group.title}\n`;
};

export const prepareTitleAndInner = (options) => {
    const { cards, place } = options;
    let { title, inner } = options;
    cards.forEach((cardItem) => {
        const { temporary_schedule: tempSchedule } = cardItem;

        if (!tempSchedule) {
            inner += buildGroupNumber(cardItem);
        } else {
            const { vacation, date, teacher, room, subjectForSite } = tempSchedule;

            const roomLabel = room ? `${subjectForSite}, ${room.name}\n` : `${subjectForSite}\n`;
            inner += vacation
                ? `${date}\n${i18n.t(COMMON_VACATION_LABEL)}\n`
                : `${date}\n${getTeacherFullName(teacher)}\n${roomLabel}`;
        }
        title += `${i18n.t(COMMON_REGULAR_LESSON_LABEL)}\r${prepareTeacherCardRegularCell(
            cardItem,
            place,
        )}\r`;
    });
    return { title, inner };
};
