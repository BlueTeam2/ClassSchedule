import React from 'react';
import { authTypes } from '../../constants/auth';
import '../Auth/Auth.scss';
import Auth from '../../containers/Auth/Auth';

const ResetPassword = () => {
    return <Auth authType={authTypes.RESET_PASSWORD} />;
};
export { ResetPassword };
