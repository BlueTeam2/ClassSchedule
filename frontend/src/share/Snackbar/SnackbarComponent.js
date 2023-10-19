import React from 'react';

import Snackbar from '@material-ui/core/Snackbar';
import { snackbarTypes } from '../../constants/snackbarTypes';
import { Alert } from '../Alerts';

const SnackbarComponent = (props) => {
    const { message, type, isOpen, handleSnackbarClose } = props;
    return (
        <Snackbar
            open={isOpen}
            autoHideDuration={type === snackbarTypes.ERROR ? null : 3000}
            onClose={handleSnackbarClose}
        >
            <Alert onClose={handleSnackbarClose} severity={type}>
                {message}
            </Alert>
        </Snackbar>
    );
};

export default SnackbarComponent;
