import React, { useState } from 'react';
import PropTypes from 'prop-types';
import Select from 'react-select';
import { isEmpty } from 'lodash';
import i18n from '../../../i18n';

import CustomDialog from '../../../containers/Dialogs/CustomDialog';
import { COMMON_MOVE_TO_GROUP_TITLE } from '../../../constants/translationLabels/common';
import { dialogCloseButton, dialogMoveToGroupButton } from '../../../constants/dialogs';
import { getGroupsOptionsForSelect } from '../../../utils/selectUtils';

const MovingGroupsDialog = (props) => {
    const { open, group, groups, onClose, moveStudentsToGroupStart } = props;

    const [newGroup, setNewGroup] = useState({});
    const [isDisabled, setIsDisabled] = useState(true);

    const groupsWithoutCurrentGroup = groups.filter((item) => item.id !== group.id);
    const groupsOption = getGroupsOptionsForSelect(groupsWithoutCurrentGroup);

    const clearSelection = () => {
        onClose();
        setNewGroup({});
    };

    const selectGroup = (currentGroup) => {
        if (isEmpty(currentGroup)) {
            return;
        }
        setIsDisabled(false);
        setNewGroup(currentGroup);
    };

    const handleSubmitGroupStudents = () => {
        moveStudentsToGroupStart(newGroup);
        clearSelection();
    };

    return (
        <CustomDialog
            title={i18n.t(COMMON_MOVE_TO_GROUP_TITLE)}
            className="select-dialog"
            onClose={onClose}
            open={open}
            buttons={[
                dialogMoveToGroupButton(handleSubmitGroupStudents, isDisabled),
                dialogCloseButton(clearSelection),
            ]}
        >
            <Select
                disabled="disabled"
                classNamePrefix="react-select"
                options={groupsOption}
                onChange={selectGroup}
            />
        </CustomDialog>
    );
};

MovingGroupsDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
};

export default MovingGroupsDialog;
