import * as actionTypes from './actionsType';
import { TOKEN_BEGIN } from '../constants/tokenBegin';

export const authUser = (payload) => ({
    type: actionTypes.AUTH_USER,
    payload,
});
export const authAutoLogout = (expirationTime) => ({
    type: actionTypes.AUTH_USER_AUTO_LOGOUT,
    expirationTime,
});

export const registerUser = (payload) => ({
    type: actionTypes.REGISTER_USER,
    payload,
});

export const resetUserPassword = (payload) => ({
    type: actionTypes.RESET_USER_PASSWORD,
    payload,
});

export const activateUser = (payload) => ({
    type: actionTypes.ACTIVATE_USER,
    payload,
});

export const logout = () => ({
    type: actionTypes.AUTH_USER_LOGOUT,
});

export const authCheckState = () => {
    const token = localStorage.getItem('token');
    if (token && !token.includes(TOKEN_BEGIN)) {
        return {
            type: actionTypes.AUTH_USER_LOGOUT,
        };
    }
    const role = localStorage.getItem('userRole');
    return {
        type: actionTypes.AUTH_USER_CHECK_STATE,
        token,
        role,
    };
};
export const activateSuccess = (response) => ({
    type: actionTypes.ACTIVATE_USER_SUCCESS,
    response,
});
export const resetUserPasswordSuccess = (response) => ({
    type: actionTypes.RESET_USER_PASSWORD_SUCCESS,
    response,
});
export const registerUserSuccess = (response) => ({
    type: actionTypes.REGISTER_USER_SUCCESS,
    response,
});
export const authSuccess = (response) => ({
    type: actionTypes.AUTH_USER_SUCCESS,
    response,
});
export const setAuthError = (error) => ({
    type: actionTypes.SET_AUTH_ERROR,
    error,
});
