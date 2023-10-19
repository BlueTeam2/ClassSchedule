import { call, put, delay, takeLatest, takeEvery } from 'redux-saga/effects';
import jwtDecode from 'jwt-decode';
import * as actionTypes from '../actions/actionsType';

import {
    ACTIVATE_ACCOUNT_URL,
    LOGIN_URL,
    LOGOUT_URL,
    REGISTRATION_URL,
    RESET_PASSWORD_URL,
} from '../constants/axios';
import {
    logout,
    authAutoLogout,
    resetUserPasswordSuccess,
    activateSuccess,
    registerUserSuccess,
    authSuccess,
    setAuthError,
} from '../actions';
import { axiosCall } from '../services/axios';
import { GOOGLE } from '../constants/common';
import { TOKEN_BEGIN } from '../constants/tokenBegin';
import axios from '../helper/axios';
import { setAuthLoading } from '../actions/loadingIndicator';
import { POST, PUT } from '../constants/methods';
import { createErrorMessage } from '../utils/sagaUtils';

function* loginToAccount({ payload }) {
    try {
        let response;
        if (payload.type === GOOGLE) {
            response = { data: { token: payload.token, email: '' } };
        } else {
            response = yield call(axiosCall, LOGIN_URL, POST, payload);
        }
        const { token, email } = response.data;
        const decodedJWT = jwtDecode(token);
        const expirationDate = new Date(decodedJWT.exp * 1000);

        axios.defaults.headers.common.Authorization = TOKEN_BEGIN + token;

        yield localStorage.setItem('token', TOKEN_BEGIN + token);
        yield localStorage.setItem('expirationDate', expirationDate);
        yield localStorage.setItem('userRole', decodedJWT.roles);
        yield localStorage.setItem('email', email || decodedJWT.sub);

        yield put(authSuccess({ token, role: decodedJWT.roles, email }));
        yield put(authAutoLogout(decodedJWT.exp * 1000 - new Date().getTime()));
    } catch (error) {
        yield put(
            setAuthError({
                login: createErrorMessage({ response: error.response }),
            }),
        );
    } finally {
        yield put(setAuthLoading(false));
    }
}

function* registerAccount({ payload }) {
    try {
        const response = yield call(axiosCall, REGISTRATION_URL, POST, payload);
        yield put(registerUserSuccess(response));
    } catch (error) {
        yield put(
            setAuthError({
                registration: createErrorMessage({ response: error.response }),
            }),
        );
    } finally {
        yield put(setAuthLoading(false));
    }
}

function* logoutOfAccount(payload) {
    try {
        yield call(axiosCall, LOGOUT_URL, POST, payload);
        yield put(logout());
    } catch (error) {
        yield put(
            setAuthError({
                login: createErrorMessage({ response: error.response }),
            }),
        );
    }
}

function* checkAuthState(payload) {
    const { token, role } = payload;
    const expirationDate = new Date(localStorage.getItem('expirationDate'));
    if (!token || expirationDate <= new Date()) {
        yield put(logout());
    } else {
        yield put(authSuccess({ token, role }));
    }
}

function* checkAuthTimeout(action) {
    yield delay(action.expirationTime);
    yield call(logoutOfAccount);
}

function* activateUserAccount({ payload }) {
    try {
        const response = yield call(axiosCall, `${ACTIVATE_ACCOUNT_URL}?token=${payload}`, PUT);
        yield put(activateSuccess(response));
    } catch (error) {
        yield put(
            setAuthError({
                activationError: createErrorMessage({ response: error.response }),
            }),
        );
    } finally {
        yield put(setAuthLoading(false));
    }
}

function* resetPassword({ payload }) {
    try {
        const response = yield call(
            axiosCall,
            `${RESET_PASSWORD_URL}?email=${payload.email}`,
            PUT,
            payload,
        );
        yield put(resetUserPasswordSuccess(response));
    } catch (error) {
        yield put(
            setAuthError({
                resetPassword: createErrorMessage({ response: error.response }),
            }),
        );
    } finally {
        yield put(setAuthLoading(false));
    }
}

export default function* watchUserAuthentication() {
    yield takeLatest(actionTypes.REGISTER_USER, registerAccount);
    yield takeLatest(actionTypes.ACTIVATE_USER, activateUserAccount);
    yield takeLatest(actionTypes.RESET_USER_PASSWORD, resetPassword);
    yield takeLatest(actionTypes.AUTH_USER, loginToAccount);
    yield takeEvery(actionTypes.AUTH_USER_CHECK_STATE, checkAuthState);
    yield takeEvery(actionTypes.AUTH_USER_AUTO_LOGOUT, checkAuthTimeout);
    yield takeLatest(actionTypes.AUTH_USER_INITIATE_LOGOUT, logoutOfAccount);
}
