import axios from '../helper/axios';
import { DEPARTMENT_URL } from '../constants/axios';
import { store } from '../store';
import { resetFormHandler } from '../helper/formHelper';
import { DEPARTMENT_FORM } from '../constants/reduxForms';
import { errorHandler, successHandler } from '../helper/handlerAxios';
import i18n from '../i18n';
import {
    addDepartment,
    clearDepartmentForm,
    deleteDepartment,
    getAllDepartments,
    getDepartItemById,
    getDisabledDepartments,
    setDisabledDepartment,
    setEnabledDepartment,
    updateDepart,
} from '../actions/departments';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { FORM_DEPARTMENT_LABEL } from '../constants/translationLabels/formElements';

export const createDepartmentService = (data) => {
    axios
        .post(DEPARTMENT_URL, data)
        .then((response) => {
            store.dispatch(addDepartment(response.data));
            resetFormHandler(DEPARTMENT_FORM);
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_DEPARTMENT_LABEL),
                    actionType: i18n.t(CREATED_LABEL),
                }),
            );
        })
        .catch((error) => errorHandler(error));
};

export const getAllDepartmentsService = () => {
    axios
        .get(DEPARTMENT_URL)
        .then((response) => {
            store.dispatch(getAllDepartments(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const getDisabledDepartmentsService = () => {
    axios
        .get(`${DEPARTMENT_URL}/disabled`)
        .then((response) => {
            store.dispatch(getDisabledDepartments(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const setDisabledDepartmentService = (data) => {
    axios
        .put(`${DEPARTMENT_URL}`, data)
        .then((response) => {
            store.dispatch(setDisabledDepartment(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const setEnabledDepartmentService = (data) => {
    axios
        .put(`${DEPARTMENT_URL}`, data)
        .then((response) => {
            store.dispatch(setEnabledDepartment(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const updateDepartmentService = (data) => {
    axios
        .put(`${DEPARTMENT_URL}`, data)
        .then((response) => {
            store.dispatch(updateDepart(response.data));
            resetFormHandler(DEPARTMENT_FORM);
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_DEPARTMENT_LABEL),
                    actionType: i18n.t(UPDATED_LABEL),
                }),
            );
        })
        .catch((error) => errorHandler(error));
};

export const deleteDepartmentsService = (id) => {
    axios
        .delete(`${DEPARTMENT_URL}/${id}`)
        .then((response) => {
            store.dispatch(deleteDepartment(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const clearDepartment = () => {
    store.dispatch(clearDepartmentForm());
    resetFormHandler(DEPARTMENT_FORM);
};

export const getDepartmentByIdService = (id) => {
    store.dispatch(getDepartItemById(Number(id)));
};
