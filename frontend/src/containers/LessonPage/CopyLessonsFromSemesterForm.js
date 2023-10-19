import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { COPY_LESSONS_FROM_SEMESTER_FORM } from '../../constants/reduxForms';
import CopyLessonsFromSemesterForm from '../../components/LessonsPage/CopyLessonsFromSemesterForm/CopyLessonsFromSemesterForm';

const mapStateToProps = (state) => ({
    semesters: state.semesters.semesters,
    currentSemester: state.schedule.currentSemester,
});

export default connect(mapStateToProps)(
    reduxForm({
        form: COPY_LESSONS_FROM_SEMESTER_FORM,
    })(CopyLessonsFromSemesterForm),
);
