import './TeachersList.scss';
import React from 'react';
import { FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import Card from '../../../share/Card/Card';

import { dialogTypes } from '../../../constants/dialogs';
import { getTeacherFullName } from '../../../helper/renderTeacher';
import { getShortTitle } from '../../../helper/shortTitle';
import {
    COMMON_SET_DISABLED,
    COMMON_EDIT_HOVER_TITLE,
    COMMON_DELETE_HOVER_TITLE,
    COMMON_SET_ENABLED,
    TEACHER_DEPARTMENT,
} from '../../../constants/translationLabels/common';
import { MAX_LENGTH_40 } from '../../../constants/common';

const TeachersCard = (props) => {
    const { t } = useTranslation('common');

    const { isDisabled, showConfirmDialog, teacherItem, selectedTeacherCard } = props;

    const sendMail = (email) => {
        const mailto = `mailto:${email}`;
        window.location.href = mailto;
    };

    return (
        <Card key={teacherItem.id} additionClassName="teacher-card">
            <div className="cards-btns">
                {!isDisabled ? (
                    <>
                        <IoMdEye
                            className="copy-icon-btn"
                            title={t(COMMON_SET_DISABLED)}
                            onClick={() => {
                                showConfirmDialog(
                                    teacherItem.id,
                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                );
                            }}
                        />
                        <FaEdit
                            className="edit-icon-btn"
                            title={t(COMMON_EDIT_HOVER_TITLE)}
                            onClick={() => {
                                selectedTeacherCard(teacherItem.id);
                            }}
                        />
                    </>
                ) : (
                    <GiSightDisabled
                        className="copy-icon-btn"
                        title={t(COMMON_SET_ENABLED)}
                        onClick={() => {
                            showConfirmDialog(teacherItem.id, dialogTypes.SET_VISIBILITY_ENABLED);
                        }}
                    />
                )}
                <MdDelete
                    className="delete-icon-btn"
                    title={t(COMMON_DELETE_HOVER_TITLE)}
                    onClick={() => showConfirmDialog(teacherItem.id, dialogTypes.DELETE_CONFIRM)}
                />
            </div>
            <h2 className="teacher-card__name">
                {getShortTitle(getTeacherFullName(teacherItem), MAX_LENGTH_40)}
            </h2>
            <p className="teacher-card__title">
                {`${teacherItem.position} ${
                    teacherItem.department !== null
                        ? `${t(TEACHER_DEPARTMENT)} ${teacherItem.department.name}`
                        : ''
                }`}
            </p>
            {teacherItem.email && (
                <button
                    type="button"
                    className="teacher-card__email"
                    onClick={() => {
                        sendMail(teacherItem.email);
                    }}
                >
                    {teacherItem.email}
                </button>
            )}
        </Card>
    );
};

export default TeachersCard;
