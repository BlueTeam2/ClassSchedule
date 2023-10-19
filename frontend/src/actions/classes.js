import * as actionTypes from './actionsType';

export const classFormHandler = (item) => ({
    type: actionTypes.CLASS_FORM_HANDLER,
    item,
});

export const addClassScheduleStart = (item) => ({
    type: actionTypes.ADD_CLASS_SCHEDULE_START,
    item,
});

export const addClassScheduleSuccess = (classSchedule) => ({
    type: actionTypes.ADD_CLASS_SCHEDULE_SUCCESS,
    classSchedule,
});

export const getClassScheduleListStart = () => ({
    type: actionTypes.GET_CLASS_SCHEDULE_LIST_START,
});

export const getClassScheduleListSuccess = (classScheduler) => ({
    type: actionTypes.GET_CLASS_SCHEDULE_LIST_SUCCESS,
    classScheduler,
});

export const getClassScheduleByIdStart = (id) => ({
    type: actionTypes.GET_CLASS_SCHEDULE_BY_ID_START,
    id,
});

export const getClassScheduleByIdSuccess = (id) => ({
    type: actionTypes.GET_CLASS_SCHEDULE_BY_ID_SUCCESS,
    id,
});

export const deleteClassScheduleStart = (id) => ({
    type: actionTypes.DELETE_CLASS_SCHEDULE_START,
    id,
});

export const deleteClassScheduleSuccess = (id) => ({
    type: actionTypes.DELETE_CLASS_SCHEDULE_SUCCESS,
    id,
});

export const updateClassScheduleStart = (item) => ({
    type: actionTypes.UPDATE_CLASS_SCHEDULE_START,
    item,
});

export const updateClassScheduleSuccess = (classSchedule) => ({
    type: actionTypes.UPDATE_CLASS_SCHEDULE_SUCCESS,
    classSchedule,
});
export const clearClassScheduleStart = () => ({
    type: actionTypes.CLEAR_CLASS_SCHEDULE_START,
});

export const clearClassScheduleSuccess = () => ({
    type: actionTypes.CLEAR_CLASS_SCHEDULE_SUCCESS,
});

export const getPublicClassScheduleStart = () => ({
    type: actionTypes.GET_PUBLIC_CLASS_SCHEDULE_LIST_START,
});

export const getPublicClassScheduleSuccess = (classScheduler) => ({
    type: actionTypes.GET_PUBLIC_CLASS_SCHEDULE_LIST_SUCCESS,
    classScheduler,
});
