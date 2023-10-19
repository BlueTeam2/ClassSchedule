import * as actionTypes from '../actions/actionsType';

const initialState = {
    isOpenConfirmDialog: false,
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.SET_IS_OPEN_CONFIRM_DIALOG:
            return { ...state, isOpenConfirmDialog: action.payload };
        default:
            return state;
    }
};

export default reducer;
