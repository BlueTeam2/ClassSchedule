import { Button } from '@material-ui/core';
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { MdPlayArrow } from 'react-icons/md';
import { TEACHER_SCHEDULE_LABEL } from '../../constants/translationLabels/common';
import GroupsList from '../../containers/GroupSchedulePage/GroupsList';
import SemestersList from '../../containers/GroupSchedulePage/SemestersList';
import TeachersList from '../../containers/GroupSchedulePage/TeachersList';

const SchedulePageForm = (props) => {
    const {
        handleSubmit,
        handleFormSubmit,
        pristine,
        submitting,
        change,
        initialize,
        semester,
        group,
        teacher,
    } = props;
    const { t } = useTranslation('common');

    useEffect(() => {
        initialize({ semester, group, teacher });
    }, [semester, group, teacher]);

    return (
        <form onSubmit={handleSubmit} className="schedule-form">
            <SemestersList handleSubmit={handleFormSubmit} />
            <GroupsList handleChange={change} />
            <TeachersList handleChange={change} />
            <Button
                variant="contained"
                color="primary"
                type="submit"
                disabled={pristine || submitting}
                className="schedule-form_submit"
            >
                <MdPlayArrow title={t(TEACHER_SCHEDULE_LABEL)} className="svg-btn" />
            </Button>
        </form>
    );
};

export default SchedulePageForm;
