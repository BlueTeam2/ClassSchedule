import { connect } from 'react-redux';
import { selectGroupId } from '../../actions';
import Search from '../../components/LessonsPage/Search';

const mapStateToProps = (state) => ({
    groups: state.groups.groups,
    group: state.groups.group,
});

const mapDispatchToProps = (dispatch) => ({
    selectByGroupId: (groupId) => dispatch(selectGroupId(groupId)),
});

export default connect(mapStateToProps, mapDispatchToProps)(Search);
