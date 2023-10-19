import './ProfilePage.scss';
import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { useTranslation } from 'react-i18next';
import Card from '../../share/Card/Card';
import { resetFormHandler } from '../../helper/formHelper';
import { PROFILE_FORM, TEACHER_FORM } from '../../constants/reduxForms';
import ChangePasswordForm from '../../components/ChangePasswordForm/ChangePasswordForm';
import { getUserProfile, updateUserPassword, updateUserTeacher } from '../../services/userService';
import AddTeacherForm from '../../components/TeachersPage/AddTeacherForm/AddTeacherForm';
import { EMAIL_LABEL } from '../../constants/translationLabels/formElements';
import { COMMON_MY_PROFILE, DIFFERENT_PASSWORDS } from '../../constants/translationLabels/common';

const ProfilePage = (props) => {
    const { t } = useTranslation('formElements');

    const handlePasswordFormReset = () => resetFormHandler(PROFILE_FORM);
    const submitPasswordChange = (values) => {
        if (values.new_password !== values.confirm_password) {
            props.setError({
                registration: { passwords: t(DIFFERENT_PASSWORDS) },
            });
            return;
        }
        updateUserPassword(values);
        handlePasswordFormReset();
    };

    const submitTeacherChange = (values) => {
        updateUserTeacher(values);
    };
    const handleTeacherFormReset = () => resetFormHandler(TEACHER_FORM);
    useEffect(() => {
        getUserProfile();
    }, [localStorage.getItem('userRole')]);

    const renderTeacherdataForm = () => {
        if (localStorage.getItem('userRole') === 'ROLE_TEACHER') {
            return (
                <AddTeacherForm onSubmit={submitTeacherChange} onReset={handleTeacherFormReset} />
            );
        }
        return null;
    };

    return (
        <>
            <Card additionClassName="form-card">
                <h2 className="form-title">{t(COMMON_MY_PROFILE)}</h2>
                <section>
                    <span>{`${t(EMAIL_LABEL)}: `}</span>
                    <span>{localStorage.getItem('email')}</span>
                </section>
                <ChangePasswordForm
                    onSubmit={submitPasswordChange}
                    onReset={handlePasswordFormReset}
                />
            </Card>
            {renderTeacherdataForm()}
        </>
    );
};

const mapStateToProps = (state) => ({
    user: state.users.user,
});

export default connect(mapStateToProps)(ProfilePage);
