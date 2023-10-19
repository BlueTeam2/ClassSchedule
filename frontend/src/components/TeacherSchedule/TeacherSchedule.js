import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { reset } from 'redux-form';

import './TeacherSchedule.scss';

import TeacherScheduleForm from '../TeacherScheduleForm/TeacherScheduleForm';

import { renderTeacherRangeSchedule } from '../../helper/renderScheduleTable';
import { TEACHER_SCHEDULE_FORM } from '../../constants/reduxForms';
import { getTeacherRangeScheduleStart } from '../../actions/schedule';

const TeacherSchedule = (props) => {
    const { schedule, viewTeacherScheduleResults, clearForm, getTeacherRangeSchedule } = props;

    useEffect(() => {
        renderTeacherRangeSchedule(schedule, viewTeacherScheduleResults);
    }, [viewTeacherScheduleResults]);

    return (
        <>
            <TeacherScheduleForm
                onSubmit={getTeacherRangeSchedule}
                onReset={() => clearForm(TEACHER_SCHEDULE_FORM)}
            />
            <section>{renderTeacherRangeSchedule(schedule, viewTeacherScheduleResults)}</section>
        </>
    );
};
const mapStateToProps = (state) => ({
    loading: state.loadingIndicator.loading,
    schedule: state.schedule.teacherRangeSchedule,
    viewTeacherScheduleResults: state.schedule.viewTeacherScheduleResults,
});

const mapDispatchToProps = (dispatch) => ({
    clearForm: (formName) => dispatch(reset(formName)),
    getTeacherRangeSchedule: (values) => dispatch(getTeacherRangeScheduleStart(values)),
});

export default connect(mapStateToProps, mapDispatchToProps)(TeacherSchedule);
