import { connect } from 'react-redux';
import SemesterPage from '../../components/Semester/SemesterPage';
import {
    getAllSemestersStart,
    getDisabledSemestersStart,
    getArchivedSemestersStart,
    handleSemesterFormSubmitStart,
} from '../../actions/semesters';
import { getEnabledGroupsStart } from '../../actions/groups';
import { setOpenErrorSnackbar } from '../../actions/snackbar';

const mapStateToProps = (state) => ({
    archivedSemesters: state.semesters.archivedSemesters,
    isSnackbarOpen: state.snackbar.isSnackbarOpen,
    snackbarType: state.snackbar.snackbarType,
    snackbarMessage: state.snackbar.message,
    semester: state.semesters.semester,
    groups: state.groups.groups,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
    classScheduler: state.classActions.classScheduler,
});
const mapDispatchToProps = (dispatch) => ({
    getAllSemestersItems: () => dispatch(getAllSemestersStart()),
    getDisabledSemestersItems: () => dispatch(getDisabledSemestersStart()),
    getArchivedSemestersItems: () => dispatch(getArchivedSemestersStart()),
    handleSemesterFormSubmit: (values) => dispatch(handleSemesterFormSubmitStart(values)),
    setOpenErrorSnackbar: (message) => dispatch(setOpenErrorSnackbar(message)),
    getAllGroupsItems: () => dispatch(getEnabledGroupsStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(SemesterPage);
