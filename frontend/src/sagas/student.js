import { reset } from 'redux-form';
import { call, put, select, takeEvery } from 'redux-saga/effects';
import { omit } from 'lodash';
import * as actionTypes from '../actions/actionsType';
import { setLoading, setStudentsLoading } from '../actions/loadingIndicator';
import { setOpenErrorSnackbar, setOpenSuccessSnackbar } from '../actions/snackbar';
import {
    deleteAllStudentSuccess,
    deleteStudentSuccess,
    selectStudentSuccess,
    showAllStudents,
    updateStudentSuccess,
} from '../actions/students';
import {
    GROUP_URL,
    MOVE_STUDENTS_URL,
    STUDENTS_TO_GROUP_FILE,
    STUDENT_URL,
    WITH_STUDENTS,
} from '../constants/axios';
import { DELETE, POST, PUT } from '../constants/methods';
import { STUDENT } from '../constants/names';
import { STUDENT_FORM } from '../constants/reduxForms';
import {
    FORM_STUDENTS_FILE_LABEL,
    STUDENTS_UPPERCASE,
} from '../constants/translationLabels/formElements';
import {
    BACK_END_SUCCESS_OPERATION,
    CREATED_LABEL,
    DELETED_LABEL,
    FILE_BACK_END_SUCCESS_OPERATION,
    FILE_LABEL,
    MOVED_TO_GROUP_LABEL,
    UPDATED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { axiosCall } from '../services/axios';
import { createDynamicMessage, createErrorMessage, createMessage } from '../utils/sagaUtils';

const getStudents = (state) => state.students.students;

function* getAllStudents({ id }) {
    try {
        yield put(showAllStudents([]));
        yield put(setStudentsLoading(true));
        const requestUrl = `${GROUP_URL}/${id}${WITH_STUDENTS}`;
        const res = yield call(axiosCall, requestUrl);
        yield put(showAllStudents(res.data.students));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    } finally {
        yield put(setStudentsLoading(false));
    }
}

function* createStudent({ data }) {
    try {
        yield call(axiosCall, STUDENT_URL, POST, data);
        yield put(reset(STUDENT_FORM));
        const message = createDynamicMessage(STUDENT, CREATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* updateStudent({ data, groupId }) {
    try {
        const res = yield call(axiosCall, STUDENT_URL, PUT, data);
        if (data.group.id === +groupId) {
            yield put(updateStudentSuccess(res.data));
        } else {
            yield put(deleteStudentSuccess(data.id));
        }
        yield put(selectStudentSuccess(data.id));
        yield put(reset(STUDENT_FORM));
        const message = createDynamicMessage(STUDENT, UPDATED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* submitStudentForm({ data, groupId }) {
    try {
        if (!data.id) {
            const newStudent = { ...data, group: { id: groupId } };
            yield call(createStudent, { data: newStudent });
        } else {
            const updatedStudent = { ...data, group: { id: data.group } };
            yield call(updateStudent, { data: updatedStudent, groupId });
        }
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* deleteStudent({ id }) {
    try {
        yield call(axiosCall, `${STUDENT_URL}/${id}`, DELETE);
        yield put(deleteStudentSuccess(id));
        const message = createDynamicMessage(STUDENT, DELETED_LABEL);
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

function* uploadStudentsToGroup({ file, id }) {
    try {
        yield put(setLoading(true));
        const formData = new FormData();
        formData.append('file', file);
        yield call(axiosCall, `${STUDENTS_TO_GROUP_FILE}${id}`, POST, formData);
        const message = createMessage(
            FILE_BACK_END_SUCCESS_OPERATION,
            FORM_STUDENTS_FILE_LABEL,
            FILE_LABEL,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    } finally {
        yield put(setLoading(false));
    }
}

function* moveStudentsToGroup({ group }) {
    try {
        const allStudents = yield select(getStudents);
        const movedStudents = allStudents.filter((item) => item.checked === true);
        const students = movedStudents.map((student) => omit(student, ['checked']));
        yield call(axiosCall, MOVE_STUDENTS_URL, PUT, { students, groupId: group.id });
        yield put(deleteAllStudentSuccess(students));
        const message = createMessage(
            STUDENTS_UPPERCASE,
            MOVED_TO_GROUP_LABEL,
            BACK_END_SUCCESS_OPERATION,
        );
        yield put(setOpenSuccessSnackbar(message));
    } catch (err) {
        yield put(setOpenErrorSnackbar(createErrorMessage(err)));
    }
}

export default function* studentWatcher() {
    yield takeEvery(actionTypes.UPLOAD_FILE_STUDENT_START, uploadStudentsToGroup);
    yield takeEvery(actionTypes.MOVE_STUDENTS_TO_GROUP_START, moveStudentsToGroup);
    yield takeEvery(actionTypes.SUBMIT_STUDENT_FORM, submitStudentForm);
    yield takeEvery(actionTypes.CREATE_STUDENT_START, createStudent);
    yield takeEvery(actionTypes.UPDATE_STUDENT_START, updateStudent);
    yield takeEvery(actionTypes.DELETE_STUDENT_START, deleteStudent);
    yield takeEvery(actionTypes.GET_ALL_STUDENTS, getAllStudents);
}
