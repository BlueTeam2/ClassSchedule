import * as actionTypes from './actionsType';

export const createLessonSuccess = (result) => ({
    type: actionTypes.CREATE_LESSON_CARD_SUCCESS,
    result,
});
export const getLessonsByGroupStart = (id) => ({
    type: actionTypes.GET_LESSONS_CARDS_START,
    id,
});

export const setLessonsCardsStart = (result) => ({
    type: actionTypes.SET_LESSONS_CARDS_START,
    result,
});

export const getLessonTypesStart = () => ({
    type: actionTypes.GET_LESSON_TYPES_START,
});

export const setLessonTypesSuccess = (result) => ({
    type: actionTypes.SET_LESSON_TYPES_SUCCESS,
    result,
});

export const deleteLessonCardSuccess = (id) => ({
    type: actionTypes.DELETE_LESSON_CARD_SUCCESS,
    id,
});

export const deleteLessonCardStart = (id) => ({
    type: actionTypes.DELETE_LESSON_CARD_START,
    id,
});

export const selectLessonCardSuccess = (cardId) => ({
    type: actionTypes.SELECT_LESSON_CARD_SUCCESS,
    cardId,
});

export const updateLessonCardSuccess = (result) => ({
    type: actionTypes.UPDATE_LESSON_CARD_SUCCESS,
    result,
});

export const copyLessonCardStart = (payload) => ({
    type: actionTypes.COPY_LESSON_START,
    payload,
});

export const handleLessonStart = (payload) => ({
    type: actionTypes.HANDLE_LESSON_CARD_START,
    payload,
});

export const selectGroupId = (id) => ({
    type: actionTypes.SELECT_GROUP_ID,
    id,
});

export const setUniqueError = (result) => ({
    type: actionTypes.SET_UNIQUE_ERROR,
    result,
});
