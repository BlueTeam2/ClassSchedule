import { connect } from 'react-redux';
import SemesterList from '../../components/Semester/SemesterList/SemesterList';
import {
    createArchivedSemesterStart,
    getArchivedSemesterByIdStart,
    selectSemesterSuccess,
    updateSemesterStart,
    deleteSemesterStart,
    updateSemesterByIdStartSuccess,
    setSemesterCopyStart,
    setGroupsToSemesterStart,
    toggleSemesterVisibilityStart,
} from '../../actions/semesters';
import { setIsOpenConfirmDialog } from '../../actions/dialog';
import { setOpenSuccessSnackbar } from '../../actions/snackbar';

const mapStateToProps = (state) => ({
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
    semesters: state.semesters.semesters,
    loading: state.loadingIndicator.loading,
});

const mapDispatchToProps = (dispatch) => ({
    createArchivedSemester: (semesterId) => dispatch(createArchivedSemesterStart(semesterId)),
    getArchivedSemesterById: (semesterId) => dispatch(getArchivedSemesterByIdStart(semesterId)),
    selectSemester: (semesterId) => dispatch(selectSemesterSuccess(semesterId)),
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    updateSemesterSuccess: (item) => dispatch(updateSemesterStart(item)),
    removeSemesterCard: (semesterId) => dispatch(deleteSemesterStart(semesterId)),
    setDefaultSemesterById: (semesterId) => dispatch(updateSemesterByIdStartSuccess(semesterId)),
    semesterCopy: (values) => dispatch(setSemesterCopyStart(values)),
    setOpenSuccessSnackbar: (message) => dispatch(setOpenSuccessSnackbar(message)),
    setGroupsToSemester: (semesterId, groups) =>
        dispatch(setGroupsToSemesterStart(semesterId, groups)),
    toggleSemesterVisibility: (semester) => dispatch(toggleSemesterVisibilityStart(semester)),
});

export default connect(mapStateToProps, mapDispatchToProps)(SemesterList);
