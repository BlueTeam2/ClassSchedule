import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

import { useTranslation } from 'react-i18next';
import { isEmpty } from 'lodash';
import { UploadFile } from '../../../components/UploadFile/UploadFile';
import CustomDialog from '../../../containers/Dialogs/CustomDialog';
import { GROUP_LABEL } from '../../../constants/translationLabels/formElements';
import { ShowStudentsOnGroupContent } from '../../../components/Students/ShowStudentsOnGroupContent';
import {
    dialogCloseButton,
    dialogUploadFromFileButton,
    dialogChooseGroupButton,
} from '../../../constants/dialogs';

const ShowStudentsOnGroupDialog = (props) => {
    const {
        uploadStudentsToGroupStart,
        getAllStudentsStart,
        onDeleteStudent,
        students,
        onClose,
        groupId,
        loading,
        open,
        match,
        groups,
    } = props;
    const { t } = useTranslation('formElements');

    const [group, setGroup] = useState({});
    const [isOpenUploadFileDialog, setIsOpenUploadFileDialog] = useState(false);
    const [isOpenMoveStudentsDialog, setIsOpenMoveStudentDialog] = useState(false);
    const [isDisabledBtnMoveStudent, setIsDisabledBtnMoveStudent] = useState(false);

    useEffect(() => {
        getAllStudentsStart(groupId);
        setGroup(groups.find((item) => item.id === +groupId));
    }, [groupId]);

    const showMoveStudentsByGroupDialog = () => {
        setIsOpenMoveStudentDialog(true);
    };
    const handleShowDialogFile = () => {
        setIsOpenUploadFileDialog((prevState) => !prevState);
    };
    const buttonClassName = { additionClassName: 'student-dialog-button-data' };
    const dialogButtons = [
        dialogUploadFromFileButton(handleShowDialogFile, buttonClassName),
        dialogCloseButton(onClose, buttonClassName),
    ];

    return (
        <>
            <CustomDialog
                open={open}
                title={`${t(GROUP_LABEL)} - ${group.title}`}
                onClose={onClose}
                buttons={
                    !isEmpty(students)
                        ? [
                              dialogChooseGroupButton(
                                  showMoveStudentsByGroupDialog,
                                  isDisabledBtnMoveStudent,
                                  buttonClassName,
                              ),
                              ...dialogButtons,
                          ]
                        : dialogButtons
                }
            >
                <ShowStudentsOnGroupContent
                    group={group}
                    match={match}
                    groups={groups}
                    loading={loading}
                    students={students}
                    onDeleteStudent={onDeleteStudent}
                    isOpenMoveStudentsDialog={isOpenMoveStudentsDialog}
                    setIsOpenMoveStudentDialog={setIsOpenMoveStudentDialog}
                    setIsDisabledBtnMoveStudent={setIsDisabledBtnMoveStudent}
                />
            </CustomDialog>
            {isOpenUploadFileDialog && (
                <UploadFile
                    group={group}
                    open={isOpenUploadFileDialog}
                    handleCloseDialogFile={handleShowDialogFile}
                    uploadStudentsToGroupStart={uploadStudentsToGroupStart}
                />
            )}
        </>
    );
};

ShowStudentsOnGroupDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
};

export default ShowStudentsOnGroupDialog;
