import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom/cjs/react-router-dom';
import { reset } from 'redux-form';
import { STUDENT_FORM } from '../../../constants/reduxForms';
import AddStudentForm from '../../../containers/Students/AddStudentForm';
import { goToGroupPage } from '../../../helper/pageRedirection';
import CustomDialog from '../../../containers/Dialogs/CustomDialog';
import { dialogCloseButton } from '../../../constants/dialogs';
import '../dialog.scss';
import {
    EDIT_TITLE,
    CREATE_TITLE,
    STUDENT_A_LABEL,
} from '../../../constants/translationLabels/formElements';

const AddStudentDialog = (props) => {
    const { setOpen, open, student, groupId } = props;
    const { t } = useTranslation('formElements');
    const history = useHistory();

    const handleClose = () => {
        setOpen(false);
        goToGroupPage(history);
    };

    const onReset = () => {
        reset(STUDENT_FORM);
    };

    return (
        <CustomDialog
            title={student ? t(EDIT_TITLE) : `${t(CREATE_TITLE)} ${t(STUDENT_A_LABEL)}`}
            open={open}
            onClose={handleClose}
            buttons={[dialogCloseButton(handleClose)]}
        >
            <AddStudentForm groupId={groupId} onReset={onReset} student={student} />
        </CustomDialog>
    );
};

AddStudentDialog.propTypes = {
    open: PropTypes.bool.isRequired,
};

export default AddStudentDialog;
