import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import { FaChalkboardTeacher, FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import SearchPanel from '../../share/SearchPanel/SearchPanel';
import Card from '../../share/Card/Card';
import AddDepartment from '../../components/AddDepartmentForm/AddDepartmentForm';
import { search } from '../../helper/search';
import NotFound from '../../share/NotFound/NotFound';
import SnackbarComponent from '../../share/Snackbar/SnackbarComponent';
import { handleSnackbarCloseService } from '../../services/snackbarService';
import {
    clearDepartment,
    createDepartmentService,
    deleteDepartmentsService,
    getAllDepartmentsService,
    getDepartmentByIdService,
    getDisabledDepartmentsService,
    setDisabledDepartmentService,
    setEnabledDepartmentService,
    updateDepartmentService,
} from '../../services/departmentService';
import { setIsOpenConfirmDialog } from '../../actions/dialog';
import CustomDialog from '../Dialogs/CustomDialog';
import ShowDepartmentDataDialog from '../../share/DialogWindows/_dialogWindows/ShowDepartmentDataDialog';
import { dialogTypes } from '../../constants/dialogs';
import {
    EDIT_TITLE,
    DEPARTMENT_LABEL,
    DELETE_TITLE,
    SHOW_TEACHER_TITLE,
} from '../../constants/translationLabels/formElements';
import { COMMON_SET_DISABLED, COMMON_SET_ENABLED } from '../../constants/translationLabels/common';
import { getAllPublicTeachersByDepartmentStart } from '../../actions/teachers';

const DepartmentPage = (props) => {
    const {
        teachers,
        snackbarType,
        isSnackbarOpen,
        snackbarMessage,
        enabledDepartments,
        disabledDepartments,
        setOpenConfirmDialog,
        isOpenConfirmDialog,
        getAllPublicTeachersByDepartment,
    } = props;
    const { t } = useTranslation('formElements');
    const [term, setTerm] = useState('');

    const [confirmDialogType, setConfirmDialogType] = useState('');

    const [isDisabled, setIsDisabled] = useState(false);
    const [isUpdateForm, setIsUpdateForm] = useState(false);
    const [departmentId, setDepartmentId] = useState(-1);
    const [isOpenTeacherDialog, setIsOpenTeacherDialog] = useState(false);

    const clearDepartmentForm = () => {
        clearDepartment();
    };

    useEffect(() => {
        getDisabledDepartmentsService();
        getAllDepartmentsService();
        clearDepartment();
    }, []);

    const SearchChange = setTerm;
    const visibleDepartments = isDisabled
        ? search(disabledDepartments, term, ['name'])
        : search(enabledDepartments, term, ['name']);

    const submitAddForm = (data) => {
        return data.id ? updateDepartmentService(data) : createDepartmentService(data);
    };
    const showConfirmDialog = (currentId, dialogType) => {
        setDepartmentId(currentId);
        setConfirmDialogType(dialogType);
        setOpenConfirmDialog(true);
    };
    const setDepartmentToUpdate = (currentId) => {
        getDepartmentByIdService(currentId);
        setIsUpdateForm(true);
    };
    const changeDepartmentDisabledStatus = (currentId) => {
        const foundDepartment = [...disabledDepartments, ...enabledDepartments].find(
            (departm) => departm.id === currentId,
        );
        const newDepartment = { ...foundDepartment, disable: !foundDepartment.disable };
        const changeDisabledStatus = {
            [dialogTypes.SET_VISIBILITY_ENABLED]: setEnabledDepartmentService(newDepartment),
            [dialogTypes.SET_VISIBILITY_DISABLED]: setDisabledDepartmentService(newDepartment),
        };
        return changeDisabledStatus[confirmDialogType];
    };
    const acceptConfirmDialog = (currentId) => {
        setOpenConfirmDialog(false);
        if (confirmDialogType !== dialogTypes.DELETE_CONFIRM) {
            changeDepartmentDisabledStatus(currentId);
        } else deleteDepartmentsService(currentId);
    };
    const changeDisable = () => {
        setIsDisabled((prev) => !prev);
    };
    const closeTeacherDialog = () => {
        setIsOpenTeacherDialog(false);
    };

    return (
        <>
            <CustomDialog
                type={confirmDialogType}
                whatDelete="department"
                handelConfirm={() => acceptConfirmDialog(departmentId.id)}
                open={isOpenConfirmDialog}
            />
            {isOpenTeacherDialog && (
                <ShowDepartmentDataDialog
                    isHide={departmentId.disabledStatus}
                    cardId={departmentId.id}
                    open={isOpenTeacherDialog}
                    onClose={closeTeacherDialog}
                    teachers={teachers}
                />
            )}
            <div className="cards-container">
                <aside className="search-list__panel">
                    <SearchPanel SearchChange={SearchChange} showDisabled={changeDisable} />
                    {!isDisabled && (
                        <AddDepartment
                            onSubmit={submitAddForm}
                            clear={clearDepartmentForm}
                            editDepartment={isUpdateForm}
                        />
                    )}
                </aside>
                <section className="container-flex-wrap wrapper">
                    {visibleDepartments.length === 0 && <NotFound name={t(DEPARTMENT_LABEL)} />}
                    {visibleDepartments.map((departmentItem) => (
                        <Card
                            key={departmentItem.id}
                            additionClassName="subject-card department-card"
                        >
                            <h2 className="subject-card__name">{departmentItem.name}</h2>
                            <div className="cards-btns">
                                {isDisabled ? (
                                    <IoMdEye
                                        className="svg-btn copy-btn"
                                        title={t(COMMON_SET_ENABLED)}
                                        onClick={() => {
                                            showConfirmDialog(
                                                departmentItem.id,
                                                dialogTypes.SET_VISIBILITY_ENABLED,
                                            );
                                        }}
                                    />
                                ) : (
                                    <>
                                        <GiSightDisabled
                                            className="svg-btn copy-btn"
                                            title={t(COMMON_SET_DISABLED)}
                                            onClick={() => {
                                                showConfirmDialog(
                                                    departmentItem.id,
                                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                                );
                                            }}
                                        />

                                        <FaEdit
                                            className="svg-btn edit-btn"
                                            title={t(EDIT_TITLE)}
                                            onClick={() => {
                                                setDepartmentToUpdate(departmentItem.id);
                                            }}
                                        />
                                    </>
                                )}

                                <MdDelete
                                    className="svg-btn delete-btn"
                                    title={t(DELETE_TITLE)}
                                    onClick={() => {
                                        showConfirmDialog(
                                            departmentItem.id,
                                            dialogTypes.DELETE_CONFIRM,
                                        );
                                    }}
                                />
                                <FaChalkboardTeacher
                                    className="svg-btn delete-btn"
                                    title={t(SHOW_TEACHER_TITLE)}
                                    onClick={() => {
                                        getAllPublicTeachersByDepartment(departmentItem.id);
                                        getDepartmentByIdService(departmentItem.id);
                                        setIsOpenTeacherDialog(true);
                                    }}
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
    enabledDepartments: state.departments.departments,
    disabledDepartments: state.departments.disabledDepartments,
    department: state.departments.department,
    isSnackbarOpen: state.snackbar.isSnackbarOpen,
    snackbarType: state.snackbar.snackbarType,
    snackbarMessage: state.snackbar.message,
    teachers: state.teachers.teachers,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
});
const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    getAllPublicTeachersByDepartment: (id) => dispatch(getAllPublicTeachersByDepartmentStart(id)),
});

export default connect(mapStateToProps, mapDispatchToProps)(DepartmentPage);
