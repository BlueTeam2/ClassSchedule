import * as actionTypes from './actionsType';

export const addDepartment = (res) => {
    return {
        type: actionTypes.ADD_DEPARTMENT,
        result: res,
    };
};
export const getAllDepartments = (res) => {
    return {
        type: actionTypes.GET_ALL_DEPARTMENTS,
        result: res,
    };
};
export const getDisabledDepartments = (res) => {
    return {
        type: actionTypes.GET_DISABLED_DEPARTMENTS,
        result: res,
    };
};
export const setDisabledDepartment = (res) => {
    return {
        type: actionTypes.SET_DISABLED_DEPARTMENT,
        result: res,
    };
};
export const setEnabledDepartment = (res) => {
    return {
        type: actionTypes.SET_ENABLED_DEPARTMENT,
        result: res,
    };
};
export const clearDepartmentForm = () => {
    return {
        type: actionTypes.CLEAR_DEPARTMENT_FORM,
    };
};
export const deleteDepartment = (res) => {
    return {
        type: actionTypes.DELETE_DEPARTMENT,
        result: res,
    };
};
export const getDepartItemById = (res) => {
    return {
        type: actionTypes.GET_DEPARTMENT_BY_ID,
        result: res,
    };
};
export const updateDepart = (res) => {
    return {
        type: actionTypes.UPDATE_DEPARTMENT,
        result: res,
    };
};
