import * as actionTypes from './actionsType';

export const createStudentSuccess = (student) => ({
    type: actionTypes.CREATE_STUDENT,
    student,
});
export const showAllStudents = (payload) => ({
    type: actionTypes.SHOW_ALL_STUDENTS,
    payload,
});

export const deleteStudentSuccess = (id) => ({
    type: actionTypes.DELETE_STUDENT,
    id,
});

export const deleteAllStudentSuccess = (students) => ({
    type: actionTypes.DELETE_SELECTED_STUDENTS,
    students,
});

export const selectStudentSuccess = (id) => ({
    type: actionTypes.SET_STUDENT,
    id,
});

export const updateStudentSuccess = (student) => ({
    type: actionTypes.UPDATE_STUDENT,
    student,
});

export const checkAllStudentsSuccess = (checkedStudents, checkedAll) => ({
    type: actionTypes.CHECK_ALL_STUDENTS,
    checkedStudents,
    checkedAll,
});

export const getAllStudentsStart = (id) => ({
    type: actionTypes.GET_ALL_STUDENTS,
    id,
});

export const submitStudentStart = (data, groupId) => ({
    type: actionTypes.SUBMIT_STUDENT_FORM,
    data,
    groupId,
});

export const deleteStudentStart = (id) => ({
    type: actionTypes.DELETE_STUDENT_START,
    id,
});

export const uploadStudentsToGroupStart = (file, id) => ({
    type: actionTypes.UPLOAD_FILE_STUDENT_START,
    file,
    id,
});

export const moveStudentsToGroupStart = (group) => ({
    type: actionTypes.MOVE_STUDENTS_TO_GROUP_START,
    group,
});
