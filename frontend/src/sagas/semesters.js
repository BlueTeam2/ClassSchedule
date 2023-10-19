import { call, put, takeLatest, takeEvery, select } from 'redux-saga/effects';
import { reset } from 'redux-form';
import * as actionTypes from '../actions/actionsType';
import { setLoading } from '../actions/loadingIndicator';
import { setOpenSuccessSnackbar, setOpenErrorSnackbar } from '../actions/snackbar';
import {
    getAllSemestersSuccess,
    getArchivedSemestersSuccess,
    updateSemesterSuccess,
    selectSemesterSuccess,
    deleteSemesterSuccess,
    addSemesterSuccess,
    moveToArchivedSemesterSuccess,
} from '../actions/semesters';
import { setScheduleType, getFullScheduleSuccess } from '../actions/schedule';
import { axiosCall } from '../services/axios';
import i18n from '../i18n';
import {
    DISABLED_SEMESTERS_URL,
    SEMESTERS_URL,
    SEMESTER_COPY_URL,
    LESSONS_FROM_SEMESTER_COPY_URL,
    ARCHIVE_SEMESTER,
    ARCHIVED_SEMESTERS_URL,
    DEFAULT_SEMESTER_URL,
} from '../constants/axios';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
    SEMESTER_SERVICE_IS_ACTIVE,
    COPIED_LABEL,
    ARCHIVED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import {
    FORM_LESSON_LABEL,
    FORM_SEMESTER_LABEL,
} from '../constants/translationLabels/formElements';
import { SEMESTER_FORM } from '../constants/reduxForms';
import { createErrorMessage, createMessage } from '../utils/sagaUtils';
import { handleFormSubmit } from '../helper/handleFormSubmit';
import { POST, DELETE, PUT } from '../constants/methods';

const getSemestersState = (state) => state.semesters.semesters;

export function* getAllSemestersItems() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, SEMESTERS_URL);
        yield put(getAllSemestersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* getDisabledSemestersItems() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, DISABLED_SEMESTERS_URL);
        yield put(getAllSemestersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* getArchivedSemestersItems() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, ARCHIVED_SEMESTERS_URL);
        yield put(getArchivedSemestersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* setGroupsToSemester({ semesterId, groups }) {
    try {
        const groupIds = groups.map((item) => item.id);
        const requestUrl = `${SEMESTERS_URL}/${semesterId}/groups`;
        const { data } = yield call(axiosCall, requestUrl, PUT, groupIds);
        yield put(updateSemesterSuccess(data));
        yield put(selectSemesterSuccess(null));
        yield put(reset(SEMESTER_FORM));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            UPDATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* deleteSemesterItem({ semesterId }) {
    try {
        const semesters = yield select(getSemestersState);
        const semester = semesters.find((item) => item.id === semesterId);
        if (semester.currentSemester) {
            const message = i18n.t(SEMESTER_SERVICE_IS_ACTIVE);
            yield put(setOpenErrorSnackbar(message));
        } else {
            const requestUrl = `${SEMESTERS_URL}/${semesterId}`;
            yield call(axiosCall, requestUrl, DELETE);
            yield put(deleteSemesterSuccess(semesterId));
            const message = createMessage(
                BACK_END_SUCCESS_OPERATION,
                FORM_SEMESTER_LABEL,
                DELETED_LABEL,
            );
            yield put(setOpenSuccessSnackbar(message));
        }
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* updateSemesterItem({ values }) {
    try {
        const { data } = yield call(axiosCall, SEMESTERS_URL, PUT, values);
        yield put(updateSemesterSuccess(data));
        yield put(selectSemesterSuccess(null));
        yield put(reset(SEMESTER_FORM));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            UPDATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* addSemesterItem({ values }) {
    try {
        const { data } = yield call(axiosCall, SEMESTERS_URL, POST, values);
        yield put(addSemesterSuccess(data));
        yield put(reset(SEMESTER_FORM));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            CREATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}
function* setPreviousDefaultSemesterToFalse({ values }) {
    const semesters = yield select(getSemestersState);
    const oldDefaultSemester = {
        ...semesters.find(
            (semesterItem) => semesterItem.defaultSemester && semesterItem.id !== values.id,
        ),
    };
    if (values.defaultSemester && oldDefaultSemester) {
        oldDefaultSemester.defaultSemester = false;
        yield put(updateSemesterSuccess(oldDefaultSemester));
    }
}
function* setPreviousCurrentSemesterToFalse({ values }) {
    const semesters = yield select(getSemestersState);
    const oldCurrentSemester = {
        ...semesters.find(
            (semesterItem) => semesterItem.currentSemester && semesterItem.id !== values.id,
        ),
    };
    if (values.currentSemester && oldCurrentSemester) {
        oldCurrentSemester.currentSemester = false;
        yield put(updateSemesterSuccess(oldCurrentSemester));
    }
}
export function* handleSemesterFormSubmit({ values }) {
    try {
        yield call(handleFormSubmit(values, addSemesterItem, updateSemesterItem), { values });
        yield call(setPreviousCurrentSemesterToFalse, { values });
        yield call(setPreviousDefaultSemesterToFalse, { values });
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* setDefaultSemesterById({ semesterId }) {
    try {
        const semesters = yield select(getSemestersState);
        const oldDefaultSemester = {
            ...semesters.find(
                (semesterItem) => semesterItem.defaultSemester && semesterItem.id !== semesterId,
            ),
        };
        if (oldDefaultSemester) {
            oldDefaultSemester.defaultSemester = false;
            yield put(updateSemesterSuccess(oldDefaultSemester));
        }
        const requestUrl = `${DEFAULT_SEMESTER_URL}?semesterId=${semesterId}`;
        const { data } = yield call(axiosCall, requestUrl, PUT);
        yield put(updateSemesterSuccess(data));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            UPDATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* toggleSemesterVisibility({ semester }) {
    try {
        yield call(axiosCall, SEMESTERS_URL, PUT, semester);
        yield put(deleteSemesterSuccess(semester.id));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            UPDATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* semesterCopy({ values }) {
    try {
        const requestUrl = `${SEMESTER_COPY_URL}?fromSemesterId=${values.fromSemesterId}&toSemesterId=${values.toSemesterId}`;
        yield call(axiosCall, requestUrl, POST);
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            COPIED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* createArchiveSemester({ semesterId }) {
    try {
        const requestUrl = `${ARCHIVE_SEMESTER}/${semesterId}`;
        yield call(axiosCall, requestUrl, POST);
        yield put(moveToArchivedSemesterSuccess(semesterId));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_SEMESTER_LABEL,
            ARCHIVED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getArchivedSemesterById({ semesterId }) {
    try {
        yield put(setScheduleType('archived'));
        const requestUrl = `${ARCHIVE_SEMESTER}/${semesterId}`;
        const { data } = yield call(axiosCall, requestUrl);
        yield put(getFullScheduleSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* CopyLessonsFromSemester({ values }) {
    try {
        const requestUrl = `${LESSONS_FROM_SEMESTER_COPY_URL}?fromSemesterId=${values.fromSemesterId}&toSemesterId=${values.toSemesterId}`;
        yield call(axiosCall, requestUrl, POST);
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_LESSON_LABEL, COPIED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* watchSemester() {
    yield takeLatest(actionTypes.GET_ALL_SEMESTERS_START, getAllSemestersItems);
    yield takeLatest(actionTypes.GET_DISABLED_SEMESTERS_START, getDisabledSemestersItems);
    yield takeLatest(actionTypes.SET_ARCHIVED_SEMESTERS_START, getArchivedSemestersItems);
    yield takeLatest(actionTypes.GET_ARCHIVE_SEMESTER_BY_ID_START, getArchivedSemesterById);
    yield takeEvery(actionTypes.SET_GROUPS_TO_SEMESTER_START, setGroupsToSemester);
    yield takeEvery(actionTypes.DELETE_SEMESTER_START, deleteSemesterItem);
    yield takeEvery(actionTypes.UPDATE_SEMESTER_START, updateSemesterItem);
    yield takeEvery(actionTypes.ADD_SEMESTER_START, addSemesterItem);
    yield takeLatest(actionTypes.UPDATE_SEMESTER_BY_ID_START_SUCCESS, setDefaultSemesterById);
    yield takeEvery(actionTypes.SET_SEMESTER_COPY_START, semesterCopy);
    yield takeEvery(actionTypes.CREATE_ARCHIVE_SEMESTER_START, createArchiveSemester);
    yield takeEvery(actionTypes.COPY_LESSONS_FROM_SEMESTER_START, CopyLessonsFromSemester);
    yield takeEvery(actionTypes.HANDLE_SEMESTER_FORM_SUBMIT_START, handleSemesterFormSubmit);
    yield takeEvery(actionTypes.TOGGLE_SEMESTER_VISIBILITY_START, toggleSemesterVisibility);
}
