import { connect } from 'react-redux';
import { selectLessonCardSuccess } from '../../actions';
import Lessons from '../../components/LessonsPage/Lessons';

const mapStateToProps = (state) => ({
    groupId: state.lesson.groupId,
    loading: state.loadingIndicator.loading,
    group: state.groups.group,
});

const mapDispatchToProps = (dispatch) => ({
    selectLessonCardSuccess: (lessonCardId) => dispatch(selectLessonCardSuccess(lessonCardId)),
});

export default connect(mapStateToProps, mapDispatchToProps)(Lessons);
