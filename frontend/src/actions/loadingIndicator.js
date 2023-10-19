import * as actionTypes from './actionsType';

export const setLoading = (payload) => ({
    type: actionTypes.SET_LOADING_INDICATOR,
    payload,
});

export const setScheduleLoading = (payload) => ({
    type: actionTypes.SET_SCHEDULE_LOADING_INDICATOR,
    payload,
});
export const setMainScheduleLoading = (payload) => ({
    type: actionTypes.SET_MAIN_SCHEDULE_LOADING_INDICATOR,
    payload,
});

export const setSemesterLoading = (payload) => ({
    type: actionTypes.SET_SEMESTER_LOADING_INDICATOR,
    payload,
});

export const setStudentsLoading = (payload) => ({
    type: actionTypes.SET_STUDENTS_LOADING_INDICATOR,
    payload,
});

export const setRoomsLoading = (payload) => ({
    type: actionTypes.SET_ROOMS_LOADING_INDICATOR,
    payload,
});

export const setAuthLoading = (payload) => ({
    type: actionTypes.SET_AUTH_LOADING_INDICATOR,
    payload,
});
