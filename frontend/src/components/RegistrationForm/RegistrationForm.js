import React from 'react';

import { Field, reduxForm } from 'redux-form';
import { useTranslation } from 'react-i18next';
import Button from '@material-ui/core/Button';
import CircularProgress from '@material-ui/core/CircularProgress';
import { Link } from 'react-router-dom';
import Card from '@material-ui/core/Card';
import renderTextField from '../../share/renderedFields/input';
import '../../styles/forms.scss';
import { REGISTRATION_FORM } from '../../constants/reduxForms';

import { email, password, required } from '../../validation/validateFields';
import { LOGIN_LINK } from '../../constants/links';
import {
    REGISTRATION_PAGE_TITLE,
    LOGIN_TITLE,
    ACCOUNT_EXIST,
    CREATE_ACCOUNT,
    DIFFERENT_PASSWORDS,
} from '../../constants/translationLabels/common';
import {
    PASSWORD_LABEL,
    RETYPE_PASSWORD_LABEL,
    EMAIL_LABEL,
} from '../../constants/translationLabels/formElements';

const RegistrationForm = (props) => {
    const { handleSubmit, errors, setError, registrationHandler, isLoading } = props;
    const { t } = useTranslation('common');

    const emailValidate = { validate: [required, email] };
    const emailErrorCondition = errors && errors.registration.reg;
    const emailAdvancedValidate = {
        error: !!emailErrorCondition,
        helperText: emailErrorCondition ? errors.registration.reg : '',
    };

    const passwordValidate = { validate: [required, password] };
    const passwordsCondition = errors && errors.registration.passwords;
    const passwordValidateAdvanced = {
        error: !!passwordsCondition,
        helperText: passwordsCondition ? errors.registration.passwords : '',
    };

    const retypePasswordValidate = { validate: [required, password] };
    const retypePasswordValidateAdvanced = {
        error: !!passwordsCondition,
    };

    const isValidForm = (formValues) => {
        if (formValues.password !== formValues.retypePassword) {
            setError({
                registration: { passwords: t(DIFFERENT_PASSWORDS) },
            });
            return false;
        }
        return true;
    };
    const onRegistration = (values) => {
        const isValid = isValidForm(values);
        if (isValid) {
            registrationHandler(values);
        }
    };

    return (
        <Card className="auth-card">
            <div className="auth-card-header">
                <h2 className="auth-card-title">{t(REGISTRATION_PAGE_TITLE)}</h2>
            </div>

            {isLoading ? (
                <CircularProgress size="70px" className="loading-circle auth-loading" />
            ) : (
                <form className="auth-form" onSubmit={handleSubmit(onRegistration)}>
                    <Field
                        name="email"
                        className="form-input"
                        type="email"
                        component={renderTextField}
                        label={t(EMAIL_LABEL)}
                        {...(!errors ? emailValidate : emailAdvancedValidate)}
                        onChange={() => props.setError(null)}
                    />
                    <Field
                        name="password"
                        className="form-input"
                        type="password"
                        component={renderTextField}
                        label={t(PASSWORD_LABEL)}
                        {...(!errors ? passwordValidate : passwordValidateAdvanced)}
                        onChange={() => props.setError(null)}
                    />
                    <Field
                        name="retypePassword"
                        className="form-input"
                        type="password"
                        component={renderTextField}
                        label={t(RETYPE_PASSWORD_LABEL)}
                        {...(!errors ? retypePasswordValidate : retypePasswordValidateAdvanced)}
                    />
                    <div className="auth-form-actions">
                        <Button
                            className="auth-confirm-button"
                            type="submit"
                            variant="contained"
                            color="primary"
                        >
                            {t(CREATE_ACCOUNT)}
                        </Button>
                    </div>
                    <div className="auth-form-footer">
                        <span>{t(ACCOUNT_EXIST)}</span>
                        <Link to={LOGIN_LINK} className="form-link">
                            {t(LOGIN_TITLE)}
                        </Link>
                    </div>
                </form>
            )}
        </Card>
    );
};

const RegistrationReduxForm = reduxForm({
    form: REGISTRATION_FORM,
})(RegistrationForm);

export default RegistrationReduxForm;
