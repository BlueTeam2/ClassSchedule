import { isNil } from 'lodash';
import React from 'react';
import {
    COMMON_REGULAR_LESSON_LABEL,
    COMMON_VACATION_LABEL,
} from '../../constants/translationLabels/common';
import {
    buildLessonWithRoom,
    prepareTeacherCardRegularCell,
    prepareTitleAndInner,
} from '../../helper/prepareTeacherCell';
import { setLink } from '../../helper/setLInk';
import i18n from '../../i18n';

const TeacherTemporaryCardCell = (props) => {
    const { cards, place } = props;
    if (!cards) {
        return '';
    }
    let inner = '';
    let title = '';

    if (cards.length === 1) {
        if (isNil(cards[0])) {
            return '';
        }

        const card = cards[0];
        const { temporary_schedule: tempSchedule, linkToMeeting } = card;

        const meetingLink = linkToMeeting && setLink(card, place);

        if (!tempSchedule) {
            return (
                <>
                    {prepareTeacherCardRegularCell(card, place)}
                    {meetingLink}
                </>
            );
        }

        const { date, room, vacation, subjectForSite } = tempSchedule;
        const roomLabel = tempSchedule.room
            ? `(${subjectForSite}, ${room.name})\n`
            : `${subjectForSite}\n`;

        inner = `${date}\n`;
        inner += vacation ? `${i18n.t(COMMON_VACATION_LABEL)}` : `${roomLabel}`;

        title = `${i18n.t(COMMON_REGULAR_LESSON_LABEL)}\r${prepareTeacherCardRegularCell(
            card,
            place,
        )}`;
        return (
            <p className="temporary-class" title={title}>
                {inner}
                {meetingLink}
            </p>
        );
    }
    const card = cards[0];

    inner += buildLessonWithRoom(card, place);

    const { title: resTitle, inner: resInner } = prepareTitleAndInner({
        title,
        inner,
        cards,
        place,
    });
    return (
        <p className="temporary-class" title={resTitle}>
            {resInner}
            {card.linkToMeeting && setLink(card, place)}
        </p>
    );
};

export default TeacherTemporaryCardCell;
