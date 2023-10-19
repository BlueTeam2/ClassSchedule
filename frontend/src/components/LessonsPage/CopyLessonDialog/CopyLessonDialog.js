import React, { useState } from 'react';

import PropTypes from 'prop-types';

import Autocomplete from '@material-ui/lab/Autocomplete';
import TextField from '@material-ui/core/TextField';

import {
    COPY_TO_SAME_GROUP_ERROR,
    COMMON_CHOOSE_GROUP,
} from '../../../constants/translationLabels/common';
import CustomDialog from '../../../containers/Dialogs/CustomDialog';
import { dialogCloseButton, dialogChooseButton } from '../../../constants/dialogs';
import './CopyLessonDialog.scss';

const CopyLessonDialog = (props) => {
    const { onClose, lesson, translation, groups, groupId, open } = props;
    const [group, setGroup] = useState('');
    const [error, setError] = useState('');

    const chooseClickHandle = () => {
        if (!group) {
            return;
        }
        if (group.id === groupId) {
            setError(translation(COPY_TO_SAME_GROUP_ERROR));
            return;
        }
        onClose({ lesson, group });
    };

    const handleChangeAutocomplete = (event, newValue) => {
        setGroup(newValue);
        setError(null);
    };

    const defaultProps = {
        options: groups,
        getOptionLabel: (option) => (option ? option.title : ''),
    };
    return (
        <CustomDialog
            open={open}
            onClose={onClose}
            buttons={[dialogChooseButton(chooseClickHandle), dialogCloseButton(() => onClose(''))]}
        >
            <Autocomplete
                {...defaultProps}
                id="group"
                multiple
                clearOnEscape
                openOnFocus
                className="groupField"
                onChange={handleChangeAutocomplete}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label={translation(COMMON_CHOOSE_GROUP)}
                        error={!!error}
                        helperText={error || null}
                        margin="normal"
                    />
                )}
            />
        </CustomDialog>
    );
};
CopyLessonDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
    lesson: PropTypes.object.isRequired,
    translation: PropTypes.func.isRequired,
    groupId: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
};

export default CopyLessonDialog;
