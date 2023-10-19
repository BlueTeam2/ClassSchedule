import * as actionTypes from '../actions/actionsType';

const initialState = {
    users: [],
    user: {},
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_USERS:
            return { ...state, users: action.result };
        case actionTypes.SET_USER:
            return { ...state, user: action.result };
        default:
            return state;
    }
};

export default reducer;
