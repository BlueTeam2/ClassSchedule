import React from 'react';

import FormHelperText from '@material-ui/core/FormHelperText';

export const renderFromHelper = ({ touched, error }) => {
    if (touched && error) {
        return <FormHelperText>{touched && error}</FormHelperText>;
    }
    return null;
};
