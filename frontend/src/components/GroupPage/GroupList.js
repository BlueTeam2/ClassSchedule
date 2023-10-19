import './GroupPage.scss';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { isEmpty } from 'lodash';
import { useTranslation } from 'react-i18next';
import { CircularProgress } from '@material-ui/core';
import { goToGroupPage } from '../../helper/pageRedirection';
import { dialogTypes } from '../../constants/dialogs';
import { GROUP_Y_LABEL } from '../../constants/translationLabels/formElements';
import { search } from '../../helper/search';
import GroupCard from './GroupCard/GroupCard';
import NotFound from '../../share/NotFound/NotFound';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import AddStudentDialog from '../../share/DialogWindows/_dialogWindows/AddStudentDialog';
import ShowStudentsOnGroupDialog from '../../containers/Students/ShowStudentsOnGroupDialog';
import { ADD_STUDENT_ACTION, SHOW_STUDENTS_ACTION } from '../../constants/actionsUrl';
import { DraggableCard } from '../../share/DraggableCard/DraggableCard';

const GroupList = (props) => {
    const {
        getEnabledGroupsStart,
        getDisabledGroupsStart,
        setIsOpenConfirmDialog,
        dragAndDropGroupStart,
        toggleDisabledStatus,
        isOpenConfirmDialog,
        selectGroupSuccess,
        deleteGroupStart,
        searchItem,
        loading,
        groups,
        match,
        setGroup,
        isDisabled,
    } = props;

    const history = useHistory();
    const { t } = useTranslation('formElements');

    const [groupId, setGroupId] = useState(-1);
    const [confirmDialogType, setConfirmDialogType] = useState('');
    const [isOpenShowStudentsDialog, setIsOpenShowStudentsDialog] = useState(false);
    const [isOpenAddStudentDialog, setIsOpenAddStudentDialog] = useState(false);
    const [dragGroup, setGroupStart] = useState();

    useEffect(() => {
        if (isDisabled) {
            getDisabledGroupsStart();
        } else {
            getEnabledGroupsStart();
        }
    }, [isDisabled]);

    const visibleGroups = search(groups, searchItem, ['title']);

    const showConfirmDialog = (currentId, disabledStatus) => {
        setGroupId(currentId);
        setConfirmDialogType(disabledStatus);
        setIsOpenConfirmDialog(true);
    };
    const acceptConfirmDialog = (currentGroupId) => {
        setIsOpenConfirmDialog(false);
        if (confirmDialogType !== dialogTypes.DELETE_CONFIRM) {
            toggleDisabledStatus(currentGroupId, isDisabled);
        } else deleteGroupStart(currentGroupId);
    };

    const showAddStudentDialog = (currentGroupId) => {
        setGroupId(currentGroupId);
        setIsOpenAddStudentDialog(true);
    };
    const showStudentsByGroup = (currentGroupId) => {
        setGroupId(currentGroupId);
        selectGroupSuccess(currentGroupId);
        setIsOpenShowStudentsDialog(true);
    };

    const closeShowStudentsByGroup = () => {
        goToGroupPage(history);
        setIsOpenShowStudentsDialog(false);
    };

    const dragAndDropItem = (afterItemId) => {
        dragAndDropGroupStart(dragGroup, afterItemId);
    };

    const checkParamsForActions = () => {
        const { id, action } = match.params;
        const checkParamsAndSetActions = {
            [ADD_STUDENT_ACTION]: showAddStudentDialog,
            [SHOW_STUDENTS_ACTION]: showStudentsByGroup,
        };
        const actionsFunc = checkParamsAndSetActions[action];
        if (actionsFunc && id) actionsFunc(id);
    };

    useEffect(() => {
        checkParamsForActions();
    }, []);

    if (loading) {
        return (
            <section className="centered-container">
                <CircularProgress />
            </section>
        );
    }

    if (isEmpty(visibleGroups)) {
        return <NotFound name={t(GROUP_Y_LABEL)} />;
    }

    return (
        <>
            <CustomDialog
                type={confirmDialogType}
                handelConfirm={() => acceptConfirmDialog(groupId)}
                whatDelete="group"
                open={isOpenConfirmDialog}
            />
            {isOpenAddStudentDialog && (
                <AddStudentDialog
                    groups={groups}
                    groupId={groupId}
                    open={isOpenAddStudentDialog}
                    setOpen={setIsOpenAddStudentDialog}
                />
            )}
            {isOpenShowStudentsDialog && (
                <ShowStudentsOnGroupDialog
                    match={match}
                    groupId={groupId}
                    open={isOpenShowStudentsDialog}
                    onClose={closeShowStudentsByGroup}
                />
            )}
            <div className="group-list">
                {visibleGroups.map((item) => (
                    <DraggableCard
                        key={item.id}
                        item={item}
                        setGroupStart={setGroupStart}
                        dragAndDropItem={dragAndDropItem}
                    >
                        <GroupCard
                            group={item}
                            setGroup={setGroup}
                            disabled={isDisabled}
                            showConfirmDialog={showConfirmDialog}
                            showAddStudentDialog={showAddStudentDialog}
                            showStudentsByGroup={showStudentsByGroup}
                        />
                    </DraggableCard>
                ))}
            </div>
        </>
    );
};

export default GroupList;
