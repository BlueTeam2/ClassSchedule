import { reset } from 'redux-form';
import { call, put, takeLatest } from 'redux-saga/effects';
import { axiosCall } from '../services/axios';
import * as actionTypes from '../actions/actionsType';

import { CLASS_URL, PUBLIC_CLASSES_URL } from '../constants/axios';
import { CLASS_FORM } from '../constants/reduxForms';
import {
    addClassScheduleSuccess,
    getClassScheduleListSuccess,
    getClassScheduleByIdSuccess,
    deleteClassScheduleSuccess,
    updateClassScheduleSuccess,
    clearClassScheduleSuccess,
    getPublicClassScheduleSuccess,
} from '../actions/classes';

import { setLoading } from '../actions';

import { GET, PUT, POST, DELETE } from '../constants/methods';
import { setOpenSuccessSnackbar, setOpenErrorSnackbar } from '../actions/snackbar';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { FORM_CLASS_LABEL } from '../constants/translationLabels/formElements';
import { createErrorMessage, createMessage } from '../utils/sagaUtils';

export function* getClassScheduleList() {
    try {
        const response = yield call(axiosCall, CLASS_URL, GET);
        yield put(getClassScheduleListSuccess(response.data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}
export function* getPublicClassScheduleList() {
    try {
        const response = yield call(axiosCall, PUBLIC_CLASSES_URL, GET);
        yield put(getPublicClassScheduleSuccess(response.data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}
export function* updateClassSchedule({ item }) {
    try {
        const response = yield call(axiosCall, CLASS_URL, PUT, item);
        yield put(updateClassScheduleSuccess(response.data));
        yield put(reset(CLASS_FORM));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_CLASS_LABEL, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* addClassSchedule({ item }) {
    try {
        const response = yield call(axiosCall, CLASS_URL, POST, item);
        yield put(addClassScheduleSuccess(response.data));
        yield put(reset(CLASS_FORM));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_CLASS_LABEL, CREATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* classFormHandler({ item }) {
    try {
        if (item.id) {
            yield call(updateClassSchedule, { item });
        } else {
            yield call(addClassSchedule, { item });
        }
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getClassScheduleById({ id }) {
    try {
        yield put(getClassScheduleByIdSuccess(id));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* deleteClassSchedule({ id }) {
    try {
        yield call(axiosCall, `${CLASS_URL}/${id}`, DELETE);
        yield put(deleteClassScheduleSuccess(id));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_CLASS_LABEL, DELETED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* clearClassSchedule() {
    try {
        yield put(clearClassScheduleSuccess());
        yield put(reset(CLASS_FORM));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export default function* watchClasses() {
    yield takeLatest(actionTypes.GET_CLASS_SCHEDULE_BY_ID_START, getClassScheduleById);
    yield takeLatest(actionTypes.GET_CLASS_SCHEDULE_LIST_START, getClassScheduleList);
    yield takeLatest(actionTypes.GET_PUBLIC_CLASS_SCHEDULE_LIST_START, getPublicClassScheduleList);
    yield takeLatest(actionTypes.CLASS_FORM_HANDLER, classFormHandler);
    yield takeLatest(actionTypes.ADD_CLASS_SCHEDULE_START, addClassSchedule);
    yield takeLatest(actionTypes.UPDATE_CLASS_SCHEDULE_START, updateClassSchedule);
    yield takeLatest(actionTypes.DELETE_CLASS_SCHEDULE_START, deleteClassSchedule);
    yield takeLatest(actionTypes.CLEAR_CLASS_SCHEDULE_START, clearClassSchedule);
}
