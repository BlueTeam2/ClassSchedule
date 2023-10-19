import React, { useEffect } from 'react';
import { Field, reduxForm } from 'redux-form';

import { MenuItem } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import { useTranslation } from 'react-i18next';
import renderTextField from '../../../share/renderedFields/input';
import { required } from '../../../validation/validateFields';

import Card from '../../../share/Card/Card';

import { TEACHER_FORM } from '../../../constants/reduxForms';

import './AddTeacherForm.scss';
import SelectField from '../../../share/renderedFields/select';
import { getDepartmentByIdService } from '../../../services/departmentService';
import { getClearOrCancelTitle, setDisableButton } from '../../../helper/disableComponent';
import {
    EDIT_TITLE,
    CREATE_TITLE,
    SAVE_BUTTON_LABEL,
    TEACHER_A_LABEL,
    TEACHER_SURNAME,
    TEACHER_FIRST_NAME,
    TEACHER_PATRONYMIC,
    TEACHER_POSITION,
    EMAIL_FIELD,
    DEPARTMENT_TEACHER_LABEL,
} from '../../../constants/translationLabels/formElements';

const AddTeacherForm = (props) => {
    const { t } = useTranslation('formElements');
    const { handleSubmit, pristine, submitting, reset, departments, teacher, initialize } = props;

    const teacherId = teacher.id;

    const initializeFormHandler = (teacherData) => {
        const department = teacherData.department ? teacherData.department.id : 0;
        const { id, surname, name, patronymic, position, email } = teacherData;
        initialize({
            id,
            surname,
            name,
            patronymic,
            position,
            email,
            department,
        });
        if (teacherData.department) getDepartmentByIdService(teacherData.department.id);
    };

    useEffect(() => {
        if (teacherId) {
            initializeFormHandler(teacher);
        } else {
            initialize();
        }
    }, [teacherId]);

    return (
        <Card additionClassName="form-card teacher-form">
            <form className="createTeacherForm w-100" onSubmit={handleSubmit}>
                <h2 className="form-title">
                    {teacherId ? t(EDIT_TITLE) : t(CREATE_TITLE)} {t(TEACHER_A_LABEL)}
                </h2>

                <Field
                    className="form-field"
                    name="surname"
                    id="surname"
                    component={renderTextField}
                    type="text"
                    placeholder={t(TEACHER_SURNAME)}
                    label={t(TEACHER_SURNAME)}
                    validate={[required]}
                />

                <Field
                    className="form-field"
                    name="name"
                    id="name"
                    component={renderTextField}
                    type="text"
                    placeholder={t(TEACHER_FIRST_NAME)}
                    label={t(TEACHER_FIRST_NAME)}
                    validate={[required]}
                />

                <Field
                    className="form-field"
                    name="patronymic"
                    id="patronymic"
                    component={renderTextField}
                    type="text"
                    placeholder={t(TEACHER_PATRONYMIC)}
                    label={t(TEACHER_PATRONYMIC)}
                    validate={[required]}
                />

                <Field
                    className="form-field"
                    name="position"
                    id="position"
                    component={renderTextField}
                    type="text"
                    placeholder={t(TEACHER_POSITION)}
                    label={t(TEACHER_POSITION)}
                    validate={[required]}
                />
                <Field
                    className="form-field"
                    name="email"
                    id="email"
                    component={renderTextField}
                    type="email"
                    placeholder={t(EMAIL_FIELD)}
                    label={t(EMAIL_FIELD)}
                />
                <Field
                    name="department"
                    className="week-days"
                    component={SelectField}
                    label={t(DEPARTMENT_TEACHER_LABEL)}
                    type="text"
                    onChange={({ target }) => {
                        getDepartmentByIdService(target.value);
                    }}
                >
                    <MenuItem value="" className="hidden" disabled />
                    {departments.map((item) => (
                        <MenuItem key={item.id} value={item.value}>
                            {item.label}
                        </MenuItem>
                    ))}
                </Field>

                <div className="form-buttons-container">
                    <Button
                        className="buttons-style"
                        variant="contained"
                        color="primary"
                        disabled={pristine || submitting}
                        type="submit"
                    >
                        {t(SAVE_BUTTON_LABEL)}
                    </Button>
                    <Button
                        className="buttons-style"
                        variant="contained"
                        disabled={setDisableButton(pristine, submitting, teacher.id)}
                        onClick={() => {
                            reset();
                            props.onSetSelectedCard(null);
                        }}
                    >
                        {getClearOrCancelTitle(teacher.id, t)}
                    </Button>
                </div>
            </form>
        </Card>
    );
};

export default reduxForm({
    form: TEACHER_FORM,
})(AddTeacherForm);
