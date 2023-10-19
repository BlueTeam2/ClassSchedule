import * as actionTypes from './actionsType';

export const getScheduleItemsStart = (semesterId) => {
    return {
        type: actionTypes.GET_SCHEDULE_ITEMS_START,
        semesterId,
    };
};
export const getScheduleItemsSuccess = (items) => {
    return {
        type: actionTypes.GET_SCHEDULE_ITEMS_SUCCESS,
        items,
    };
};

export const getCurrentSemesterSuccess = (semester) => {
    return {
        type: actionTypes.GET_CURRENT_SEMESTER_SUCCESS,
        semester,
    };
};

export const getAllPublicSemestersStart = () => {
    return {
        type: actionTypes.GET_ALL_PUBLIC_SEMESTERS_START,
    };
};

export const getAllPublicSemestersSuccess = (semesters) => {
    return {
        type: actionTypes.GET_ALL_PUBLIC_SEMESTERS_SUCCESS,
        semesters,
    };
};
export const getAllScheduleItemsStart = () => {
    return {
        type: actionTypes.GET_ALL_SCHEDULE_ITEMS_START,
    };
};
export const addItemsToScheduleStart = (item) => {
    return {
        type: actionTypes.ADD_ITEM_TO_SCHEDULE_START,
        item,
    };
};
export const editRoomItemToScheduleStart = (item) => {
    return {
        type: actionTypes.EDIT_ITEM_TO_SCHEDULE_START,
        item,
    };
};
export const deleteScheduleItemStart = (itemId) => {
    return {
        type: actionTypes.DELETE_SCHEDULE_ITEM_START,
        itemId,
    };
};

export const deleteScheduleItemSuccess = (itemId) => {
    return {
        type: actionTypes.DELETE_SCHEDULE_ITEM_SUCCESS,
        itemId,
    };
};
export const clearScheduleStart = (semesterId) => {
    return {
        type: actionTypes.CLEAR_SCHEDULE_START,
        semesterId,
    };
};

export const sendTeacherScheduleStart = (data) => {
    return {
        type: actionTypes.SEND_TEACHER_SCHEDULE_START,
        data,
    };
};
export const getTeacherRangeScheduleStart = (values) => {
    return {
        type: actionTypes.GET_TEACHER_RANGE_SCHEDULE_START,
        values,
    };
};

export const getTeacherRangeScheduleSuccess = (schedule) => {
    return {
        type: actionTypes.GET_TEACHER_RANGE_SCHEDULE_SUCCESS,
        schedule,
    };
};

export const getCurrentSemesterRequsted = () => {
    return {
        type: actionTypes.GET_CURRENT_SEMESTER_START,
    };
};

export const getDefaultSemesterRequsted = () => {
    return {
        type: actionTypes.GET_DEFAULT_SEMESTER_START,
    };
};

export const getDefaultSemesterSuccess = (semester) => {
    return {
        type: actionTypes.GET_DEFAULT_SEMESTER_SUCCESS,
        semester,
    };
};

export const addItemToSchedule = (payload) => {
    return {
        type: actionTypes.ADD_ITEM_TO_SCHEDULE,
        payload,
    };
};

export const checkAvailabilityScheduleStart = (item) => {
    return {
        type: actionTypes.CHECK_AVAILABILITY_SCHEDULE_START,
        item,
    };
};

export const checkAvailabilityScheduleSuccess = (payload) => {
    return {
        type: actionTypes.CHECK_AVAILABILITY_CHANGE_ROOM_SCHEDULE_SUCCESS,
        payload,
    };
};

export const checkAvailabilityChangeRoomScheduleStart = (item) => {
    return {
        type: actionTypes.CHECK_AVAILABILITY_CHANGE_ROOM_SCHEDULE_START,
        item,
    };
};

export const getFullScheduleStart = (semesterId) => {
    return {
        type: actionTypes.GET_FULL_SCHEDULE_START,
        semesterId,
    };
};

export const getFullScheduleSuccess = (schedule) => {
    return {
        type: actionTypes.GET_FULL_SCHEDULE_SUCCESS,
        schedule,
    };
};
export const setItemGroupId = (payload) => {
    return {
        type: actionTypes.SET_ITEM_GROUP_ID,
        payload,
    };
};
export const getGroupScheduleStart = (groupId, semesterId) => {
    return {
        type: actionTypes.GET_GROUP_SCHEDULE_START,
        groupId,
        semesterId,
    };
};

export const getGroupScheduleSuccess = (schedule) => {
    return {
        type: actionTypes.GET_GROUP_SCHEDULE_SUCCESS,
        schedule,
    };
};
export const getTeacherScheduleStart = (teacherId, semesterId) => {
    return {
        type: actionTypes.GET_TEACHER_SCHEDULE_START,
        teacherId,
        semesterId,
    };
};

export const getTeacherScheduleSuccess = (schedule) => {
    return {
        type: actionTypes.GET_TEACHER_SCHEDULE_SUCCESS,
        schedule,
    };
};

export const setScheduleType = (newType) => {
    return {
        type: actionTypes.SET_SCHEDULE_TYPE,
        newType,
    };
};

export const setScheduleGroup = (group) => {
    return {
        type: actionTypes.SET_SCHEDULE_GROUP,
        group,
    };
};

export const setScheduleTeacher = (teacher) => {
    return {
        type: actionTypes.SET_SCHEDULE_TEACHER,
        teacher,
    };
};

export const setScheduleSemester = (semester) => {
    return {
        type: actionTypes.SET_SCHEDULE_SEMESTER,
        semester,
    };
};

export const setTeacherViewType = (payload) => {
    return {
        type: actionTypes.SET_TEACHER_VIEW_TYPE,
        payload,
    };
};

export const setPlace = (place) => {
    return {
        type: actionTypes.SET_PLACE,
        place,
    };
};

export const selectGroupSchedule = (semesterId, groupId) => {
    return {
        type: actionTypes.SELECT_GROUP_SCHEDULE_START,
        semesterId,
        groupId,
    };
};
export const selectTeacherSchedule = (semesterId, teacherId) => {
    return {
        type: actionTypes.SELECT_TEACHER_SCHEDULE_START,
        semesterId,
        teacherId,
    };
};

export const selectFullSchedule = (semesterId) => {
    return {
        type: actionTypes.SELECT_FULL_SCHEDULE_START,
        semesterId,
    };
};
