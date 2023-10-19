import React from 'react';
import { connect } from 'react-redux';
import AccessRoute from './AccessRoute';

function AdminRoute({ component: Component, userRole, ...rest }) {
    return <AccessRoute condition={userRole === 'ROLE_MANAGER'} component={Component} {...rest} />;
}
const mapStateToProps = (state) => ({ userRole: state.auth.role });

export default connect(mapStateToProps)(AdminRoute);
