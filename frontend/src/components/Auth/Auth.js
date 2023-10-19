import React, { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import { HOME_PAGE_LINK, LOGIN_LINK, ADMIN_PAGE_LINK } from '../../constants/links';
import { authTypes, successAuthMessages } from '../../constants/auth';
import { GOOGLE_LOGIN_URL } from '../../constants/axios';
import { userRoles } from '../../constants/userRoles';
import { snackbarTypes } from '../../constants/snackbarTypes';
import LoginForm from '../LoginForm/LoginForm';
import RegistrationForm from '../RegistrationForm/RegistrationForm';
import ResetPasswordForm from '../ResetPasswordForm/ResetPasswordForm';

import { resetFormHandler } from '../../helper/formHelper';
import { handleSnackbarOpenService } from '../../services/snackbarService';
import { LOGIN_FORM, REGISTRATION_FORM, RESET_PASSWORD_FORM } from '../../constants/reduxForms';
import { GOOGLE } from '../../constants/common';

import './Auth.scss';
import {
    LOGIN_TITLE,
    REGISTRATION_PAGE_TITLE,
    HOME_TITLE,
    ADMIN_TITLE,
    RESET_PASSWORD_PAGE_TITLE,
    BROKEN_TOKEN,
} from '../../constants/translationLabels/common';

const Auth = (props) => {
    const {
        authType,
        userRole,
        response,
        resetPasswordResponse,
        onRegister,
        onResetPassword,
        setLoadingForm,
        onAuth,
        setError,
        error,
        isLoading,
    } = props;
    const [isResponse, setIsResponse] = useState(false);
    const history = useHistory();
    const { t } = useTranslation('common');
    const url = window.document.location;
    const parser = new URL(url);

    const loginHandler = loginData => {
        onAuth(loginData);
        setLoadingForm(true);
        resetFormHandler(LOGIN_FORM);
    };

    const showSuccessMessage = massage => {
        handleSnackbarOpenService(true, snackbarTypes.SUCCESS, t(massage));
    };

    const successLoginRedirect = useCallback(() => {
        if (userRole === userRoles.MANAGER) {
            document.title = t(ADMIN_TITLE);
            history.push(ADMIN_PAGE_LINK);
        } else {
            document.title = t(HOME_TITLE);
            history.push(HOME_PAGE_LINK);
        }
        showSuccessMessage(successAuthMessages[authType]);
    }, []);

    const registrationHandler = registrationData => {
        onRegister({
            email: registrationData.email,
            password: registrationData.password,
        });
        setLoadingForm(true);
        resetFormHandler(REGISTRATION_FORM);
        setIsResponse(true);
    };

    const resetPasswordHandler = resetPasswordData => {
        onResetPassword({
            email: resetPasswordData.email,
        });
        setLoadingForm(true);
        resetFormHandler(RESET_PASSWORD_FORM);
        setIsResponse(true);
    };

    const socialLoginHandler = data => {
        setLoadingForm(true);
        if (!data.token || data.token.length < 20) {
            props.setError({ login: t(BROKEN_TOKEN) });
            return;
        }
        onAuth({ type: authTypes.GOOGLE });
        resetFormHandler(LOGIN_FORM);
        window.history.replaceState({}, document.title, '/');
        setLoadingForm(false);
    };

    let social = false;
    let isToken = false;
    let splitedParamToken = '';

    if (parser.search.length > 0) {
        const params = parser.search.split('&');
        if (params) {
            params.forEach(param => {
                const splitedParam = param.split('=');
                if (splitedParam) {
                    if (splitedParam[0] === '?social' && splitedParam[1] === 'true') {
                        social = true;
                    }
                    if (splitedParam[0] === 'token' && splitedParam[1].length > 0) {
                        isToken = true;
                        splitedParamToken = splitedParam;
                    }
                }
            });
        }
        if (social && isToken)
            socialLoginHandler({ authType: GOOGLE, token: splitedParamToken[1] });
    }

    useEffect(() => {
        if (userRole) {
            successLoginRedirect();
        }
    }, [userRole]);

    useEffect(() => {
        if (isResponse) {
            showSuccessMessage(successAuthMessages[authType]);
            history.push(LOGIN_LINK);
        }
    }, [response, resetPasswordResponse]);

    switch (authType) {
        case authTypes.REGISTRATION:
            document.title = t(REGISTRATION_PAGE_TITLE);
            return (
                <div className="auth-container">
                    <RegistrationForm
                        isLoading={isLoading}
                        registrationError={error}
                        registrationHandler={registrationHandler}
                        setError={setError}
                    />
                </div>
            );
        case authTypes.RESET_PASSWORD:
            document.title = t(RESET_PASSWORD_PAGE_TITLE);
            return (
                <div className="auth-container">
                    <ResetPasswordForm
                        isLoading={isLoading}
                        resetPasswordError={error}
                        onSubmit={resetPasswordHandler}
                        setError={setError}
                    />
                </div>
            );
        default:
            document.title = t(LOGIN_TITLE);
            return (
                <div className="auth-container">
                    <a
                        className="hidden-link"
                        href={`${process.env.REACT_APP_API_BASE_URL.trim()}${`/${GOOGLE_LOGIN_URL}`}`}
                    >
                        auth via google
                    </a>
                    <LoginForm
                        isLoading={isLoading}
                        loginHandler={loginHandler}
                        setError={setError}
                        errors={error}
                    />
                </div>
            );
    }
};

export default Auth;
