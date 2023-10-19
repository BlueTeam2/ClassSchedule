import { connect } from 'react-redux';
import LessonTemporaryCardCell from '../../components/GroupSchedulePage/LessonTemporaryCardCell';

const mapStateToProps = (state) => ({
    place: state.schedule.place,
});

export default connect(mapStateToProps)(LessonTemporaryCardCell);
