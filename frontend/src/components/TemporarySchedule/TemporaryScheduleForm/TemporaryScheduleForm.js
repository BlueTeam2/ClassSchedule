import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Field, reduxForm } from 'redux-form';

import Button from '@material-ui/core/Button';
import Hidden from '@material-ui/core/Hidden';
import { TEMPORARY_SCHEDULE_FORM } from '../../../constants/reduxForms';

import Card from '../../../share/Card/Card';
import renderCheckboxField from '../../../share/renderedFields/checkbox';
import SelectField from '../../../share/renderedFields/select';
import renderTextField from '../../../share/renderedFields/input';

import { handleTeacherInfo } from '../../../helper/renderTeacher';
import {
    setValueToSubjectForSiteHandler,
    setValueToTeacherForSiteHandler,
} from '../../../helper/reduxFormHelper';

import { maxLengthValue, required } from '../../../validation/validateFields';

import { selectTemporaryScheduleService } from '../../../services/temporaryScheduleService';
import {
    SAVE_BUTTON_LABEL,
    GROUP_LABEL,
    ROOM_LABEL,
    SUBJECT_LABEL,
    CLEAR_BUTTON_LABEL,
    CLASS_LABEL,
    FOR_SITE_LABEL,
    TEACHER_LABEL,
    EDIT_TEMPORARY_SCHEDULE_FORM,
    CREATE_TEMPORARY_SCHEDULE_FORM,
} from '../../../constants/translationLabels/formElements';
import {
    COMMON_NOTIFY_LABEL,
    COMMON_HOLIDAY_LABEL,
    TYPE_LABEL,
} from '../../../constants/translationLabels/common';

const TemporaryScheduleForm = (props) => {
    const { t } = useTranslation('formElements');
    const {
        handleSubmit,
        invalid,
        reset,
        submitting,
        temporarySchedule,
        teachers,
        periods,
        rooms,
        subjects,
        lessonTypes,
        groups,
        initialize,
    } = props;
    const [isVacation, setIsVacation] = useState(false);
    const [notify, setNotify] = useState(false);

    const scheduleId = temporarySchedule?.scheduleId;
    const temporaryScheduleId = temporarySchedule?.id;

    const initializeFormHandler = (temporaryScheduleData) => {
        setIsVacation(temporaryScheduleData.vacation);
        initialize({
            vacation: isVacation,
            teacher: temporaryScheduleData.teacher.id,
            semester: temporaryScheduleData.semester.id,
            subject: temporaryScheduleData.subject.id,
            group: temporaryScheduleData.group.id,
            room: temporaryScheduleData.room.id,
            period: temporaryScheduleData.class.id,
            lessonType: temporaryScheduleData.lessonType,
            teacherForSite: temporaryScheduleData.teacherForSite,
            subjectForSite: temporaryScheduleData.subjectForSite,
            date: temporaryScheduleData.date,
            id: temporaryScheduleData.id,
            scheduleId: temporaryScheduleData.scheduleId,
        });
    };

    useEffect(() => {
        if (temporaryScheduleId || scheduleId) {
            initializeFormHandler(temporarySchedule);
        } else {
            initialize({ vacation: isVacation });
        }
    }, [temporaryScheduleId]);

    const handleVacationChange = (event) => setIsVacation(event.target.checked);
    const handleNotifyChange = (event) => setNotify(event.target.checked);

    return (
        <Card additionClassName="form-card">
            <>
                <h2 className="form-title under-line">
                    {!temporarySchedule.scheduleId
                        ? t(EDIT_TEMPORARY_SCHEDULE_FORM)
                        : t(CREATE_TEMPORARY_SCHEDULE_FORM)}
                </h2>
                <form onSubmit={handleSubmit}>
                    <Field
                        name="vacation"
                        label={t(COMMON_HOLIDAY_LABEL)}
                        component={renderCheckboxField}
                        checked={isVacation}
                        onChange={handleVacationChange}
                        color="primary"
                    />
                    <Field
                        name="teacher"
                        className="form-field"
                        component={SelectField}
                        label={t(TEACHER_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                        onChange={(event) => {
                            if (event.target.value)
                                setValueToTeacherForSiteHandler(
                                    teachers,
                                    event.target.value,
                                    props.change,
                                );
                            else props.change('teacherForSite', '');
                        }}
                    >
                        <option value="" />
                        {teachers.map((teacher) => (
                            <option value={teacher.id} key={teacher.id}>
                                {handleTeacherInfo(teacher)}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="subject"
                        className="form-field"
                        component={SelectField}
                        label={t(SUBJECT_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                        onChange={(event) => {
                            setValueToSubjectForSiteHandler(
                                subjects,
                                event.target.value,
                                props.change,
                            );
                        }}
                    >
                        <option value="" />
                        {subjects.map((subject) => (
                            <option key={subject.id} value={subject.id}>
                                {subject.name}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="group"
                        className="form-field"
                        component={SelectField}
                        label={t(GROUP_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                    >
                        <option value="" />
                        {groups.map((group) => (
                            <option key={group.id} value={group.id}>
                                {group.title}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="lessonType"
                        className="form-field"
                        component={SelectField}
                        label={t(TYPE_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                    >
                        <option value="" />
                        {lessonTypes.map((lessonType) => (
                            <option value={lessonType} key={lessonType}>
                                {t(`formElements:lesson_type_${lessonType.toLowerCase()}_label`)}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="room"
                        className="form-field"
                        component={SelectField}
                        label={t(ROOM_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                    >
                        <option value="" />
                        {rooms.map((room) => (
                            <option value={room.id} key={room.id}>
                                {room.name}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="period"
                        className="form-field"
                        component={SelectField}
                        label={t(CLASS_LABEL)}
                        validate={[required]}
                        disabled={isVacation}
                    >
                        <option value="" />
                        {periods.map((period) => (
                            <option value={period.id} key={period.id}>
                                {period.startTime} - {period.endTime}
                            </option>
                        ))}
                    </Field>
                    <Field
                        name="teacherForSite"
                        className="form-field"
                        multiline
                        rowsMax="1"
                        margin="normal"
                        component={renderTextField}
                        label={t(TEACHER_LABEL) + t(FOR_SITE_LABEL)}
                        validate={[required, maxLengthValue]}
                        disabled={isVacation}
                    />
                    <Field
                        name="subjectForSite"
                        className="form-field"
                        multiline
                        rowsMax="1"
                        margin="normal"
                        component={renderTextField}
                        label={t(SUBJECT_LABEL) + t(FOR_SITE_LABEL)}
                        validate={[required, maxLengthValue]}
                        disabled={isVacation}
                    />
                    <Field
                        name="notify"
                        label={t(COMMON_NOTIFY_LABEL)}
                        component={renderCheckboxField}
                        checked={notify}
                        onChange={handleNotifyChange}
                        color="primary"
                    />
                    <Hidden smUp smDown xsDown xsUp>
                        <Field name="date" component={renderTextField} />
                        <Field name="id" component={renderTextField} />
                        <Field name="scheduleId" component={renderTextField} />
                        <Field name="semester" component={renderTextField} />
                    </Hidden>
                    <div className="form-buttons-container">
                        <Button
                            className="buttons-style"
                            type="submit"
                            variant="contained"
                            color="primary"
                            disabled={invalid || submitting}
                        >
                            {t(SAVE_BUTTON_LABEL)}
                        </Button>
                        <Button
                            className="buttons-style"
                            type="button"
                            variant="contained"
                            disabled={invalid || submitting}
                            onClick={() => {
                                reset();
                                selectTemporaryScheduleService({});
                            }}
                        >
                            {t(CLEAR_BUTTON_LABEL)}
                        </Button>
                    </div>
                </form>
            </>
        </Card>
    );
};

const TemporaryScheduleReduxForm = reduxForm({
    form: TEMPORARY_SCHEDULE_FORM,
})(TemporaryScheduleForm);

export default TemporaryScheduleReduxForm;
