import React, { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import './GroupSchedulePage.scss';
import { CircularProgress } from '@material-ui/core';
import { get } from 'lodash';
import { getDataFromParams } from '../../utils/urlUtils';
import GroupSchedulePageTop from './GroupSchedulePageTop/GroupSchedulePageTop';
import { SCHEDULE_FOR_LINK } from '../../constants/links';
import { renderSchedule } from '../../helper/renderSchedule';
import { getScheduleType } from '../../helper/getScheduleType';

const createSubmitValues = (semester, group, teacher) => ({
    semester,
    group: { id: group },
    teacher: { id: teacher },
});

const GroupSchedulePage = (props) => {
    const history = useHistory();
    const location = useLocation();
    const { t } = useTranslation('common');
    const {
        defaultSemester,
        scheduleType,
        loading,
        getDefaultSemester,
        getGroupSchedule,
        getTeacherSchedule,
        getFullSchedule,
    } = props;

    useEffect(() => {
        getDefaultSemester();
    }, []);

    const scheduleActions = {
        group: (values) => {
            const { semester, group } = values;
            getGroupSchedule(semester.id, group.id);
        },
        teacher: (values) => {
            const { semester, teacher } = values;
            getTeacherSchedule(semester.id, teacher.id);
        },
        full: (values) => {
            const { semester } = values;
            getFullSchedule(semester.id);
        },
    };

    const handleSubmit = (values) => {
        const { semester, group, teacher } = values;
        const groupPath = get(group, 'id') ? `&group=${group.id}` : '';
        const teacherPath = get(teacher, 'id') ? `&teacher=${teacher.id}` : '';
        const typeOfSchedule = getScheduleType(values);
        scheduleActions[typeOfSchedule](values);
        history.push(`${SCHEDULE_FOR_LINK}?semester=${semester.id}${groupPath}${teacherPath}`);
    };

    const getSchedule = () => {
        const { semester, group, teacher } = getDataFromParams(location);

        if (!semester) {
            handleSubmit(createSubmitValues(defaultSemester, group, teacher));
        } else {
            handleSubmit(createSubmitValues({ id: Number(semester) }, group, teacher));
        }
    };

    useEffect(() => {
        if (defaultSemester.id) {
            getSchedule();
        }
    }, [defaultSemester]);

    const getTop = () =>
        scheduleType !== 'archived' && (
            <GroupSchedulePageTop scheduleType={scheduleType} handleSubmit={handleSubmit} />
        );

    return (
        <>
            {getTop()}
            {loading ? (
                <section className="centered-container">
                    <CircularProgress />
                </section>
            ) : (
                renderSchedule({ ...props, t })
            )}
        </>
    );
};
export default GroupSchedulePage;
