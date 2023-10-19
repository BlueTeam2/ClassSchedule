/* eslint-disable */
// this functionality doesn't work
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

import { Field, reduxForm } from 'redux-form';

import Checkbox from '@material-ui/core/Checkbox';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Button from '@material-ui/core/Button';
import Switch from '@material-ui/core/Switch';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { TEMPORARY_SCHEDULE_VACATION_FORM } from '../../../constants/reduxForms';
import renderMonthPicker from '../../../share/renderedFields/timeSemester';
import Card from '../../../share/Card/Card';

import { greaterThanDate, lessThanDate, required } from '../../../validation/validateFields';

import { handleTeacherInfo } from '../../../helper/renderTeacher';

import {
    selectTeacherIdService,
    selectVacationService,
} from '../../../services/temporaryScheduleService';
import renderCheckboxField from '../../../share/renderedFields/checkbox';
import {
    SAVE_BUTTON_LABEL,
    CLEAR_BUTTON_LABEL,
    CLASS_FROM_LABEL,
    CLASS_TO_LABEL,
    FORM_TEACHER_LABEL,
    FOR_TEACHER,
    EDIT_VACATION_FORM,
    CREATE_VACATION_FORM,
} from '../../../constants/translationLabels/formElements';
import {
    COMMON_NOTIFY_LABEL,
    COMMON_FEW_DAYS_LABEL,
    COMMON_DATE_LABEL,
    FOR_ALL,
} from '../../../constants/translationLabels/common';

const TemporaryScheduleVacationForm = (props) => {
    const { t } = useTranslation('formElements');
    const { handleSubmit, invalid, reset, submitting, teachers, teacherId, vacation, initialize } =
        props;

    const [isFewDays, setIsFewDays] = useState(false);
    const [forAll, setForAll] = useState(true);
    const [notify, setNotify] = useState(false);

    const vacationId = vacation.id;

    const initializeFormHandler = (vacationData) => {
        if (vacationData.teacher?.id) {
            selectTeacherIdService(vacationData.teacher.id);
            setForAll(false);
        } else {
            setForAll(true);
        }
        initialize({
            id: vacationData.id,
            date: vacationData.date,
        });
    };

    useEffect(() => {
        if (vacationId) {
            initializeFormHandler(vacation);
        } else {
            initialize();
        }
    }, [vacationId]);

    useEffect(() => {
        if (teacherId) setForAll(false);
    }, [teacherId]);

    useEffect(() => {
        if (forAll) selectTeacherIdService(null);
    }, [forAll]);

    const handleNotifyChange = (event) => setNotify(event.target.checked);

    const defaultProps = {
        options: teachers,
        getOptionLabel: (option) => (option ? handleTeacherInfo(option) : ''),
    };

    const handleFindTeacher = (id) => {
        if (id) return teachers.find((teacher) => teacher.id === id);
        return '';
    };

    const handleTeacherSelect = (teacher) => {
        if (teacher) selectTeacherIdService(teacher.id);
    };

    const forAllLabel = forAll ? t(FOR_ALL) : t(FOR_TEACHER);

    const isDisabled = submitting || invalid;

    return (
        <Card additionClassName='form-card'>
            <h2 className='form-title under-line'>
                {vacationId ? t(EDIT_VACATION_FORM) : t(CREATE_VACATION_FORM)}
            </h2>
            <form onSubmit={handleSubmit}>
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={isFewDays}
                            onChange={(event) => {
                                setIsFewDays(event.target.checked);
                            }}
                            color='primary'
                        />
                    }
                    label={t(COMMON_FEW_DAYS_LABEL)}
                />
                {isFewDays ? (
                    <>
                        <Field
                            className='time-input'
                            name='from'
                            component={renderMonthPicker}
                            label={`${t(CLASS_FROM_LABEL)}:`}
                            validate={[required, lessThanDate]}
                        />
                        <Field
                            className='time-input'
                            name='to'
                            component={renderMonthPicker}
                            label={`${t(CLASS_TO_LABEL)}:`}
                            validate={[required, greaterThanDate]}
                        />
                    </>
                ) : (
                    <Field
                        className='time-input'
                        name='date'
                        component={renderMonthPicker}
                        label={`${t(COMMON_DATE_LABEL)}:`}
                        validate={[required]}
                    />
                )}
                <div>
                    <FormControlLabel
                        control={
                            <Switch
                                color='primary'
                                checked={forAll}
                                onChange={(event) => {
                                    setForAll(event.target.checked);
                                }}
                            />
                        }
                        label={forAllLabel}
                    />
                </div>
                {!forAll && (
                    <Autocomplete
                        {...defaultProps}
                        clearOnEscape
                        openOnFocus
                        value={handleFindTeacher(teacherId)}
                        onChange={(event, newValue) => {
                            if (!newValue) handleTeacherSelect({});
                            else handleTeacherSelect(newValue);
                        }}
                        renderInput={(params) => (
                            <TextField {...params} label={t(FORM_TEACHER_LABEL)} margin='normal' />
                        )}
                    />
                )}
                <Field
                    name='notify'
                    label={t(COMMON_NOTIFY_LABEL)}
                    component={renderCheckboxField}
                    checked={notify}
                    onChange={handleNotifyChange}
                    color='primary'
                />

                <div className='form-buttons-container'>
                    <Button
                        className='buttons-style'
                        type='submit'
                        variant='contained'
                        color='primary'
                        disabled={isDisabled}
                    >
                        {t(SAVE_BUTTON_LABEL)}
                    </Button>
                    <Button
                        className='buttons-style'
                        type='button'
                        variant='contained'
                        disabled={isDisabled}
                        onClick={() => {
                            reset();
                            selectVacationService({});
                        }}
                    >
                        {t(CLEAR_BUTTON_LABEL)}
                    </Button>
                </div>
            </form>
        </Card>
    );
};

const TemporaryScheduleVacationReduxForm = reduxForm({
    form: TEMPORARY_SCHEDULE_VACATION_FORM,
})(TemporaryScheduleVacationForm);

export default TemporaryScheduleVacationReduxForm;
