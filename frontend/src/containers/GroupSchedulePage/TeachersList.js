import { connect } from 'react-redux';
import TeachersList from '../../components/GroupSchedulePage/TeachersList';

const mapStateToProps = (state) => ({
    teachers: state.teachers.teachers,
});

export default connect(mapStateToProps)(TeachersList);
