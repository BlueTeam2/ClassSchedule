import { connect } from 'react-redux';
import TeacherTemporaryCardCell from '../../components/GroupSchedulePage/TeacherTemporaryCardCell';

const mapStateToProps = (state) => ({
    place: state.schedule.place,
});

export default connect(mapStateToProps)(TeacherTemporaryCardCell);
