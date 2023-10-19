import * as actionTypes from './actionsType';

export const setTemporarySchedules = (res) => {
    return {
        type: actionTypes.SET_TEMPORARY_SCHEDULES,
        result: res,
    };
};

export const setSchedulesAndTemporarySchedules = (res) => {
    return {
        type: actionTypes.SET_SCHEDULES_AND_TEMPORARY_SCHEDULES,
        result: res,
    };
};

export const selectTemporarySchedule = (res) => {
    return {
        type: actionTypes.SELECT_TEMPORARY_SCHEDULE,
        result: res,
    };
};

export const selectVacation = (res) => {
    return {
        type: actionTypes.SELECT_VACATION,
        result: res,
    };
};

export const selectTeacherId = (res) => {
    return {
        type: actionTypes.SELECT_TEACHER_ID,
        result: res,
    };
};
