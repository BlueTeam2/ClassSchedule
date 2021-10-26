import { call, takeEvery, put, select } from 'redux-saga/effects';
import { reset } from 'redux-form';
import { has } from 'lodash';
import i18n from '../i18n';
import { sortGroup } from '../helper/sortGroup';
import { GROUP_FORM } from '../constants/reduxForms';
import * as actionTypes from '../actions/actionsType';
import { setLoading } from '../actions/loadingIndicator';
import { errorHandler, successHandler } from '../helper/handlerAxios';
import { FORM_GROUP_LABEL } from '../constants/translationLabels/formElements';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import {
    showAllGroups,
    deleteGroup,
    updateGroup,
    selectGroup,
    clearGroup,
    addGroup,
} from '../actions';
import { DISABLED_GROUPS_URL, GROUP_URL } from '../constants/axios';
import { axiosCall } from '../services/axios';

function* fetchDisabledGroupsWorker() {
    try {
        yield put(showAllGroups([]));
        yield put(setLoading(true));
        const res = yield call(axiosCall, DISABLED_GROUPS_URL, 'GET');
        yield put(showAllGroups(res.data.sort((a, b) => sortGroup(a, b))));
    } catch (err) {
        errorHandler(err);
    } finally {
        yield put(setLoading(false));
    }
}

function* fetchEnabledGroupsWorker() {
    try {
        yield put(showAllGroups([]));
        yield put(setLoading(true));
        const res = yield call(axiosCall, GROUP_URL, 'GET');
        yield put(showAllGroups(res.data.sort((a, b) => sortGroup(a, b))));
    } catch (err) {
        errorHandler(err);
    } finally {
        yield put(setLoading(false));
    }
}

function* createGroupWorker({ data }) {
    try {
        const res = yield call(axiosCall, GROUP_URL, 'POST', data);
        yield put(addGroup(res.data));
        yield put(reset(GROUP_FORM));
        successHandler(
            i18n.t(BACK_END_SUCCESS_OPERATION, {
                cardType: i18n.t(FORM_GROUP_LABEL),
                actionType: i18n.t(CREATED_LABEL),
            }),
        );
    } catch (err) {
        errorHandler(err);
    }
}

function* updateGroupWorker({ data }) {
    try {
        const res = yield call(axiosCall, GROUP_URL, 'PUT', data);
        if (has(data, 'disable')) {
            yield put(deleteGroup(data.id));
        } else {
            yield put(updateGroup(res.data));
        }
        yield put(selectGroup(null));
        yield put(reset(GROUP_FORM));
        successHandler(
            i18n.t(BACK_END_SUCCESS_OPERATION, {
                cardType: i18n.t(FORM_GROUP_LABEL),
                actionType: i18n.t(UPDATED_LABEL),
            }),
        );
    } catch (err) {
        errorHandler(err);
    }
}

function* deleteGroupWorker({ id }) {
    try {
        yield call(axiosCall, `${GROUP_URL}/${id}`, 'DELETE');
        yield put(deleteGroup(id));
        successHandler(
            i18n.t(BACK_END_SUCCESS_OPERATION, {
                cardType: i18n.t(FORM_GROUP_LABEL),
                actionType: i18n.t(DELETED_LABEL),
            }),
        );
    } catch (err) {
        errorHandler(err);
    }
}

function* toggleDisabledGroupWorker({ groupId, disabledStatus }) {
    try {
        if (groupId) {
            const state = yield select();
            const group = state.groups.groups.find((item) => item.id === groupId);
            yield call(updateGroupWorker, { data: { ...group, disable: !disabledStatus } });
        }
    } catch (err) {
        errorHandler(err);
    }
}

function* clearGroupWorker() {
    try {
        yield put(clearGroup());
        yield put(reset(GROUP_FORM));
    } catch (err) {
        errorHandler(err);
    }
}

export default function* groupWatcher() {
    yield takeEvery(actionTypes.TOGGLE_DISABLED_STATUS_GROUP, toggleDisabledGroupWorker);
    yield takeEvery(actionTypes.FETCH_DISABLED_GROUPS, fetchDisabledGroupsWorker);
    yield takeEvery(actionTypes.FETCH_ENABLED_GROUPS, fetchEnabledGroupsWorker);
    yield takeEvery(actionTypes.START_DELETE_GROUP, deleteGroupWorker);
    yield takeEvery(actionTypes.START_CREATE_GROUP, createGroupWorker);
    yield takeEvery(actionTypes.START_UPDATE_GROUP, updateGroupWorker);
    yield takeEvery(actionTypes.START_CLEAR_GROUP, clearGroupWorker);
}
