import * as actionTypes from '../actions/actionsType';
import axios from '../helper/axios';

const initialState = {
    token: null,
    role: null,
    error: null,
    activationError: null,
};

const reducer = (state = initialState, action) => {
    const { response } = action;

    switch (action.type) {
        case actionTypes.AUTH_USER_SUCCESS:
            return { ...state, token: response.token, role: response.role, error: null };
        case actionTypes.AUTH_USER_ERROR:
            return { ...state, response, error: { login: action.error } };
        case actionTypes.AUTH_USER_LOGOUT:
            delete axios.defaults.headers.common.Authorization;

            localStorage.removeItem('token');
            localStorage.removeItem('expirationDate');
            localStorage.removeItem('userRole');
            return { ...state, response: null, token: null, error: null, role: null };
        case actionTypes.REGISTER_USER_SUCCESS:
        case actionTypes.ACTIVATE_USER_SUCCESS:
            return { ...state, response };
        case actionTypes.REGISTER_USER_ERROR:
            return { ...state, error: { registration: { reg: action.error } } };
        case actionTypes.RESET_USER_PASSWORD_SUCCESS:
            return { ...state, resetPasswordResponse: response };
        case actionTypes.RESET_USER_PASSWORD_ERROR:
            return { ...state, error: action.error };
        case actionTypes.ACTIVATE_USER_ERROR:
            return { ...state, activationError: action.error };
        case actionTypes.SET_AUTH_ERROR:
            return { ...state, error: action.error };
        default:
            return state;
    }
};

export default reducer;
