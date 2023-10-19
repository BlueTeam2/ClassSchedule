import { reset } from 'redux-form';
import { isEmpty } from 'lodash';
import { call, put, takeLatest, select } from 'redux-saga/effects';
import { axiosCall } from '../services/axios';

import {
    DISABLED_TEACHERS_URL,
    TEACHERS_WITHOUT_ACCOUNT_URL,
    PUBLIC_TEACHER_URL,
    TEACHER_URL,
    DEPARTMENT_URL,
} from '../constants/axios';
import {
    BACK_END_SUCCESS_OPERATION,
    CREATED_LABEL,
    DELETED_LABEL,
    UPDATED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { TEACHER_FORM } from '../constants/reduxForms';
import { DELETE, GET, POST, PUT } from '../constants/methods';
import { FORM_TEACHER_A_LABEL } from '../constants/translationLabels/formElements';
import { createErrorMessage, createMessage } from '../utils/sagaUtils';
import {
    setLoading,
    deleteTeacherSuccess,
    showAllTeachersSuccess,
    setDisabledTeachersSuccess,
} from '../actions';
import { setOpenErrorSnackbar, setOpenSuccessSnackbar } from '../actions/snackbar';
import * as actionTypes from '../actions/actionsType';
import {
    selectTeacherCard,
    addTeacherSuccess,
    updateTeacherCardSuccess,
    getTeacherWithoutAccountSuccess,
    getAllTeachersByDepartmentId,
} from '../actions/teachers';
import { handleFormSubmit } from '../helper/handleFormSubmit';

export function* getEnabledTeachers() {
    try {
        yield put(setLoading(true));
        const { data } = yield call(axiosCall, TEACHER_URL, GET);

        yield put(showAllTeachersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* getDisabledTeachers() {
    try {
        const { data } = yield call(axiosCall, DISABLED_TEACHERS_URL, GET);

        yield put(setDisabledTeachersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* removeTeacher({ id }) {
    try {
        const requestUrl = `${TEACHER_URL}/${id}`;
        yield call(axiosCall, requestUrl, DELETE);

        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_TEACHER_A_LABEL,
            DELETED_LABEL,
        );

        yield put(deleteTeacherSuccess(id));
        yield call(getDisabledTeachers);
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* createTeacher({ teacher }) {
    const results = { ...teacher };
    if (isEmpty(teacher.department) || !teacher.department?.id) {
        delete results.department;
    }

    try {
        const { data } = yield call(axiosCall, TEACHER_URL, POST, results);

        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_TEACHER_A_LABEL,
            CREATED_LABEL,
        );

        yield put(addTeacherSuccess(data));
        yield put(reset(TEACHER_FORM));
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* updateTeacher({ teacher }) {
    const result = { ...teacher };

    if (isEmpty(teacher.department) || !teacher.department?.id) {
        delete result.department;
    }
    try {
        const { data } = yield call(axiosCall, TEACHER_URL, PUT, result);

        yield put(updateTeacherCardSuccess(data));

        yield call(getEnabledTeachers);
        yield call(getDisabledTeachers);
        yield put(selectTeacherCard(null));

        yield put(reset(TEACHER_FORM));
        const message = createMessage(
            BACK_END_SUCCESS_OPERATION,
            FORM_TEACHER_A_LABEL,
            UPDATED_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

function* toggleDisabledTeacher({ teacherId, disableStatus }) {
    try {
        const state = yield select();
        const { teachers, disabledTeachers } = state.teachers;
        const teacher = [...teachers, ...disabledTeachers].find((item) => item.id === teacherId);
        yield call(updateTeacher, { teacher: { ...teacher, disable: !disableStatus } });
        yield put(deleteTeacherSuccess(teacherId, disableStatus));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* handleTeacher({ values }) {
    try {
        yield call(handleFormSubmit(values, createTeacher, updateTeacher), { teacher: values });
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getAllPublicTeachers() {
    try {
        const { data } = yield call(axiosCall, PUBLIC_TEACHER_URL);
        yield put(showAllTeachersSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getTeachersWithoutAccount() {
    try {
        const { data } = yield call(axiosCall, TEACHERS_WITHOUT_ACCOUNT_URL, GET);

        yield put(getTeacherWithoutAccountSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getAllPublicTeachersByDepartment({ departmentId }) {
    const requestUrl = `${DEPARTMENT_URL}/${departmentId}/${TEACHER_URL}`;
    try {
        const { data } = yield call(axiosCall, requestUrl);
        yield put(getAllTeachersByDepartmentId(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export default function* watchTeachers() {
    yield takeLatest(actionTypes.DELETE_TEACHER_START, removeTeacher);
    yield takeLatest(actionTypes.SHOW_ALL_TEACHERS_START, getEnabledTeachers);
    yield takeLatest(actionTypes.SET_DISABLED_TEACHERS_START, getDisabledTeachers);
    yield takeLatest(actionTypes.GET_TEACHERS_WITHOUT_ACCOUNT_START, getTeachersWithoutAccount);
    yield takeLatest(actionTypes.ADD_TEACHER_START, createTeacher);
    yield takeLatest(actionTypes.UPDATE_TEACHER_START, updateTeacher);
    yield takeLatest(actionTypes.HANDLE_TEACHER_START, handleTeacher);
    yield takeLatest(actionTypes.TOOGLE_TEACHER_START, toggleDisabledTeacher);
    yield takeLatest(actionTypes.GET_ALL_PUBLIC_TEACHERS_START, getAllPublicTeachers);
    yield takeLatest(
        actionTypes.GET_ALL_PUBLIC_TEACHERS_BY_DEPARTMENT_START,
        getAllPublicTeachersByDepartment,
    );
}
