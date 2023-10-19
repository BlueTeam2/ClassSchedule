import React from 'react';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import { connect } from 'react-redux';

import Header from '../components/Header/Header';

import HomePage from '../containers/Home/Home';
import TeacherSchedule from '../components/TeacherSchedule/TeacherSchedule';
import Logout from '../containers/Auth/Logout';
import ActivationPage from '../containers/ActivationPage/ActivationPage';
import ProfilePage from '../containers/ProfilePage/ProfilePage';
import EditCurrentSchedulePage from '../containers/EditCurrentSchedule/EditCurrentSchedulePage';
import {
    HOME_PAGE_LINK,
    SCHEDULE_FOR_LINK,
    ACTIVATION_PAGE_LINK,
    LOGIN_LINK,
    LOGOUT_LINK,
    RESET_PASSWORD_LINK,
    REGISTRATION_LINK,
    SCHEDULE_PAGE_LINK,
    MY_PROFILE_LINK,
    TEACHER_SCHEDULE_LINK,
    ADMIN_PAGE_LINK,
} from '../constants/links';
import { userRoles } from '../constants/userRoles';
import GroupSchedulePage from '../containers/GroupSchedulePage/GroupSchedulePage';
import { Register } from '../components/Register/Register';
import { ResetPassword } from '../components/ResetPassword/ResetPassword';
import { Login } from '../components/Login/Login';
import { AdminRoute, UserRoute } from './routes';
import AdminRouter from './AdminRouter';

const Routers = (props) => {
    const { userRole } = props;
    return (
        <Router>
            <Header userRole={userRole} roles={userRoles} />
            <Switch>
                <Route exact path={HOME_PAGE_LINK} component={HomePage} />
                <Route path={SCHEDULE_FOR_LINK} component={GroupSchedulePage} />
                <Route path={ACTIVATION_PAGE_LINK} component={ActivationPage} />
                <Route path={RESET_PASSWORD_LINK} component={ResetPassword} />
                <Route path={REGISTRATION_LINK} component={Register} />
                <Route path={LOGIN_LINK} component={Login} />
                <UserRoute path={TEACHER_SCHEDULE_LINK} component={TeacherSchedule} />
                <UserRoute path={MY_PROFILE_LINK} component={ProfilePage} />
                <UserRoute path={LOGOUT_LINK} component={Logout} />
                <AdminRoute path={SCHEDULE_PAGE_LINK} component={EditCurrentSchedulePage} />
                <AdminRoute path={ADMIN_PAGE_LINK} component={AdminRouter} />
                <UserRoute path={ACTIVATION_PAGE_LINK}>
                    <Redirect to={HOME_PAGE_LINK} />
                </UserRoute>
                <UserRoute path={SCHEDULE_PAGE_LINK}>
                    <Redirect to={LOGIN_LINK} />
                </UserRoute>
            </Switch>
        </Router>
    );
};

const mapStateToProps = (state) => ({ userRole: state.auth.role });

export default connect(mapStateToProps)(Routers);
