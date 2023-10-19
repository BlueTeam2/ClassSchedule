import { connect } from 'react-redux';
import GroupsList from '../../components/GroupSchedulePage/GroupsList';

const mapStateToProps = (state) => ({
    groups: state.groups.groups,
});

export default connect(mapStateToProps)(GroupsList);
