import * as actionTypes from './actionsType';

export const showAllSubjects = (res) => {
    return {
        type: actionTypes.SHOW_ALL_SUBJECTS,
        result: res,
    };
};

export const setDisabledSubjects = (res) => {
    return {
        type: actionTypes.SET_DISABLED_SUBJECTS,
        result: res,
    };
};

export const addSubject = (res) => {
    return {
        type: actionTypes.ADD_SUBJECT,
        result: res,
    };
};

export const deleteSubject = (res) => {
    return {
        type: actionTypes.DELETE_SUBJECT,
        result: res,
    };
};

export const selectSubject = (res) => {
    return {
        type: actionTypes.SELECT_SUBJECT,
        result: res,
    };
};

export const updateSubject = (res) => {
    return {
        type: actionTypes.UPDATE_SUBJECT,
        result: res,
    };
};

export const clearSubject = () => {
    return {
        type: actionTypes.CLEAR_SUBJECT,
    };
};
