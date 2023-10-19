import { store } from '../store';

import { setOpenSnackbar } from '../actions/index';

export const handleSnackbarOpenService = (isOpen, type, message) => {
    store.dispatch(
        setOpenSnackbar({
            isOpen,
            type,
            message,
        }),
    );
};

export const handleSnackbarCloseService = () => {
    const snackbarStore = store.getState().snackbar;
    store.dispatch(
        setOpenSnackbar({
            isOpen: false,
            type: snackbarStore.snackbarType,
            message: null,
        }),
    );
};
