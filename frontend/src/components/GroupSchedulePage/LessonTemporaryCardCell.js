import React from 'react';
import { isNil } from 'lodash';
import {
    COMMON_REGULAR_LESSON_LABEL,
    COMMON_VACATION_LABEL,
} from '../../constants/translationLabels/common';
import { getTeacherForSite } from '../../helper/renderTeacher';
import { setLink } from '../../helper/setLInk';
import i18n from '../../i18n';
import { prepareLessonCardCell, prepareLessonSubCardCell } from '../../helper/prepareLessonCell';

const LessonTemporaryCardCell = (props) => {
    const { card, place, day } = props;
    if (isNil(card)) return '';
    const { temporary_schedule: tempSchedule, linkToMeeting } = card;

    const meetingLink = linkToMeeting && setLink(card, place);

    if (tempSchedule) {
        const { vacation, date, subjectForSite, room } = tempSchedule;
        const roomLabel = room ? `, ${room.name}` : '';
        let inner = `${date}\n\r`;

        inner += vacation
            ? `${i18n.t(COMMON_VACATION_LABEL)}`
            : `${getTeacherForSite(tempSchedule)}\n${subjectForSite}${roomLabel}`;

        const title = `${i18n.t(COMMON_REGULAR_LESSON_LABEL)}\r${prepareLessonCardCell(
            card,
        )}\r${prepareLessonSubCardCell(card, place)}\r`;

        return (
            <>
                <p className="temporary-class" title={title}>
                    {inner}
                </p>
                {meetingLink}
            </>
        );
    }

    return (
        <>
            <p title={i18n.t(`common:day_of_week_${day}`)}>{prepareLessonCardCell(card)}</p>
            <p>{prepareLessonSubCardCell(card, place)}</p>
            {meetingLink}
        </>
    );
};

export default LessonTemporaryCardCell;
