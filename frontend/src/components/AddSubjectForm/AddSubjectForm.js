import { connect } from 'react-redux';
import React, { useEffect } from 'react';
import { Field, reduxForm } from 'redux-form';
import Button from '@material-ui/core/Button';
import { useTranslation } from 'react-i18next';

import './AddSubjectForm.scss';
import Card from '../../share/Card/Card';
import { SUBJECT_FORM } from '../../constants/reduxForms';
import renderTextField from '../../share/renderedFields/input';
import { required, uniqueSubject, maxLengthValue } from '../../validation/validateFields';
import { getClearOrCancelTitle, setDisableButton } from '../../helper/disableComponent';
import {
    EDIT_TITLE,
    CREATE_TITLE,
    SAVE_BUTTON_LABEL,
    SUBJECT_Y_LABEL,
    SUBJECT_LABEL,
} from '../../constants/translationLabels/formElements';

const AddSubject = (props) => {
    const { t } = useTranslation('formElements');
    const { handleSubmit, pristine, onReset, submitting, subject, initialize } = props;

    useEffect(() => {
        if (subject) {
            if (subject.id) {
                initialize({
                    id: subject.id,
                    name: subject.name,
                });
            } else {
                initialize();
            }
        }
    }, [subject]);

    return (
        <Card additionClassName="form-card subject-form">
            <h2 style={{ textAlign: 'center' }}>
                {subject.id ? t(EDIT_TITLE) : t(CREATE_TITLE)}
                {t(SUBJECT_Y_LABEL)}
            </h2>
            <form onSubmit={handleSubmit}>
                <Field
                    className="form-field"
                    name="name"
                    component={renderTextField}
                    label={`${t(SUBJECT_LABEL)}:`}
                    validate={[required, uniqueSubject, maxLengthValue]}
                />
                <div className="form-buttons-container subject-btns">
                    <Button
                        variant="contained"
                        color="primary"
                        className="buttons-style "
                        disabled={pristine || submitting}
                        type="submit"
                    >
                        {t(SAVE_BUTTON_LABEL)}
                    </Button>
                    <Button
                        type="button"
                        variant="contained"
                        className="buttons-style"
                        disabled={setDisableButton(pristine, submitting, subject.id)}
                        onClick={onReset}
                    >
                        {getClearOrCancelTitle(subject.id, t)}
                    </Button>
                </div>
            </form>
        </Card>
    );
};

const mapStateToProps = (state) => ({
    subject: state.subjects.subject,
});

export default connect(mapStateToProps)(
    reduxForm({
        form: SUBJECT_FORM,
    })(AddSubject),
);
