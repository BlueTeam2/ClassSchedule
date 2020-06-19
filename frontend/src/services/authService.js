import axios from '../helper/axios';

import {
    ACTIVATE_ACCOUNT_URL,
    LOGIN_URL,
    GOOGLE_LOGIN_URL,
    LOGOUT_URL,
    REGISTRATION_URL,
    RESET_PASSWORD_URL
} from '../constants/axios';

export const authUserService = request => {
    if (
        request.result.hasOwnProperty('authType') &&
        request.result.authType === 'google'
    ) {
        return axios.get(GOOGLE_LOGIN_URL).then(response => {
            console.log('google response', response);
            return response;
        });
    }
    return axios.post(LOGIN_URL, request.result).then(response => {
        return response;
    });
};

export const registerUserService = request => {
    return axios.post(REGISTRATION_URL, request.result).then(response => {
        return response;
    });
};

export const resetUserPasswordService = request => {
    return axios
        .put(
            `${RESET_PASSWORD_URL}?email=${request.result.email}`,
            request.result
        )
        .then(response => {
            return response;
        });
};

export const activateUserService = request => {
    return axios
        .put(`${ACTIVATE_ACCOUNT_URL}?token=${request.result}`)
        .then(response => response);
};

export const logoutUserService = () => {
    return axios.post(LOGOUT_URL).then(response => response);
};