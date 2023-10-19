import './UploadFile.scss';
import React, { useState, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import BackupIcon from '@material-ui/icons/Backup';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogCloseButton, dialogUploadButton } from '../../constants/dialogs';
import {
    SELECT_FILE,
    EXAMPLE_FILE,
    FILE_RULES_FOR_HEADER,
    SELECT_CORRECT_FORMAT,
    FILE_RULES_FOR_EACH_LNE,
} from '../../constants/translationLabels/common';

export const UploadFile = (props) => {
    const { t } = useTranslation('common');
    const [isHideRules, setIsHideRules] = useState(false);
    const [selectedFile, setSelectedFile] = useState(null);
    const fileInputRef = useRef();
    const {
        group: { id },
        open,
        handleCloseDialogFile,
        uploadStudentsToGroupStart,
    } = props;

    const changeHandler = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleSubmission = () => {
        uploadStudentsToGroupStart(selectedFile, id);
        fileInputRef.current.value = '';
        setSelectedFile(null);
    };

    const setDisabledSendButton = () => {
        return selectedFile === null;
    };
    const buttonTitle = selectedFile ? selectedFile.name : t(SELECT_FILE);
    return (
        <CustomDialog
            open={open}
            onClose={handleCloseDialogFile}
            buttons={[
                dialogUploadButton(handleSubmission, setDisabledSendButton()),
                dialogCloseButton(handleCloseDialogFile),
            ]}
        >
            <div className="upload-dialog">
                {isHideRules && (
                    <div className="upload-title">
                        <div>{t(SELECT_CORRECT_FORMAT)}</div>
                        <div>{t(FILE_RULES_FOR_HEADER)}</div>
                        <div>{t(FILE_RULES_FOR_EACH_LNE)}</div>
                    </div>
                )}
                <div className="upload-example-btn">
                    <div className="upload-example">{t(EXAMPLE_FILE)}</div>
                    <HelpOutlineIcon
                        className="upload-button"
                        onClick={() => setIsHideRules(!isHideRules)}
                    />
                </div>
                <div className="upload-text-file">
                    <div> surname,name,patronymic,email</div>
                    <div> example,example,example,example@gmail.com</div>
                </div>
                <label htmlFor="file-upload" className="upload-file">
                    <input
                        id="file-upload"
                        type="file"
                        accept=".txt, .csv"
                        onChange={changeHandler}
                        ref={fileInputRef}
                        className="upload-input"
                    />
                    <BackupIcon></BackupIcon>
                    <div className="upload-text">{t(buttonTitle)}</div>
                </label>
            </div>
        </CustomDialog>
    );
};
