import * as actionTypes from '../actions/actionsType';
import { makeFullSchedule } from '../mappers/fullScheduleMapper';
import { makeTeacherSchedule } from '../mappers/teacherScheduleMapper';
import { makeGroupSchedule } from '../mappers/groupScheduleMapper';
import { places } from '../constants/places';

const initialState = {
    items: [],
    place: localStorage.getItem('place') || places.TOGETHER,
    availability: {},
    itemsIds: [],
    fullSchedule: [],
    groupSchedule: {},
    teacherSchedule: {},
    scheduleType: 'full',
    scheduleGroup: null,
    scheduleTeacher: null,
    scheduleSemester: null,
    currentSemester: {},
    defaultSemester: {},
    viewTeacherScheduleResults: 'block-view',
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_PLACE:
            return { ...state, place: action.place };
        case actionTypes.GET_SCHEDULE_ITEMS_SUCCESS:
            return { ...state, items: action.items };
        case actionTypes.GET_CURRENT_SEMESTER_SUCCESS:
            return { ...state, currentSemester: action.semester };
        case actionTypes.GET_DEFAULT_SEMESTER_SUCCESS:
            return { ...state, defaultSemester: action.semester };
        case actionTypes.CHECK_AVAILABILITY_CHANGE_ROOM_SCHEDULE_SUCCESS:
            return { ...state, availability: action.payload };
        case actionTypes.ADD_ITEM_TO_SCHEDULE: {
            const { id } = action.payload;
            let itemArr;
            if (id) {
                const index = state.items.findIndex((item) => {
                    return item.id === id;
                });
                if (index < 0) {
                    itemArr = state.items.concat(action.payload);
                } else {
                    state.items.splice(index, 1, action.payload);
                    itemArr = state.items;
                }
            } else {
                itemArr = state.items.concat(action.payload);
            }
            return { ...state, items: itemArr };
        }
        case actionTypes.SET_SCHEDULE_TYPE:
            return { ...state, groupSchedule: {}, fullSchedule: [], scheduleType: action.newType };
        case actionTypes.GET_FULL_SCHEDULE_SUCCESS: {
            const mappedSchedule = makeFullSchedule(action.schedule);
            return {
                ...state,
                fullSchedule: mappedSchedule,
                groupSchedule: {},
                teacherSchedule: {},
            };
        }
        case actionTypes.GET_GROUP_SCHEDULE_SUCCESS: {
            const mappedSchedule = makeGroupSchedule(action.schedule);
            return {
                ...state,
                groupSchedule: mappedSchedule,
                teacherSchedule: {},
                fullSchedule: [],
            };
        }
        case actionTypes.SET_ITEM_GROUP_ID:
            return { ...state, itemGroupId: action.payload };
        case actionTypes.SET_SCHEDULE_GROUP:
            return { ...state, scheduleGroup: action.group };
        case actionTypes.DELETE_SCHEDULE_ITEM_SUCCESS: {
            const index = state.items.findIndex((item) => item.id === action.itemId);
            state.items.splice(index, 1);
            const newArr = state.items;
            return { ...state, items: newArr };
        }
        case actionTypes.SET_SCHEDULE_TEACHER:
            return { ...state, scheduleTeacher: action.teacher };
        case actionTypes.GET_TEACHER_SCHEDULE_SUCCESS: {
            const mappedSchedule = makeTeacherSchedule(action.schedule);
            return {
                ...state,
                teacherSchedule: mappedSchedule,
                groupSchedule: {},
                fullSchedule: [],
            };
        }
        case actionTypes.GET_ALL_PUBLIC_SEMESTERS_SUCCESS:
            return { ...state, semesters: action.semesters };
        case actionTypes.SET_SCHEDULE_SEMESTER:
            return {
                ...state,
                scheduleGroup: null,
                scheduleTeacher: null,
                scheduleSemester: action.semester,
            };
        case actionTypes.GET_TEACHER_RANGE_SCHEDULE_SUCCESS:
            return {
                ...state,
                teacherRangeSchedule: action.schedule,
                scheduleGroup: null,
                teacherSchedule: [],
                groupSchedule: {},
                fullSchedule: [],
            };
        case actionTypes.SET_TEACHER_VIEW_TYPE:
            return { ...state, viewTeacherScheduleResults: action.payload };
        default:
            return state;
    }
};

export default reducer;
