import { connect } from 'react-redux';
import { logout } from '../../actions/index';
import Logout from '../../components/Auth/Logout';

const mapDispatchToProps = (dispatch) => {
    return {
        onLogout: () => dispatch(logout()),
    };
};

export default connect(null, mapDispatchToProps)(Logout);
