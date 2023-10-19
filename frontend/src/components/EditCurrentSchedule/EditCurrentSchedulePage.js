import React, { useEffect, useState } from 'react';
import { CircularProgress } from '@material-ui/core';
import { useTranslation } from 'react-i18next';
import './EditCurrentSchedule.scss';
import ScheduleLessonsList from '../../containers/EditCurrentSchedule/ScheduleLessonsList';
import Schedule from '../../containers/EditCurrentSchedule/Schedule';
import { SCHEDULE_TITLE, USE_PC } from '../../constants/translationLabels/common';
import { EDIT_SCHEDULE_MIN_WINDOW_SIZE } from '../../constants/windowSizes';

const SchedulePage = (props) => {
    const {
        groupId,
        itemGroupId,
        scheduleItems,
        getEnabledGroups,
        scheduleLoading,
        setScheduleLoading,
        getAllLessonsByGroup,
        getAllScheduleItems,
        clearScheduleItems,
        getClassScheduleList,
        currentSemester,
        getListOfRooms,
    } = props;
    const { t } = useTranslation('common');
    document.title = t(SCHEDULE_TITLE);
    const [dragItemData, setDragItemData] = useState({});
    const [isHiddenSchedule, setIsHiddenSchedule] = useState(false);
    const days = currentSemester.semester_days || [];
    const classes = currentSemester.semester_classes || [];
    const allLessons = [];

    const handleResize = () => {
        if (window.innerWidth < EDIT_SCHEDULE_MIN_WINDOW_SIZE) {
            setIsHiddenSchedule(true);
        } else {
            setIsHiddenSchedule(false);
        }
    };

    useEffect(() => {
        window.addEventListener('resize', handleResize);
        handleResize();
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    days.forEach((day, outerIndex) => {
        classes.forEach((classNumber, index) => {
            allLessons.push(
                {
                    dayName: day,
                    classId: classNumber.id,
                    className: classNumber.class_name,
                    week: 'ODD',
                    id: `${index}-${outerIndex}`,
                },
                {
                    dayName: day,
                    classId: classNumber.id,
                    className: classNumber.class_name,
                    week: 'EVEN',
                    id: `${index}-${outerIndex}`,
                },
            );
        });
    });

    useEffect(() => {
        setScheduleLoading(true);
        getAllScheduleItems();
        getEnabledGroups();
        getListOfRooms();
        getClassScheduleList();
    }, []);

    useEffect(() => {
        if (groupId) {
            getAllLessonsByGroup(groupId);
        }
    }, [groupId]);

    const handleClearSchedule = () => {
        if (currentSemester.id) {
            clearScheduleItems(currentSemester.id);
            if (groupId) {
                getAllLessonsByGroup(groupId);
            }
        }
    };
    if (isHiddenSchedule) {
        return (
            <section className="for-phones-and-tablets schedule-card">
                <h1>{t(USE_PC)}</h1>
            </section>
        );
    }
    return (
        <>
            <section className="schedule-control-panel">
                <section className="schedule-card schedule-table ">
                    {scheduleLoading ? (
                        <CircularProgress className="loading-circle" />
                    ) : (
                        <>
                            <Schedule
                                dragItemData={dragItemData}
                                groupId={groupId}
                                currentSemester={currentSemester}
                                itemGroupId={itemGroupId}
                                items={scheduleItems}
                                t={t}
                                allLessons={allLessons}
                            />
                        </>
                    )}
                </section>
                <aside className="schedule-card lesson-list">
                    <ScheduleLessonsList
                        setDragItemData={setDragItemData}
                        items={scheduleItems}
                        handleClearSchedule={handleClearSchedule}
                        groupId={groupId}
                        t={t}
                        classScheduler={currentSemester.semester_classes}
                    />
                </aside>
            </section>
        </>
    );
};

export default SchedulePage;
