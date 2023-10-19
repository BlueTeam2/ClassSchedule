import * as actionTypes from '../actions/actionsType';

function compare(a, b) {
    let comparison = 0;
    if (a.surname > b.surname) {
        comparison = 1;
    } else if (a.surname < b.surname) {
        comparison = -1;
    }
    return comparison;
}

const reducer = (
    state = {
        teachers: [],
        teacher: {},
        disabledTeachers: [],
    },
    action,
) => {
    switch (action.type) {
        case actionTypes.ADD_TEACHER_SUCCESS:
            return { ...state, teachers: [...state.teachers, action.teacher].sort(compare) };

        case actionTypes.DELETE_TEACHER_SUCCESS: {
            if (action.disabledStatus) {
                return {
                    ...state,
                    disabledTeachers: [
                        ...state.disabledTeachers.filter(
                            (disabledTeacher) => disabledTeacher.id !== action.id,
                        ),
                    ],
                };
            }
            return {
                ...state,
                teachers: [...state.teachers.filter((teacher) => teacher.id !== action.id)],
            };
        }

        case actionTypes.SET_TEACHER:
            return { ...state, teacher: action.teacher };
        case actionTypes.SELECT_TEACHER: {
            let teacher = state.teachers.find((teach) => teach.id === action.teacher);
            if (!teacher) {
                teacher = { id: null };
            }
            return { ...state, teacher };
        }
        case actionTypes.UPDATE_TEACHER_SUCCESS: {
            const teacherIndex = state.teachers.findIndex(({ id }) => id === action.teacher.id);
            const teachers = [...state.teachers];
            teachers[teacherIndex] = {
                ...teachers[teacherIndex],
                ...action.teacher,
            };
            return { ...state, teacher: {}, teachers };
        }

        case actionTypes.SET_DISABLED_TEACHERS_SUCCESS:
            return { ...state, disabledTeachers: [...action.teachers] };
        case actionTypes.SHOW_ALL_TEACHERS_SUCCESS:
        case actionTypes.GET_TEACHERS_BY_DEPARTMENT:
        case actionTypes.GET_TEACHERS_WITHOUT_ACCOUNT_SUCCESS:
            return { ...state, teachers: [...action.teachers] };

        default:
            return state;
    }
};

export default reducer;
