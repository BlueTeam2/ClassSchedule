import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { selectLessonCardSuccess, setUniqueError, selectGroupSuccess } from '../../actions';
import LessonForm from '../../components/LessonsPage/LessonForm/LessonForm';
import { LESSON_FORM } from '../../constants/reduxForms';

const mapStateToProps = (state) => ({
    lesson: state.lesson.lesson,
    groups: state.groups.groups,
    group: state.groups.group,
    isUniqueError: state.lesson.uniqueError,
    groupId: state.lesson.groupId,
    subjects: state.subjects.subjects,
    lessonTypes: state.lesson.lessonTypes,
    teachers: state.teachers.teachers,
    currentSemester: state.schedule.currentSemester,
});

const mapDispatchToProps = (dispatch) => ({
    selectLessonCardSuccess: (lessonCardId) => dispatch(selectLessonCardSuccess(lessonCardId)),
    setUniqueError: (isUniqueError) => dispatch(setUniqueError(isUniqueError)),
    selectGroupSuccess: (id) => dispatch(selectGroupSuccess(id)),
});

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(
    reduxForm({
        form: LESSON_FORM,
    })(LessonForm),
);
