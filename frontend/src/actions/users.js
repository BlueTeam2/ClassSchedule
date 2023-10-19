import * as actionTypes from './actionsType';

export const setUsers = (res) => {
    return {
        type: actionTypes.SET_USERS,
        result: res,
    };
};
export const setUser = (res) => {
    return {
        type: actionTypes.SET_USER,
        result: res,
    };
};
