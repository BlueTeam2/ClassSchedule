import React, { useState } from 'react';
import { FaSearch } from 'react-icons/fa';
import { TextField } from '@material-ui/core';
import { useTranslation } from 'react-i18next';

import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import {
    TYPE_TO_SEARCH,
    SHOW_ARCHIVED,
    SHOW_REGULAR,
} from '../../constants/translationLabels/formElements';
import Card from '../Card/Card';
import {
    COMMON_SHOW_DISABLED,
    COMMON_SHOW_ENABLED,
} from '../../constants/translationLabels/common';

    export function SearchPanel (SearchChange, showDisabled, showArchived, forLessons){
    const { t } = useTranslation('formElements');
    const [term, setTerm] = useState('');

    const [state, setState] = useState({
        checkedB: false,
        checkedArchived: false,
    });

    const handleChange = (event) => {
        setState({
            ...state,
            checkedB: false,
            [event.target.name]: event.target.checked,
        });
        if (event.target.name === 'checkedArchived') {
            showArchived();
        }
        showDisabled();
    };

    const onSearchChange = (e) => {
        const newTerm = e.target.value;
        setTerm(newTerm);
        SearchChange(newTerm);
    };

    return (
        <Card additionClassName="search-group">
            {!forLessons && (
                <FormControlLabel
                    control={
                        <Switch
                            checked={state.checkedB}
                            onChange={handleChange}
                            name="checkedB"
                            color="primary"
                        />
                    }
                    label={!state.checkedB ? t(COMMON_SHOW_DISABLED) : t(COMMON_SHOW_ENABLED)}
                />
            )}
            {!forLessons && showArchived ? (
                <FormControlLabel
                    control={
                        <Switch
                            checked={state.checkedArchived}
                            onChange={handleChange}
                            name="checkedArchived"
                            color="secondary"
                        />
                    }
                    label={!state.checkedArchived ? t(SHOW_ARCHIVED) : t(SHOW_REGULAR)}
                />
            ) : null}

            <TextField
                className="form-field"
                label={<FaSearch />}
                placeholder={t(TYPE_TO_SEARCH)}
                value={term}
                onChange={onSearchChange}
            />
        </Card>
    );
};

export default SearchPanel;
