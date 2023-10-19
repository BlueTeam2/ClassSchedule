import * as actionTypes from '../actions/actionsType';

const initialState = {
    lessons: [],
    lessonTypes: [],
    lesson: {},
    groupId: null,
    uniqueError: false,
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.CREATE_LESSON_CARD_SUCCESS:
            return { ...state, lessons: state.lessons.concat(action.result) };
        case actionTypes.SET_LESSONS_CARDS_START:
            return { ...state, lessons: action.result };
        case actionTypes.SET_LESSON_TYPES_SUCCESS:
            return { ...state, lessonTypes: action.result };
        case actionTypes.DELETE_LESSON_CARD_SUCCESS: {
            const lessons = state.lessons.filter((lesson) => lesson.id !== action.id);
            return { ...state, lessons };
        }
        case actionTypes.SELECT_LESSON_CARD_SUCCESS: {
            let lesson = state.lessons.find((less) => less.id === action.cardId);
            if (!lesson) {
                lesson = { id: null };
            }
            return { ...state, lesson };
        }
        case actionTypes.UPDATE_LESSON_CARD_SUCCESS: {
            const lessonIndex = state.lessons.findIndex(({ id }) => id === action.result.id);
            const lessons = [...state.lessons];

            lessons[lessonIndex] = {
                ...lessons[lessonIndex],
                ...action.result,
            };
            return { ...state, lessons, lesson: {} };
        }
        case actionTypes.SELECT_GROUP_ID:
            return { ...state, lesson: {}, groupId: action.id };
        case actionTypes.SET_UNIQUE_ERROR:
            return { ...state, uniqueError: action.result };
        default:
            return state;
    }
};

export default reducer;
