import * as actionTypes from './actionsType';

export const getAllSemestersSuccess = (semesters) => ({
    type: actionTypes.SHOW_ALL_SEMESTERS_SUCCESS,
    semesters,
});

export const getAllSemestersStart = () => ({
    type: actionTypes.GET_ALL_SEMESTERS_START,
});

export const getDisabledSemestersSuccess = (semesters) => ({
    type: actionTypes.SET_DISABLED_SEMESTERS_SUCCESS,
    semesters,
});

export const getDisabledSemestersStart = () => ({
    type: actionTypes.GET_DISABLED_SEMESTERS_START,
});

export const getArchivedSemestersSuccess = (semesters) => ({
    type: actionTypes.SET_ARCHIVED_SEMESTERS_SUCCESS,
    semesters,
});

export const getArchivedSemesterByIdStart = (semesterId) => ({
    type: actionTypes.GET_ARCHIVE_SEMESTER_BY_ID_START,
    semesterId,
});

export const createArchivedSemesterStart = (semesterId) => ({
    type: actionTypes.CREATE_ARCHIVE_SEMESTER_START,
    semesterId,
});

export const getArchivedSemestersStart = () => ({
    type: actionTypes.SET_ARCHIVED_SEMESTERS_START,
});

export const addSemesterSuccess = (item) => ({
    type: actionTypes.ADD_SEMESTER_SUCCESS,
    item,
});

export const addSemesterStart = (item) => ({
    type: actionTypes.ADD_SEMESTER_START,
    item,
});

export const deleteSemesterSuccess = (semesterId) => ({
    type: actionTypes.DELETE_SEMESTER_SUCCESS,
    semesterId,
});

export const deleteSemesterStart = (semesterId) => ({
    type: actionTypes.DELETE_SEMESTER_START,
    semesterId,
});

export const selectSemesterSuccess = (semesterId) => ({
    type: actionTypes.SELECT_SEMESTER_SUCCESS,
    semesterId,
});

export const updateSemesterSuccess = (semester) => ({
    type: actionTypes.UPDATE_SEMESTER_SUCCESS,
    semester,
});

export const updateSemesterStart = (item) => ({
    type: actionTypes.UPDATE_SEMESTER_START,
    item,
});

export const updateSemesterByIdStartSuccess = (semesterId) => ({
    type: actionTypes.UPDATE_SEMESTER_BY_ID_START_SUCCESS,
    semesterId,
});

export const moveToArchivedSemesterSuccess = (semesterId) => ({
    type: actionTypes.MOVE_SEMESTER_TO_ARCHIVE_SUCCESS,
    semesterId,
});

export const clearSemesterSuccess = () => ({
    type: actionTypes.CLEAR_SEMESTER_SUCCESS,
});

export const handleSemesterFormSubmitStart = (values) => ({
    type: actionTypes.HANDLE_SEMESTER_FORM_SUBMIT_START,
    values,
});

export const setGroupsToSemesterStart = (semesterId, groups) => ({
    type: actionTypes.SET_GROUPS_TO_SEMESTER_START,
    semesterId,
    groups,
});

export const copyLessonsFromSemesterStart = (values) => ({
    type: actionTypes.COPY_LESSONS_FROM_SEMESTER_START,
    values,
});

export const setSemesterCopyStart = (values) => ({
    type: actionTypes.SET_SEMESTER_COPY_START,
    values,
});

export const toggleSemesterVisibilityStart = (semester) => ({
    type: actionTypes.TOGGLE_SEMESTER_VISIBILITY_START,
    semester,
});
