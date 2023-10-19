import React from 'react';
import { Field, reduxForm } from 'redux-form';

import { connect } from 'react-redux';

import './ChangePasswordForm.scss';

import Button from '@material-ui/core/Button';
import { useTranslation } from 'react-i18next';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Typography from '@material-ui/core/Typography';
import renderTextField from '../../share/renderedFields/input';

import { required, password } from '../../validation/validateFields';

import { PROFILE_FORM } from '../../constants/reduxForms';
import {
    SAVE_BUTTON_LABEL,
    CHANGE_PASSWORD_FROM_TITLE,
    PASSWORD_LABEL,
    RETYPE_PASSWORD_LABEL,
    CLEAR_BUTTON_LABEL,
    NEW_PASSWORD_LABEL,
} from '../../constants/translationLabels/formElements';

function ExpandMoreIcon() {
    return null;
}

const ChangePasswordForm = (props) => {
    const { t } = useTranslation('formElements');
    const { handleSubmit, pristine, onReset, submitting } = props;

    return (
        <>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon />}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography>{t(CHANGE_PASSWORD_FROM_TITLE)}</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography>
                        <form onSubmit={handleSubmit}>
                            <Field
                                component={renderTextField}
                                className="form-field"
                                name="current_password"
                                id="current_password"
                                label={t(PASSWORD_LABEL)}
                                type="password"
                                validate={[required, password]}
                            />
                            <Field
                                component={renderTextField}
                                className="form-field"
                                name="new_password"
                                id="new_password"
                                label={t(NEW_PASSWORD_LABEL)}
                                type="password"
                                validate={[required, password]}
                            />
                            <Field
                                component={renderTextField}
                                className="form-field"
                                name="confirm_password"
                                id="confirm_password"
                                label={t(RETYPE_PASSWORD_LABEL)}
                                type="password"
                                validate={[required, password]}
                            />

                            <div className="form-buttons-container">
                                <Button
                                    className="buttons-style"
                                    type="submit"
                                    variant="contained"
                                    color="primary"
                                    disabled={pristine || submitting}
                                >
                                    {t(SAVE_BUTTON_LABEL)}
                                </Button>
                                <Button
                                    className="buttons-style"
                                    type="button"
                                    variant="contained"
                                    disabled={pristine || submitting}
                                    onClick={onReset}
                                >
                                    {t(CLEAR_BUTTON_LABEL)}
                                </Button>
                            </div>
                        </form>
                    </Typography>
                </AccordionDetails>
            </Accordion>
        </>
    );
};

export default connect()(
    reduxForm({
        form: PROFILE_FORM,
    })(ChangePasswordForm),
);
