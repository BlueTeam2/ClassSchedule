import React from 'react';
import { Field } from 'redux-form';

import { useTranslation } from 'react-i18next';
import { MenuItem } from '@material-ui/core';

import Button from '@material-ui/core/Button';
import SelectField from '../../../share/renderedFields/select';

import './CopyLessonsFromSemesterForm.scss';
import Card from '../../../share/Card/Card';

import { required } from '../../../validation/validateFields';
import {
    FORM_SEMESTER_LABEL,
    FORM_COPY_LESSON,
} from '../../../constants/translationLabels/formElements';
import {
    COPY_LESSON,
    COPY_LESSONS_FROM_SEMESTER,
} from '../../../constants/translationLabels/common';

const CopyLessonsFromSemesterForm = (props) => {
    const { t } = useTranslation('common');
    const { semesters, handleSubmit, pristine, submitting } = props;
    const renderSemesterList = () => {
        if (semesters?.length > 1) {
            return (
                <Field
                    id="fromSemesterId"
                    name="fromSemesterId"
                    component={SelectField}
                    label={t(FORM_SEMESTER_LABEL)}
                    type="text"
                    validate={[required]}
                >
                    <MenuItem value="" className="hidden" disabled />
                    {semesters.map((semester) => (
                        <MenuItem key={semester.id} value={semester.id}>
                            {semester.description}
                        </MenuItem>
                    ))}
                </Field>
            );
        }
        if (semesters?.length === 1) {
            handleSubmit({ fromSemesterId: semesters[0].id });
            return <p>{semesters[0].description}</p>;
        }
        return null;
    };

    return (
        <Card additionClassName="form-card">
            <form onSubmit={handleSubmit}>
                <h2>{t(COPY_LESSON)}</h2>
                <p>{t(COPY_LESSONS_FROM_SEMESTER)}</p>
                {renderSemesterList()}
                <div className="form-buttons-container">
                    <Button
                        className="semester-copy-btn"
                        variant="contained"
                        color="primary"
                        type="submit"
                        disabled={pristine || submitting}
                    >
                        {t(FORM_COPY_LESSON)}
                    </Button>
                </div>
            </form>
        </Card>
    );
};

export default CopyLessonsFromSemesterForm;
