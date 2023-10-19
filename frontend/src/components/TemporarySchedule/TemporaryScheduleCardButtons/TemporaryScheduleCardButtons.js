import React from 'react';
import { FaEdit, MdDelete } from 'react-icons/all';
import { useTranslation } from 'react-i18next';
import { selectTemporaryScheduleService } from '../../../services/temporaryScheduleService';
import {
    COMMON_EDIT_HOVER_TITLE,
    COMMON_DELETE_HOVER_TITLE,
} from '../../../constants/translationLabels/common';

const TemporaryScheduleCardButtons = (props) => {
    const { t } = useTranslation('common');

    const { schedule, date, isTemporary, scheduleId } = props;
    const { onOpenDialog, setDate, setTeacherId } = props;

    const selectTemporarySchedule = (scheduleData) => {
        selectTemporaryScheduleService({
            ...scheduleData.lesson,
            room: scheduleData.room,
            class: scheduleData.class,
            id: scheduleData.id,
            vacation: scheduleData.vacation,
            scheduleId: scheduleData.scheduleId ? scheduleData.scheduleId : scheduleId,
            date: scheduleData.date,
        });
    };

    const handleScheduleSelect = (scheduleData) => {
        const resultSchedule = scheduleData;
        resultSchedule.scheduleId = scheduleData.id;
        resultSchedule.id = null;
        resultSchedule.lesson.id = null;
        selectTemporarySchedule(resultSchedule);
    };

    const handleEditClick = () => {
        if (isTemporary) selectTemporarySchedule({ ...schedule, date });
        else {
            handleScheduleSelect({
                ...schedule,
                date,
            });
        }
    };

    return (
        <div className="cards-btns">
            <FaEdit
                title={t(COMMON_EDIT_HOVER_TITLE)}
                className="svg-btn edit-btn"
                onClick={handleEditClick}
            />
            {isTemporary && (
                <MdDelete
                    title={t(COMMON_DELETE_HOVER_TITLE)}
                    className="svg-btn delete-btn"
                    onClick={() => {
                        onOpenDialog(schedule.id);
                        setDate(date);
                        setTeacherId(schedule.lesson.teacher.id);
                    }}
                />
            )}
        </div>
    );
};

export default TemporaryScheduleCardButtons;
