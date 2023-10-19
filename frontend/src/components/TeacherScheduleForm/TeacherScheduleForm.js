import React, { useEffect } from 'react';
import { Field, reduxForm } from 'redux-form';

import { connect } from 'react-redux';

import './TeacherScheduleForm.scss';

import { MdPlayArrow, MdViewModule, MdViewHeadline } from 'react-icons/md';

import Button from '@material-ui/core/Button';

import { useTranslation } from 'react-i18next';
import * as moment from 'moment';
import { required, lessThanDate, greaterThanDate } from '../../validation/validateFields';

import { TEACHER_SCHEDULE_FORM } from '../../constants/reduxForms';
import renderMonthPicker from '../../share/renderedFields/timeSemester';
import Card from '../../share/Card/Card';
import { CLASS_FROM_LABEL, CLASS_TO_LABEL } from '../../constants/translationLabels/formElements';
import {
    COMMON_LIST_VIEW,
    COMMON_SELECT_DATES_FOR_TEACHERS_SCHEDULE,
    COMMON_BLOCK_VIEW,
    FULL_SCHEDULE_LABEL,
} from '../../constants/translationLabels/common';
import { dateFormat } from '../../constants/formats';
import { setTeacherViewType } from '../../actions';

const TeacherScheduleForm = (props) => {
    const { t } = useTranslation('formElements');
    const { handleSubmit, initialize, setTypeOfTeacherView } = props;
    const isSchedule = false;
    // TODO check if it`s necessary here
    useEffect(() => {
        initialize({
            startDay: moment(new Date(), dateFormat).format(dateFormat),
            endDay: moment(new Date(), dateFormat).add(7, 'd').format(dateFormat),
        });
    }, [isSchedule]);
    return (
        <Card additionClassName="form-card teacher-schedule-form">
            <form onSubmit={handleSubmit}>
                <div className="form-line-block">
                    <h2 className="form-title">{t(COMMON_SELECT_DATES_FOR_TEACHERS_SCHEDULE)}</h2>
                    <Field
                        className="time-input"
                        name="startDay"
                        component={renderMonthPicker}
                        label={`${t(CLASS_FROM_LABEL)}:`}
                        validate={[required, lessThanDate]}
                    />
                    <Field
                        className="time-input"
                        name="endDay"
                        component={renderMonthPicker}
                        label={`${t(CLASS_TO_LABEL)}:`}
                        validate={[required, greaterThanDate]}
                    />
                    <div className="form-buttons-container">
                        <Button
                            className="buttons-style"
                            type="submit"
                            variant="contained"
                            color="primary"
                        >
                            <MdPlayArrow title={t(FULL_SCHEDULE_LABEL)} className="svg-btn" />
                        </Button>
                        <Button
                            className="view-type-icon first-view-type-button"
                            variant="contained"
                            color="secondary"
                            title={t(COMMON_BLOCK_VIEW)}
                            onClick={() => setTypeOfTeacherView('blocks-view')}
                        >
                            <MdViewModule className="svg-btn" />
                        </Button>
                        <Button
                            className="view-type-icon"
                            variant="contained"
                            color="secondary"
                            title={t(COMMON_LIST_VIEW)}
                            onClick={() => setTypeOfTeacherView('list-view')}
                        >
                            <MdViewHeadline className="svg-btn" />
                        </Button>
                    </div>
                </div>
            </form>
        </Card>
    );
};

const mapDispatchToProps = (dispatch) => ({
    setTypeOfTeacherView: (type) => dispatch(setTeacherViewType(type)),
});

export default connect(
    null,
    mapDispatchToProps,
)(
    reduxForm({
        form: TEACHER_SCHEDULE_FORM,
    })(TeacherScheduleForm),
);
