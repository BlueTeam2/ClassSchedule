import React, { useEffect, useState } from 'react';
import { Field } from 'redux-form';
import { isNil } from 'lodash';
import Button from '@material-ui/core/Button';
import MenuItem from '@material-ui/core/MenuItem';
import { useTranslation } from 'react-i18next';
import { required } from '../../validation/validateFields';
import SelectField from '../../share/renderedFields/select';
import './FreeRoomsDialog.scss';
import {
    FORM_SEMESTER_FREE_ROOMS,
    FORM_WEEK_FREE_ROOMS,
    FORM_DAY_FREE_ROOMS,
    FORM_CLASS_FREE_ROOMS,
    FORM_SUBMIT_BUTTON_LABEL,
} from '../../constants/translationLabels/formElements';
import { COMMON_SEARCH_FREE_ROOMS_TITLE } from '../../constants/translationLabels/common';

const FreeRoomForm = (props) => {
    const {
        handleSubmit,
        getAllSemestersItems,
        getFreeRoomsByParams,
        pristine,
        submitting,
        semesters,
        clearField,
        setRoomsLoading,
        initialValues,
    } = props;
    const [semesterClasses, setSemesterClasses] = useState([]);
    const [semesterDays, setSemesterDays] = useState([]);
    const { t } = useTranslation('formElements');

    const weeks = ['ODD', 'EVEN', 'WEEKLY'];

    const getFormDataBySemesterId = (id) => {
        const semesterData = semesters.find((item) => item.id === id);
        setSemesterDays(semesterData?.semester_days.map((item) => item.toUpperCase()) || []);
        setSemesterClasses(semesterData?.semester_classes || []);
        clearField('classId');
        clearField('dayOfWeek');
    };

    useEffect(() => {
        getAllSemestersItems();
    }, []);
    useEffect(() => {
        if (!isNil(semesters)) {
            getFormDataBySemesterId(initialValues.semesterId);
        }
    }, [semesters]);

    const searchHandler = (params) => {
        getFreeRoomsByParams(params);
        setRoomsLoading(true);
    };

    return (
        <form className="free-room-form" onSubmit={handleSubmit(searchHandler)}>
            <h3>{t(COMMON_SEARCH_FREE_ROOMS_TITLE)}</h3>
            <div className="field-container">
                <Field
                    name="semesterId"
                    component={SelectField}
                    label={t(FORM_SEMESTER_FREE_ROOMS)}
                    onChange={({ target }) => getFormDataBySemesterId(target.value)}
                    type="text"
                    className="form-select"
                    validate={[required]}
                >
                    {semesters.map((semester) => (
                        <MenuItem key={semester.id} value={semester.id}>
                            {semester.description}
                        </MenuItem>
                    ))}
                </Field>
                <Field
                    name="evenOdd"
                    component={SelectField}
                    label={t(FORM_WEEK_FREE_ROOMS)}
                    type="text"
                    className="form-select"
                    validate={[required]}
                >
                    <MenuItem value="" className="hidden" disabled />
                    {weeks.map((week) => (
                        <MenuItem key={week} value={`${week}`}>
                            {t(`common:${week.toLowerCase()}_week`)}
                        </MenuItem>
                    ))}
                </Field>
                <Field
                    name="dayOfWeek"
                    component={SelectField}
                    label={t(FORM_DAY_FREE_ROOMS)}
                    type="text"
                    className="form-select"
                    validate={[required]}
                >
                    <MenuItem value="" className="hidden" disabled />
                    {semesterDays.map((day) => (
                        <MenuItem key={day} value={`${day}`}>
                            {t(`common:day_of_week_${day}`)}
                        </MenuItem>
                    ))}
                </Field>
                <Field
                    name="classId"
                    component={SelectField}
                    label={t(FORM_CLASS_FREE_ROOMS)}
                    type="text"
                    className="form-select"
                    validate={[required]}
                >
                    <MenuItem value="" className="hidden" disabled />
                    {semesterClasses.map((item) => {
                        const classTitle = item.class_name;
                        return (
                            <MenuItem key={classTitle} value={`${classTitle}`}>
                                {classTitle}
                            </MenuItem>
                        );
                    })}
                </Field>
            </div>
            <div className="app-button-container">
                <Button
                    variant="contained"
                    disabled={pristine || submitting}
                    className="common-button"
                    type="submit"
                >
                    {t(FORM_SUBMIT_BUTTON_LABEL)}
                </Button>
            </div>
        </form>
    );
};

export default FreeRoomForm;
