import React, { useState, useEffect } from 'react';
import { isEmpty } from 'lodash';
import { setLoadingService } from '../../../services/loadingService';
import ScheduleBoardItem from '../../../containers/EditCurrentSchedule/ScheduleBoardItem';
import { actionType } from '../../../constants/actionTypes';
import './ScheduleBoard.scss';

const ScheduleBoard = (props) => {
    const {
        lesson,
        isSelectedGroup,
        dragItemData,
        additionClassName,
        currentSemester,
        groupId,
        scheduleItems,
        openDialogWithData,
        t,
        checkScheduleItemAvailability,
    } = props;

    const { classId, dayName, week } = lesson;
    const [className, setClassName] = useState('');
    const [boardFill, setBoardFill] = useState({});
    useEffect(() => {
        setBoardFill(
            scheduleItems.find(
                (item) =>
                    item.lesson.group.id === groupId &&
                    item.period.id === classId &&
                    item.dayOfWeek === dayName &&
                    item.evenOdd === week,
            ),
        );
    }, [scheduleItems]);

    const changeBoardStyle = (selectedClassName, notSelectedClassName, callBack = () => {}) => {
        if (!isSelectedGroup) {
            setClassName(notSelectedClassName);
        } else {
            setClassName(selectedClassName);
            callBack();
        }
    };

    useEffect(() => {
        changeBoardStyle('possible', '');
    }, [isSelectedGroup]);

    const drop = (e) => {
        if (!isSelectedGroup) {
            e.preventDefault();
            return;
        }
        const AddObj = {
            id: dragItemData.id,
            lessonId: dragItemData.id,
            dayOfWeek: dayName,
            periodId: classId,
            evenOdd: week,
            semesterId: currentSemester.id,
        };
        checkScheduleItemAvailability(AddObj);
        setClassName('possible');
        setLoadingService(true);
        openDialogWithData({ type: actionType.ADD, item: AddObj, groupId });
    };
    const dragOver = (e) => {
        changeBoardStyle('allow', 'not-allow', () => e.preventDefault());
    };
    const dragLeave = (e) => {
        changeBoardStyle('possible', '', () => e.preventDefault());
    };

    return (
        <>
            {!isEmpty(boardFill) ? (
                <ScheduleBoardItem
                    itemData={boardFill}
                    t={t}
                    openDialogWithData={openDialogWithData}
                />
            ) : (
                <div
                    className={`board ${additionClassName} ${className}`}
                    onDrop={drop}
                    onDragOver={dragOver}
                    onDragLeave={dragLeave}
                >
                    <p>{t(`week_${week.toLowerCase()}_title`)}</p>
                </div>
            )}
        </>
    );
};

export default ScheduleBoard;
