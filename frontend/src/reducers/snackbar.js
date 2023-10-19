import * as actionTypes from '../actions/actionsType';

const initialState = {
    isSnackbarOpen: false,
    snackbarType: null,
    message: null,
};

const reducer = (state = initialState, action) => {
    if (action.type === actionTypes.SET_OPEN_SNACKBAR) {
        const { type, message, isOpen } = action.result;
        return { ...state, isSnackbarOpen: isOpen, snackbarType: type, message };
    }
    return state;
};

export default reducer;
