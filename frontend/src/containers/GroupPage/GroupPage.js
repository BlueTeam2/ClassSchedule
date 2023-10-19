import { connect } from 'react-redux';
import { setIsOpenConfirmDialog } from '../../actions/dialog';
import {
    clearGroupStart,
    deleteGroupStart,
    getDisabledGroupsStart,
    getEnabledGroupsStart,
    selectGroupSuccess,
    toggleDisabledStatus,
    dragAndDropGroupStart,
} from '../../actions/groups';
import { deleteStudentStart } from '../../actions/students';
import GroupPage from '../../components/GroupPage/GroupPage';

const mapStateToProps = (state) => ({
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
    isSnackbarOpen: state.snackbar.isSnackbarOpen,
    snackbarType: state.snackbar.snackbarType,
    snackbarMessage: state.snackbar.message,
    groups: state.groups.groups,
    loading: state.loadingIndicator.loading,
});

const mapDispatchToProps = {
    getEnabledGroupsStart,
    getDisabledGroupsStart,
    setIsOpenConfirmDialog,
    dragAndDropGroupStart,
    toggleDisabledStatus,
    deleteStudentStart,
    deleteGroupStart,
    clearGroupStart,
    selectGroupSuccess,
};

export default connect(mapStateToProps, mapDispatchToProps)(GroupPage);
