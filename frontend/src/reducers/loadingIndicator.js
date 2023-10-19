import * as actionTypes from '../actions/actionsType';

const initialState = {
    loading: true,
    authLoading: false,
    scheduleLoading: false,
    semesterLoading: false,
    studentsLoading: false,
    mainScheduleLoading: false,
    roomsLoading: false,
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_LOADING_INDICATOR:
            return { ...state, loading: action.payload };
        case actionTypes.SET_AUTH_LOADING_INDICATOR:
            return { ...state, authLoading: action.payload };
        case actionTypes.SET_SCHEDULE_LOADING_INDICATOR:
            return { ...state, scheduleLoading: action.payload };
        case actionTypes.SET_MAIN_SCHEDULE_LOADING_INDICATOR:
            return { ...state, mainScheduleLoading: action.payload };
        case actionTypes.SET_SEMESTER_LOADING_INDICATOR:
            return { ...state, semesterLoading: action.payload };
        case actionTypes.SET_STUDENTS_LOADING_INDICATOR:
            return { ...state, studentsLoading: action.payload };
        case actionTypes.SET_ROOMS_LOADING_INDICATOR:
            return { ...state, roomsLoading: action.payload };
        default:
            return state;
    }
};

export default reducer;
