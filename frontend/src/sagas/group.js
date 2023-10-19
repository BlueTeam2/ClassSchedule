import { call, takeEvery, put, select, takeLatest } from 'redux-saga/effects';
import { reset } from 'redux-form';
import { GROUP_FORM } from '../constants/reduxForms';
import * as actionTypes from '../actions/actionsType';
import { setLoading } from '../actions/loadingIndicator';
import { createErrorMessage, createDynamicMessage, createMessage } from '../utils/sagaUtils';
import {
    setOpenSuccessSnackbar,
    setOpenErrorSnackbar,
    setOpenInfoSnackbar,
} from '../actions/snackbar';
import {
    GROUP_URL,
    GROUPS_URL,
    SEMESTERS_URL,
    GROUPS_AFTER_URL,
    DISABLED_GROUPS_URL,
} from '../constants/axios';
import { DELETE, POST, PUT } from '../constants/methods';
import { axiosCall } from '../services/axios';
import { FORM_CHOSEN_SEMESTER_LABEL } from '../constants/translationLabels/formElements';
import {
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
    CHOSEN_SEMESTER_HAS_NOT_GROUPS,
    SERVICE_MESSAGE_GROUP_LABEL,
} from '../constants/translationLabels/serviceMessages';
import {
    getGroupByIdSuccess,
    showAllGroupsSuccess,
    deleteGroupSuccess,
    updateGroupSuccess,
    selectGroupSuccess,
    clearGroupSuccess,
    createGroupSuccess,
} from '../actions';
import { GROUP } from '../constants/names';
import { handleFormSubmit } from '../helper/handleFormSubmit';

const getGroupsState = (state) => state.groups.groups;

function* getGroups(url) {
    try {
        yield put(setLoading(true));
        const res = yield call(axiosCall, url);
        yield put(showAllGroupsSuccess(res.data));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    } finally {
        yield put(setLoading(false));
    }
}

function* getGroupById({ id }) {
    try {
        yield put(selectGroupSuccess({}));
        const res = yield call(axiosCall, `${GROUP_URL}${id}`);
        yield put(getGroupByIdSuccess(res.data));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* getDisabledGroups() {
    yield call(getGroups, DISABLED_GROUPS_URL);
}

function* getEnabledGroups() {
    yield call(getGroups, GROUPS_URL);
}

function* createGroup({ data }) {
    try {
        const res = yield call(axiosCall, GROUPS_AFTER_URL, POST, data);
        yield put(createGroupSuccess(res.data, data.afterId));
        yield put(reset(GROUP_FORM));
        const message = createDynamicMessage(GROUP, CREATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* updateGroup({ data, url }) {
    try {
        const res = yield call(axiosCall, url, PUT, data);
        yield put(updateGroupSuccess(res.data, data.afterId));
        yield put(selectGroupSuccess(null));
        const message = createDynamicMessage(GROUP, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
        yield put(reset(GROUP_FORM));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* submitGroupForm({ group }) {
    try {
        const url = GROUPS_AFTER_URL;
        yield call(handleFormSubmit(group, createGroup, updateGroup), {
            data: group,
            url,
        });
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* deleteGroup({ id }) {
    try {
        yield call(axiosCall, `${GROUP_URL}/${id}`, DELETE);
        yield put(deleteGroupSuccess(id));
        const message = createDynamicMessage(GROUP, DELETED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* dragAndDropGroup({ dragGroup, afterGroupId }) {
    try {
        yield put(setLoading(true));
        const url = GROUPS_AFTER_URL;
        yield call(updateGroup, {
            data: { ...dragGroup, afterId: afterGroupId },
            url,
        });
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    } finally {
        yield put(setLoading(false));
    }
}

function* toggleDisabledGroup({ groupId }) {
    try {
        if (groupId) {
            const url = GROUP_URL;
            const groups = yield select(getGroupsState);
            const group = groups.find((item) => item.id === groupId);
            yield call(updateGroup, { data: { ...group, disable: !group.disable }, url });
            yield put(deleteGroupSuccess(groupId));
        }
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* clearGroup() {
    try {
        yield put(clearGroupSuccess());
        yield put(reset(GROUP_FORM));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

export function* getAllPublicGroups({ id }) {
    try {
        const requestUrl = `/${SEMESTERS_URL}/${id}/${GROUPS_URL}`;
        const { data } = yield call(axiosCall, requestUrl);
        yield put(showAllGroupsSuccess(data));
        if (data.length === 0) {
            const message = createMessage(
                CHOSEN_SEMESTER_HAS_NOT_GROUPS,
                FORM_CHOSEN_SEMESTER_LABEL,
                SERVICE_MESSAGE_GROUP_LABEL,
            );
            yield put(setOpenInfoSnackbar(message));
        }
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export default function* groupWatcher() {
    yield takeEvery(actionTypes.DELETE_GROUP_START, deleteGroup);
    yield takeEvery(actionTypes.CLEAR_GROUP_START, clearGroup);
    yield takeEvery(actionTypes.GET_GROUP_BY_ID_START, getGroupById);
    yield takeEvery(actionTypes.SUBMIT_GROUP_START, submitGroupForm);
    yield takeLatest(actionTypes.GET_ENABLED_GROUPS_START, getEnabledGroups);
    yield takeEvery(actionTypes.DRAG_AND_DROP_GROUP_START, dragAndDropGroup);
    yield takeLatest(actionTypes.GET_DISABLED_GROUPS_START, getDisabledGroups);
    yield takeLatest(actionTypes.GET_ALL_PUBLIC_GROUPS_START, getAllPublicGroups);
    yield takeEvery(actionTypes.TOGGLE_DISABLED_STATUS_GROUP, toggleDisabledGroup);
}
