import React from 'react';
import { connect } from 'react-redux';
import AccessRoute from './AccessRoute';

function UserRoute({ component: Component, userRole, ...rest }) {
    return <AccessRoute condition={userRole} component={Component} {...rest}></AccessRoute>;
}
const mapStateToProps = (state) => ({ userRole: state.auth.role });

export default connect(mapStateToProps)(UserRoute);
