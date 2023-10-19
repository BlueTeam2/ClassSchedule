import { connect } from 'react-redux';
import { FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import React, { useEffect, useState } from 'react';

import './SubjectPage.scss';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import Card from '../../share/Card/Card';
import { search } from '../../helper/search';
import NotFound from '../../share/NotFound/NotFound';
import CustomDialog from '../Dialogs/CustomDialog';
import { dialogTypes } from '../../constants/dialogs';
import SearchPanel from '../../share/SearchPanel/SearchPanel';
import SnackbarComponent from '../../share/Snackbar/SnackbarComponent';
import AddSubject from '../../components/AddSubjectForm/AddSubjectForm';
import { handleSnackbarCloseService } from '../../services/snackbarService';
import { setIsOpenConfirmDialog } from '../../actions/dialog';
import {
    showAllSubjectsService,
    removeSubjectCardService,
    handleSubjectService,
    selectSubjectService,
    clearSubjectService,
    setEnabledSubjectsService,
    setDisabledSubjectsService,
    getDisabledSubjectsService,
} from '../../services/subjectService';
import {
    EDIT_TITLE,
    SUBJECT_Y_LABEL,
    DELETE_TITLE,
} from '../../constants/translationLabels/formElements';
import { COMMON_SET_DISABLED, COMMON_SET_ENABLED } from '../../constants/translationLabels/common';

const SubjectPage = (props) => {
    const { t } = useTranslation('formElements');
    const {
        isSnackbarOpen,
        snackbarType,
        snackbarMessage,
        disabledSubjects,
        subjects,
        setOpenConfirmDialog,
        isOpenConfirmDialog,
    } = props;

    const [confirmDialogType, setConfirmDialogType] = useState('');
    const [subjectId, setSubjectId] = useState(-1);
    const [term, setTerm] = useState('');

    const [disabled, setDisabled] = useState(false);

    useEffect(() => {
        showAllSubjectsService();
        getDisabledSubjectsService();
    }, []);

    const handleFormReset = () => clearSubjectService();
    const visibleSubjects = disabled
        ? search(disabledSubjects, term, ['name'])
        : search(subjects, term, ['name']);
    const SearchChange = setTerm;

    const showConfirmDialog = (subjId, dialogType) => {
        setSubjectId(subjId);
        setConfirmDialogType(dialogType);
        setOpenConfirmDialog(true);
    };

    const acceptConfirmDialog = (id) => {
        setOpenConfirmDialog(false);
        if (!id) return;
        switch (confirmDialogType) {
            case dialogTypes.DELETE_CONFIRM:
                removeSubjectCardService(subjectId);
                break;
            case dialogTypes.SET_VISIBILITY_DISABLED:
                {
                    const group = subjects.find((subject) => subject.id === subjectId);
                    setDisabledSubjectsService(group);
                }
                break;
            case dialogTypes.SET_VISIBILITY_ENABLED:
                {
                    const group = disabledSubjects.find((subject) => subject.id === subjectId);
                    setEnabledSubjectsService(group);
                }
                break;
            default:
                break;
        }
    };

    const showDisabledHandle = () => {
        setDisabled((prev) => !prev);
    };
    return (
        <>
            <CustomDialog
                type={confirmDialogType}
                whatDelete="subject"
                open={isOpenConfirmDialog}
                handelConfirm={() => acceptConfirmDialog(subjectId)}
            />

            <div className="cards-container">
                <aside className="search-list__panel">
                    <SearchPanel SearchChange={SearchChange} showDisabled={showDisabledHandle} />
                    {disabled ? (
                        ''
                    ) : (
                        <AddSubject
                            className="form"
                            onSubmit={handleSubjectService}
                            onReset={handleFormReset}
                        />
                    )}
                </aside>
                <section className="container-flex-wrap wrapper">
                    {visibleSubjects.length === 0 && <NotFound name={t(SUBJECT_Y_LABEL)} />}
                    {visibleSubjects.map((subject) => (
                        <Card key={subject.id} additionClassName="subject-card done-card">
                            <h2 className="subject-card__name">{subject.name}</h2>
                            <div className="cards-btns">
                                {disabled ? (
                                    <GiSightDisabled
                                        className="svg-btn copy-btn"
                                        title={t(COMMON_SET_ENABLED)}
                                        onClick={() => {
                                            showConfirmDialog(
                                                subject.id,
                                                dialogTypes.SET_VISIBILITY_ENABLED,
                                            );
                                        }}
                                    />
                                ) : (
                                    <>
                                        <IoMdEye
                                            className="svg-btn copy-btn"
                                            title={t(COMMON_SET_DISABLED)}
                                            onClick={() => {
                                                showConfirmDialog(
                                                    subject.id,
                                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                                );
                                            }}
                                        />
                                        <FaEdit
                                            className="svg-btn edit-btn"
                                            title={t(EDIT_TITLE)}
                                            onClick={() => selectSubjectService(subject.id)}
                                        />
                                    </>
                                )}

                                <MdDelete
                                    className="svg-btn delete-btn"
                                    title={t(DELETE_TITLE)}
                                    onClick={() =>
                                        showConfirmDialog(subject.id, dialogTypes.DELETE_CONFIRM)
                                    }
                                />
                            </div>
                        </Card>
                    ))}
                </section>
            </div>
            <SnackbarComponent
                message={snackbarMessage}
                type={snackbarType}
                isOpen={isSnackbarOpen}
                handleSnackbarClose={handleSnackbarCloseService}
            />
        </>
    );
};
const mapStateToProps = (state) => ({
    subjects: state.subjects.subjects,
    disabledSubjects: state.subjects.disabledSubjects,
    isSnackbarOpen: state.snackbar.isSnackbarOpen,
    snackbarType: state.snackbar.snackbarType,
    snackbarMessage: state.snackbar.message,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
});
const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
});

export default connect(mapStateToProps, mapDispatchToProps)(SubjectPage);
