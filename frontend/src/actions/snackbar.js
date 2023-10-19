import * as actionTypes from './actionsType';
import { snackbarTypes } from '../constants/snackbarTypes';

export const setOpenSnackbar = (res) => {
    return {
        type: actionTypes.SET_OPEN_SNACKBAR,
        result: res,
    };
};

export const setOpenInfoSnackbar = (message) => {
    return {
        type: actionTypes.SET_OPEN_SNACKBAR,
        result: {
            isOpen: true,
            type: snackbarTypes.INFO,
            message,
        },
    };
};

export const setOpenSuccessSnackbar = (message) => {
    return {
        type: actionTypes.SET_OPEN_SNACKBAR,
        result: {
            isOpen: true,
            type: snackbarTypes.SUCCESS,
            message,
        },
    };
};

export const setOpenErrorSnackbar = (message) => {
    return {
        type: actionTypes.SET_OPEN_SNACKBAR,
        result: {
            isOpen: true,
            type: snackbarTypes.ERROR,
            message,
        },
    };
};
