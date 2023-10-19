import './GroupCard.scss';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import { FaEdit, FaUserPlus, FaUsers } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { dialogTypes } from '../../../constants/dialogs';
import { ADD_STUDENT_LINK, GROUP_LIST_LINK, SHOW_STUDENTS_LINK } from '../../../constants/links';
import { getShortTitle } from '../../../helper/shortTitle';
import {
    COMMON_EDIT,
    COMMON_SET_DISABLED,
    COMMON_SET_ENABLED,
} from '../../../constants/translationLabels/common';
import {
    DELETE_TITLE_LABEL,
    FORM_SHOW_STUDENTS,
    FORM_STUDENT_ADD_LABEL,
    GROUP_LABEL,
} from '../../../constants/translationLabels/formElements';

const GroupCard = (props) => {
    const {
        group,
        disabled,
        setGroup,
        showConfirmDialog,
        showStudentsByGroup,
        showAddStudentDialog,
    } = props;
    const { t } = useTranslation('formElements');
    return (
        <div className="group-card">
            <div className="group-card-buttons">
                {!disabled ? (
                    <>
                        <IoMdEye
                            className="eye-icon-btn"
                            title={t(COMMON_SET_DISABLED)}
                            onClick={() => {
                                showConfirmDialog(group.id, dialogTypes.SET_VISIBILITY_DISABLED);
                            }}
                        />
                        <FaEdit
                            className="edit-icon-btn"
                            title={t(COMMON_EDIT)}
                            onClick={() => setGroup(group)}
                        />
                    </>
                ) : (
                    <GiSightDisabled
                        className="eye-icon-btn"
                        title={t(COMMON_SET_ENABLED)}
                        onClick={() => {
                            showConfirmDialog(group.id, dialogTypes.SET_VISIBILITY_ENABLED);
                        }}
                    />
                )}
                <MdDelete
                    className="delete-icon-btn"
                    title={t(DELETE_TITLE_LABEL)}
                    onClick={() => showConfirmDialog(group.id, dialogTypes.DELETE_CONFIRM)}
                />
                <Link to={`${GROUP_LIST_LINK}/${group.id}${ADD_STUDENT_LINK}`}>
                    <FaUserPlus
                        title={t(FORM_STUDENT_ADD_LABEL)}
                        className="group-card-buttons-add-student"
                        onClick={() => {
                            showAddStudentDialog(group.id);
                        }}
                    />
                </Link>
            </div>
            <p className="group-card__description">{`${t(GROUP_LABEL)}:`}</p>
            <h3 className="group-card__number">{getShortTitle(group.title, 5)}</h3>
            <Link to={`${GROUP_LIST_LINK}/${group.id}${SHOW_STUDENTS_LINK}`}>
                <FaUsers
                    title={t(FORM_SHOW_STUDENTS)}
                    className="group-card-button-students"
                    onClick={() => {
                        showStudentsByGroup(group.id);
                    }}
                />
            </Link>
        </div>
    );
};

export default GroupCard;
