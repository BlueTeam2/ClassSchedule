import { reset } from 'redux-form';
import { call, put, takeLatest, takeEvery, select } from 'redux-saga/effects';

import * as actionTypes from '../actions/actionsType';
import { setScheduleLoading } from '../actions';

import { setLoading, setRoomsLoading } from '../actions/loadingIndicator';
import { setOpenSuccessSnackbar, setOpenErrorSnackbar } from '../actions/snackbar';
import { axiosCall } from '../services/axios';
import {
    ROOM_URL,
    ROOM_ORDERED_URL,
    DISABLED_ROOMS_URL,
    ROOM_TYPES_URL,
    FREE_ROOMS_URL,
    BUSY_ROOMS,
    CURRENT_SEMESTER_URL,
} from '../constants/axios';
import { ROOM_FORM, ROOM_FORM_TYPE } from '../constants/reduxForms';
import { createErrorMessage, createMessage } from '../utils/sagaUtils';
import {
    updateRoomSuccess,
    addRoomSuccess,
    getListOfRoomsSuccess,
    getListOfDisabledRoomsSuccess,
    deleteRoomSuccess,
    getAllRoomTypesSuccess,
    deleteRoomTypeSuccess,
    updateRoomTypeSuccess,
    addRoomTypeSuccess,
    getFreeRoomsSuccess,
    getBusyRoomsSuccess,
} from '../actions/rooms';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { FORM_ROOM_LABEL, FORM_TYPE_LABEL } from '../constants/translationLabels/formElements';
import { POST, DELETE, PUT } from '../constants/methods';

export function* getListOfRooms() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, ROOM_ORDERED_URL);
        yield put(getListOfRoomsSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* getListOfDisabledRooms() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, DISABLED_ROOMS_URL);
        yield put(getListOfDisabledRoomsSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* updateRoomItem({ values }) {
    try {
        const { data } = yield call(axiosCall, ROOM_URL, PUT, values);
        yield put(updateRoomSuccess(data));
        yield put(reset(ROOM_FORM));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_ROOM_LABEL, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* addRoomItem({ values }) {
    try {
        const { data } = yield call(axiosCall, ROOM_URL, POST, values);
        yield put(addRoomSuccess(data));
        yield put(reset(ROOM_FORM));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_ROOM_LABEL, CREATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* toggleRoomsVisibility({ roomId, isDisabled }) {
    try {
        const state = yield select();
        const { disabledRooms, rooms } = state.rooms;
        const room = [...disabledRooms, ...rooms].find((roomItem) => roomItem.id === roomId);
        yield call(axiosCall, ROOM_URL, PUT, { ...room, disable: !room.disable });
        yield put(deleteRoomSuccess(roomId, isDisabled));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_ROOM_LABEL, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* deleteRoomItem({ roomId, isDisabled }) {
    try {
        const requestUrl = `${ROOM_URL}/${roomId}`;
        yield call(axiosCall, requestUrl, DELETE);
        yield put(deleteRoomSuccess(roomId, isDisabled));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_ROOM_LABEL, DELETED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* handleRoomFormSubmit({ values }) {
    try {
        if (values.id) {
            yield call(updateRoomItem, { values });
        } else {
            yield call(addRoomItem, { values });
        }
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getAllRoomTypes() {
    try {
        const { data } = yield call(axiosCall, ROOM_TYPES_URL);
        yield put(getAllRoomTypesSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* updateRoomTypeItem({ values }) {
    try {
        const { data } = yield call(axiosCall, ROOM_TYPES_URL, PUT, values);
        yield put(updateRoomTypeSuccess(data));
        yield put(reset(ROOM_FORM_TYPE));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_TYPE_LABEL, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* addRoomTypeItem({ values }) {
    try {
        const { data } = yield call(axiosCall, ROOM_TYPES_URL, POST, values);
        yield put(addRoomTypeSuccess(data));
        yield put(reset(ROOM_FORM_TYPE));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_TYPE_LABEL, CREATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}
export function* handleRoomTypeFormSubmit({ values }) {
    try {
        if (values.id) {
            yield call(updateRoomTypeItem, { values });
        } else {
            yield call(addRoomTypeItem, { values });
        }
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* deleteRoomTypeItem({ roomTypeId }) {
    try {
        const requestUrl = `${ROOM_TYPES_URL}/${roomTypeId}`;
        yield call(axiosCall, requestUrl, DELETE);
        yield put(deleteRoomTypeSuccess(roomTypeId));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_TYPE_LABEL, DELETED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getFreeRoomsByParams({ params }) {
    try {
        const { dayOfWeek, evenOdd, classId, semesterId } = params;
        const requestUrl = `${FREE_ROOMS_URL}?dayOfWeek=${dayOfWeek}&evenOdd=${evenOdd}&classId=${classId}&semesterId=${semesterId}`;
        const { data } = yield call(axiosCall, requestUrl);
        yield put(getFreeRoomsSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setRoomsLoading(false));
    }
}

export function* getBusyRooms() {
    try {
        const semesterResponse = yield call(axiosCall, CURRENT_SEMESTER_URL);
        const { id } = semesterResponse.data;
        const { data } = yield call(axiosCall, `${BUSY_ROOMS}?semesterId=${id}`);
        yield put(getBusyRoomsSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setScheduleLoading(false));
    }
}

function* watchRooms() {
    yield takeLatest(actionTypes.GET_LIST_OF_ROOMS_START, getListOfRooms);
    yield takeLatest(actionTypes.GET_LIST_OF_DISABLED_ROOMS_START, getListOfDisabledRooms);
    yield takeLatest(actionTypes.GET_ALL_ROOM_TYPES_START, getAllRoomTypes);
    yield takeLatest(actionTypes.GET_FREE_ROOMS_START, getFreeRoomsByParams);
    yield takeEvery(actionTypes.ADD_ROOM_START, addRoomItem);
    yield takeEvery(actionTypes.UPDATE_ROOM_START, updateRoomItem);
    yield takeEvery(actionTypes.DELETE_ROOM_START, deleteRoomItem);
    yield takeEvery(actionTypes.DELETE_ROOM_TYPE_START, deleteRoomTypeItem);
    yield takeEvery(actionTypes.HANDLE_ROOM_FORM_SUBMIT_START, handleRoomFormSubmit);
    yield takeEvery(actionTypes.HANDLE_ROOM_TYPE_FORM_SUBMIT_START, handleRoomTypeFormSubmit);
    yield takeEvery(actionTypes.TOGGLE_ROOM_VISIBILITY_START, toggleRoomsVisibility);
    yield takeLatest(actionTypes.GET_BUSY_ROOMS_START, getBusyRooms);
}

export default watchRooms;
