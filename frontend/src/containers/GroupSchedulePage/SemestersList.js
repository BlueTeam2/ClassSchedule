import { connect } from 'react-redux';
import { getAllPublicGroupsStart } from '../../actions/groups';
import SemestersList from '../../components/GroupSchedulePage/SemestersList';

const mapStateToProps = (state) => ({
    semesters: state.schedule.semesters,
});

const mapDispatchToProps = (dispatch) => ({
    getAllGroups: (id) => dispatch(getAllPublicGroupsStart(id)),
});

export default connect(mapStateToProps, mapDispatchToProps)(SemestersList);
