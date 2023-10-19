import React from 'react';
import { authTypes } from '../../constants/auth';
import '../Auth/Auth.scss';
import Auth from '../../containers/Auth/Auth';

const Login = () => {
    return <Auth authType={authTypes.LOGIN} />;
};
export { Login };
