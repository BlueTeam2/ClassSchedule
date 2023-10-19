import React from 'react';

import { Field, reduxForm } from 'redux-form';

import Button from '@material-ui/core/Button';
import CircularProgress from '@material-ui/core/CircularProgress';

import { Link } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import renderTextField from '../../share/renderedFields/input';

import { RESET_PASSWORD_FORM } from '../../constants/reduxForms';

import { email, required } from '../../validation/validateFields';
import {
    EMAIL_LABEL,
    RESET_PASSWORD_HELPER_TEXT,
} from '../../constants/translationLabels/formElements';
import { LOGIN_LINK } from '../../constants/links';
import i18n from '../../i18n';
import {
    LOGIN_TITLE,
    RESET_PASSWORD_PAGE_TITLE,
    RESET_PASSWORD_LABEL,
} from '../../constants/translationLabels/common';

const ResetPasswordForm = (props) => {
    const { handleSubmit, resetPasswordError, setError, isLoading } = props;

    const error = resetPasswordError;

    const emailValidate = { validate: [required, email] };

    const errorHandling = (value) => {
        if (required(value)) {
            setError(required(value));
        } else {
            setError(null);
        }
    };

    return (
        <Card className="auth-card">
            <div className="auth-card-header">
                <h2 className="auth-card-title">{i18n.t(RESET_PASSWORD_PAGE_TITLE)}</h2>
                <span className="auth-card-subtitle">{i18n.t(RESET_PASSWORD_HELPER_TEXT)}</span>
            </div>
            {isLoading ? (
                <CircularProgress size="70px" className="loading-circle auth-loading" />
            ) : (
                <form className="auth-form" onSubmit={handleSubmit}>
                    <Field
                        name="email"
                        className="form-input"
                        component={renderTextField}
                        label={i18n.t(EMAIL_LABEL)}
                        {...(!error ? emailValidate : error)}
                        onChange={(e) => {
                            errorHandling(e.target.value);
                        }}
                    />
                    <div className="auth-form-actions">
                        <Button
                            className="auth-confirm-button"
                            type="submit"
                            variant="contained"
                            color="primary"
                        >
                            {i18n.t(RESET_PASSWORD_LABEL)}
                        </Button>
                    </div>
                    <div className="auth-form-footer">
                        <Link to={LOGIN_LINK} className="form-link">
                            {i18n.t(LOGIN_TITLE)}
                        </Link>
                    </div>
                </form>
            )}
        </Card>
    );
};

const ResetPasswordReduxForm = reduxForm({
    form: RESET_PASSWORD_FORM,
})(ResetPasswordForm);

export default ResetPasswordReduxForm;
