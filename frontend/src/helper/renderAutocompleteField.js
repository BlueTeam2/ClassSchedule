import React from 'react';
import { Autocomplete } from '@material-ui/lab';
import { FormControl, TextField } from '@material-ui/core';
import { renderFromHelper } from '../share/renderedFields/error';

export const renderAutocompleteField = ({
    values,
    label,
    input,
    handleChange,
    getOptionLabel,
    meta: { touched, error },
    className,
    ...custom
}) => {
    return (
        <Autocomplete
            {...input}
            {...custom}
            label={label}
            options={values}
            placeholder={label}
            getOptionLabel={getOptionLabel}
            className={className}
            onChange={(_, value) => {
                if (handleChange) {
                    handleChange(value);
                }

                return input.onChange(value);
            }}
            onBlur={(_, value) => input.onBlur(value)}
            renderInput={(params) => (
                <FormControl error={touched && !!error}>
                    <TextField {...params} label={label} />
                    {renderFromHelper({ touched, error })}
                </FormControl>
            )}
        />
    );
};
