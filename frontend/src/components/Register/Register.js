import React from 'react';
import { authTypes } from '../../constants/auth';
import '../Auth/Auth.scss';
import Auth from '../../containers/Auth/Auth';

const Register = () => {
    return <Auth authType={authTypes.REGISTRATION} />;
};
export { Register };
