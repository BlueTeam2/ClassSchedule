import { handleSnackbarOpenService } from '../services/snackbarService';
import { snackbarTypes } from '../constants/snackbarTypes';
import i18n from '../i18n';

export const errorHandler = (error) => {
    handleSnackbarOpenService(
        true,
        snackbarTypes.ERROR,
        error.response ? i18n.t(error.response.data.message, error.response.data.message) : 'Error',
    );
};

export const successHandler = (message) => {
    handleSnackbarOpenService(true, snackbarTypes.SUCCESS, message);
};

export const infoHandler = (message) => {
    handleSnackbarOpenService(true, snackbarTypes.INFO, message);
};
