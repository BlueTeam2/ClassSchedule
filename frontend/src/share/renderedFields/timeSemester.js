import React from 'react';
import MomentUtils from '@date-io/moment';
import * as moment from 'moment';
import { MuiPickersUtilsProvider, DatePicker } from '@material-ui/pickers';
import { dateFormat } from '../../constants/formats';

const renderMonthPicker = ({
    label,
    input: { value, ...inputProps },
    meta: { touched, invalid, error },
    ...custom
}) => {
    return (
        <MuiPickersUtilsProvider utils={MomentUtils}>
            <DatePicker
                minDate={new Date()}
                placeholder="11/11/2021"
                clearable
                value={value ? moment(value, dateFormat).toDate() : null}
                format="DD/MM/YYYY"
                error={touched && invalid}
                helperText={touched && error ? touched && error : label}
                {...inputProps}
                {...custom}
            />
        </MuiPickersUtilsProvider>
    );
};
export default renderMonthPicker;
