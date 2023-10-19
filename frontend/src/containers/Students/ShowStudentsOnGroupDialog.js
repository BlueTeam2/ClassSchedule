import { connect } from 'react-redux';
import ShowStudentsOnGroupDialog from '../../share/DialogWindows/_dialogWindows/ShowStudentsOnGroupDialog';
import { getAllStudentsStart, uploadStudentsToGroupStart } from '../../actions/students';

const mapStateToProps = (state) => ({
    loading: state.loadingIndicator.studentsLoading,
    students: state.students.students,
    groups: state.groups.groups,
});

const mapDispatchToProps = {
    getAllStudentsStart,
    uploadStudentsToGroupStart,
};

export default connect(mapStateToProps, mapDispatchToProps)(ShowStudentsOnGroupDialog);
