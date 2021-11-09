import './TeachersList.scss';
import React from 'react';
import { FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import Card from '../../share/Card/Card';

import { dialogTypes } from '../../constants/dialogs';
import { getTeacherFullName } from '../../helper/renderTeacher';
import { getShortTitle } from '../../helper/shortTitle';
import { selectTeacherCardService } from '../../services/teacherService';
import {
    COMMON_SET_DISABLED,
    COMMON_EDIT_HOVER_TITLE,
    COMMON_DELETE_HOVER_TITLE,
    COMMON_SET_ENABLED,
    TEACHER_DEPARTMENT,
} from '../../constants/translationLabels/common';

const TeachersCard = (props) => {
    const { t } = useTranslation('common');

    const { isDisabled, showConfirmDialog, teacherItem, selectedTeacherCard } = props;
    return (
        <Card key={teacherItem.id} additionClassName="teacher-card done-card">
            <div className="cards-btns">
                {!isDisabled ? (
                    <>
                        <GiSightDisabled
                            className="svg-btn copy-btn"
                            title={t(COMMON_SET_DISABLED)}
                            onClick={() => {
                                showConfirmDialog(
                                    teacherItem.id,
                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                );
                            }}
                        />
                        <FaEdit
                            className="svg-btn edit-btn"
                            title={t(COMMON_EDIT_HOVER_TITLE)}
                            onClick={() => {
                                selectedTeacherCard(teacherItem.id);
                                console.log('teacherItems 1111', teacherItem.id);
                            }}
                        />
                    </>
                ) : (
                    <IoMdEye
                        className="svg-btn copy-btn"
                        title={t(COMMON_SET_ENABLED)}
                        onClick={() => {
                            showConfirmDialog(teacherItem.id, dialogTypes.SET_VISIBILITY_ENABLED);
                        }}
                    />
                )}
                <MdDelete
                    className="svg-btn delete-btn"
                    title={t(COMMON_DELETE_HOVER_TITLE)}
                    onClick={() => showConfirmDialog(teacherItem.id, dialogTypes.DELETE_CONFIRM)}
                />
            </div>
            <h2 className="teacher-card-name">
                {getShortTitle(getTeacherFullName(teacherItem), 30)}
            </h2>
            <p className="teacher-card-title">
                {`${teacherItem.position} ${
                    teacherItem.department !== null
                        ? `${t(TEACHER_DEPARTMENT)} ${teacherItem.department.name}`
                        : ''
                }`}
            </p>
            <p>{`${teacherItem.email}`}</p>
        </Card>
    );
};

export default TeachersCard;
