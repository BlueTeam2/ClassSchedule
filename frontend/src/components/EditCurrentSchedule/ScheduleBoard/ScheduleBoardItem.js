import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

import { MdGroup } from 'react-icons/md';

import { IoMdMore } from 'react-icons/all';

import Card from '@material-ui/core/Card';
import {
    FORM_GROUPED_LABEL,
    FORM_HOURS_LABEL,
} from '../../../constants/translationLabels/formElements';
import {
    COMMON_EDIT,
    COMMON_DELETE_HOVER_TITLE,
} from '../../../constants/translationLabels/common';
import { actionType } from '../../../constants/actionTypes';
import { getTeacherName } from '../../../helper/renderTeacher';

const ScheduleItem = (props) => {
    const {
        deleteScheduleItem,
        checkRoomAvailability,
        itemData,
        getLessonsByGroupId,
        selectByGroupId,
        t,
        openDialogWithData,
    } = props;
    const [anchorEl, setAnchorEl] = useState(null);
    const { lesson, room } = itemData;


    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const handelEdit = () => {
        const { group, semesterId } = lesson;
        const { id, period, dayOfWeek, evenOdd } = itemData;
        const editObj = {
            id,
            dayOfWeek,
            periodId: period.id,
            evenOdd,
            semesterId: semesterId,

        };
        checkRoomAvailability(editObj);
        selectByGroupId(group.id);
        openDialogWithData({ type: actionType.UPDATED, item: editObj, groupId: group.id });
        getLessonsByGroupId(group.id);
        handleClose();
    };
    const handelDelete = () => {
        const { group } = lesson;
        deleteScheduleItem(itemData.id);
        selectByGroupId(group.id);
        getLessonsByGroupId(group.id);
        handleClose();
    };

    return (
        <Card className="schedule-item">
            <Button
                aria-controls="simple-menu"
                className="schedule-item-menu"
                onClick={handleClick}
            >
                <IoMdMore title="more" className="svg-btn delete-btn" />
            </Button>
            <Menu
                className="action-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem className="edit-item" onClick={handelEdit}>
                    {t(COMMON_EDIT)}
                </MenuItem>
                <MenuItem className="delete-item" onClick={handelDelete}>
                    {t(COMMON_DELETE_HOVER_TITLE)}
                </MenuItem>
            </Menu>
            <h5 className="lesson-title">
                {lesson.subjectForSite} (
                {t(`formElements:lesson_type_${lesson.lessonType.toLowerCase()}_label`)})
            </h5>
            <p className="teacher-name">{getTeacherName(lesson.teacher)}</p>
            {lesson.grouped && (
                <MdGroup
                    title={t(FORM_GROUPED_LABEL)}
                    className="svg-btn copy-btn grouped-icon align-left info-btn"
                />
            )}
            <p className="lesson-duration">
                <b>{room.name}</b>
            </p>
        </Card>
    );
};

export default ScheduleItem;
