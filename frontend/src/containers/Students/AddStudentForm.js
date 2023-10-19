import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { STUDENT_FORM } from '../../constants/reduxForms';
import { AddStudentForm } from '../../components/AddStudentForm/AddStudentForm';
import { submitStudentStart } from '../../actions/students';

const mapStateToProps = (state) => ({
    groups: state.groups.groups,
    student: state.students.student,
});

export default connect(mapStateToProps, { submitStudentStart })(
    reduxForm({
        form: STUDENT_FORM,
    })(AddStudentForm),
);
