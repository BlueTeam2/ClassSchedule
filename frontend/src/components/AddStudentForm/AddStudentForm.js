import Button from '@material-ui/core/Button';
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import './AddStudentForm.scss';
import { required } from '../../validation/validateFields';
import renderTextField from '../../share/renderedFields/input';
import renderSelectField from '../../share/renderedFields/select';
import { TYPE_LABEL, CANCEL_BUTTON_LABEL } from '../../constants/translationLabels/common';
import {
    EMAIL_FIELD,
    NAME_PLACEHOLDER,
    SAVE_BUTTON_LABEL,
    CLEAR_BUTTON_LABEL,
    SURNAME_PLACEHOLDER,
    PATRONYMIC_PLACEHOLDER,
} from '../../constants/translationLabels/formElements';

export const AddStudentForm = (props) => {
    const {
        handleSubmit,
        submitting,
        initialize,
        pristine,
        student,
        reset,
        groups,
        groupId,
        submitStudentStart,
    } = props;
    const { t } = useTranslation('formElements');

    const initializeFormHandler = (currentStudent) => {
        const { id, surname, name, patronymic, email } = currentStudent;
        initialize({
            id,
            surname,
            name,
            patronymic,
            email,
            group: groupId,
        });
    };

    useEffect(() => {
        if (student) {
            initializeFormHandler(student);
        } else {
            initialize();
        }
    }, [student]);

    return (
        <form
            className="student-form"
            onSubmit={handleSubmit((data) => submitStudentStart(data, groupId))}
        >
            <Field
                className="form-field"
                name="surname"
                id="surname"
                component={renderTextField}
                type="text"
                placeholder={t(SURNAME_PLACEHOLDER)}
                label={t(SURNAME_PLACEHOLDER)}
                validate={[required]}
            />
            <Field
                className="form-field"
                name="name"
                id="name"
                component={renderTextField}
                type="text"
                placeholder={t(NAME_PLACEHOLDER)}
                label={t(NAME_PLACEHOLDER)}
                validate={[required]}
            />
            <Field
                className="form-field"
                name="patronymic"
                id="patronymic"
                component={renderTextField}
                type="text"
                placeholder={t(PATRONYMIC_PLACEHOLDER)}
                label={t(PATRONYMIC_PLACEHOLDER)}
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
                validate={[required]}
            />
            {student.id && (
                <Field
                    className="form-field"
                    component={renderSelectField}
                    name="group"
                    label={t(TYPE_LABEL)}
                    validate={[required]}
                >
                    defaultValue={groupId}
                    {groups.map((groupItem) => (
                        <option key={groupItem.id} value={groupItem.id}>
                            {groupItem.title}
                        </option>
                    ))}
                </Field>
            )}

            <div className="form-buttons">
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
                    disabled={pristine || submitting}
                    onClick={reset}
                >
                    {student ? t(CANCEL_BUTTON_LABEL) : t(CLEAR_BUTTON_LABEL)}
                </Button>
            </div>
        </form>
    );
};
