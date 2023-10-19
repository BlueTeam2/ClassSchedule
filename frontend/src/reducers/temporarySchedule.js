import * as actionTypes from '../actions/actionsType';

const initialState = {
    temporarySchedules: [],
    temporarySchedule: {},
    vacation: {},
    schedulesAndTemporarySchedules: [],
    teacherId: null,
    changedScheduleId: null,
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_TEMPORARY_SCHEDULES:
            return { ...state, temporarySchedules: action.result };
        case actionTypes.SET_SCHEDULES_AND_TEMPORARY_SCHEDULES:
            return { ...state, schedulesAndTemporarySchedules: action.result };
        case actionTypes.SELECT_TEMPORARY_SCHEDULE:
            return { ...state, temporarySchedule: action.result };
        case actionTypes.SELECT_VACATION:
            return { ...state, vacation: action.result };
        case actionTypes.SELECT_TEACHER_ID:
            return { ...state, teacherId: action.result };
        default:
            return state;
    }
};

export default reducer;
