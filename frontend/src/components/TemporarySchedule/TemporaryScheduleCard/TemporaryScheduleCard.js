import React from 'react';
import { useTranslation } from 'react-i18next';
import {
    GROUP_LABEL,
    ROOM_LABEL,
    SUBJECT_LABEL,
    TEACHER_LABEL,
} from '../../../constants/translationLabels/formElements';
import { COMMON_CLASS_SCHEDULE, SEMESTER_LABEL } from '../../../constants/translationLabels/common';

const TemporaryScheduleCard = (props) => {
    const { t } = useTranslation('formElements');
    const { schedule } = props;

    return (
        <>
            <p>
                {t(SUBJECT_LABEL)}:{' '}
                <b>
                    {schedule.lesson ? schedule.lesson.subjectForSite : schedule.subjectForSite}(
                    {schedule.lesson ? schedule.lesson.lessonType : schedule.lessonType})
                </b>
            </p>
            <p>
                {t(ROOM_LABEL)}: <b>{schedule.room.name}</b>
            </p>
            <p>
                {t(TEACHER_LABEL)}:{' '}
                <b>{schedule.lesson ? schedule.lesson.teacherForSite : schedule.teacherForSite}</b>
            </p>
            <p>
                {t(COMMON_CLASS_SCHEDULE)}:{' '}
                <b>
                    {schedule.class.startTime} - {schedule.class.endTime}
                </b>
            </p>
            <p>
                {t(GROUP_LABEL)}:{' '}
                <b>{schedule.lesson ? schedule.lesson.group.title : schedule.group.title}</b>
            </p>
            <p>
                {t(SEMESTER_LABEL)}:{' '}
                <b>
                    {schedule.lesson
                        ? schedule.lesson.semester.description
                        : schedule.semester.description}
                </b>
            </p>
        </>
    );
};

export default TemporaryScheduleCard;
