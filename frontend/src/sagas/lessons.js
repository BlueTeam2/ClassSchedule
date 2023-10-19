import { reset } from 'redux-form';
import { call, put, takeLatest } from 'redux-saga/effects';
import * as actionTypes from '../actions/actionsType';
import {
    createLessonSuccess,
    selectLessonCardSuccess,
    updateLessonCardSuccess,
    deleteLessonCardSuccess,
    setLessonsCardsStart,
    setLoading,
    setLessonTypesSuccess,
} from '../actions';
import { setOpenErrorSnackbar, setOpenSuccessSnackbar } from '../actions/snackbar';
import { createErrorMessage, createMessage } from '../utils/sagaUtils';
import { DELETE, POST, PUT, GET } from '../constants/methods';
import { LESSON_FORM } from '../constants/reduxForms';
import { FORM_LESSON_LABEL } from '../constants/translationLabels/formElements';
import { LESSON_URL, COPY_LESSON_URL, LESSON_TYPES_URL } from '../constants/axios';
import {
    BACK_END_SUCCESS_OPERATION,
    CREATED_LABEL,
    UPDATED_LABEL,
    DELETED_LABEL,
    COPIED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { axiosCall } from '../services/axios';
import { handleFormSubmit } from '../helper/handleFormSubmit';

export function* createLessonCard({ values, groupId }) {
    try {
        const { data } = yield call(axiosCall, LESSON_URL, POST, values);
        const dataForThisGroup = data.filter((item) => item.group.id === groupId);

        yield put(createLessonSuccess(dataForThisGroup));
        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_LESSON_LABEL, CREATED_LABEL);

        yield put(reset(LESSON_FORM));
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* updateLesson({ values, groupId }) {
    try {
        const { groups, ...rest } = values;

        const request = {
            ...rest,
            group: { id: groupId },
        };
        const { data } = yield call(axiosCall, LESSON_URL, PUT, request);

        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_LESSON_LABEL, UPDATED_LABEL);

        yield put(updateLessonCardSuccess(data));
        yield put(selectLessonCardSuccess(null));
        yield put(reset(LESSON_FORM));
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* handleLesson({ payload }) {
    const { values, groupId } = payload;
    try {
        yield call(handleFormSubmit(values, createLessonCard, updateLesson), { values, groupId });
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* removeLessonCard({ id }) {
    try {
        const requestUrl = `${LESSON_URL}/${id}`;
        yield call(axiosCall, requestUrl, DELETE);

        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_LESSON_LABEL, DELETED_LABEL);

        yield put(deleteLessonCardSuccess(id));
        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* copyLessonCard({ payload }) {
    const { group, lesson } = payload;
    const groupList = group.map((groupItem) => {
        return groupItem.id;
    });
    try {
        const requestUrl = `${COPY_LESSON_URL}?lessonId=${lesson.id}`;
        yield call(axiosCall, requestUrl, POST, groupList);

        const message = createMessage(BACK_END_SUCCESS_OPERATION, FORM_LESSON_LABEL, COPIED_LABEL);

        yield put(setOpenSuccessSnackbar(message));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export function* getLessonsByGroup({ id }) {
    try {
        yield put(setLoading(true));

        const requestUrl = `${LESSON_URL}?groupId=${Number(id)}`;
        const { data } = yield call(axiosCall, requestUrl, GET, id);

        yield put(setLessonsCardsStart(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    } finally {
        yield put(setLoading(false));
    }
}

export function* getLessonTypes() {
    try {
        const { data } = yield call(axiosCall, LESSON_TYPES_URL, GET);

        yield put(setLessonTypesSuccess(data));
    } catch (error) {
        yield put(setOpenErrorSnackbar(createErrorMessage(error)));
    }
}

export default function* watchLessons() {
    yield takeLatest(actionTypes.GET_LESSON_TYPES_START, getLessonTypes);
    yield takeLatest(actionTypes.GET_LESSONS_CARDS_START, getLessonsByGroup);
    yield takeLatest(actionTypes.COPY_LESSON_START, copyLessonCard);
    yield takeLatest(actionTypes.DELETE_LESSON_CARD_START, removeLessonCard);
    yield takeLatest(actionTypes.CREATE_LESSON_CARD_START, createLessonCard);
    yield takeLatest(actionTypes.UPDATE_LESSON_CARD_START, updateLesson);
    yield takeLatest(actionTypes.HANDLE_LESSON_CARD_START, handleLesson);
}
