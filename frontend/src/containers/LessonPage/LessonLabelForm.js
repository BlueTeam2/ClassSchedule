import { connect } from 'react-redux';
import LessonLabelForm from '../../components/LessonsPage/LessonForm/LessonLabelForm';

const mapStateToProps = (state) => ({
    lesson: state.lesson.lesson,
    groupId: state.lesson.groupId,
});

export default connect(mapStateToProps)(LessonLabelForm);
