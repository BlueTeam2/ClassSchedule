import React from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import { FaEdit, FaUserPlus, FaUsers } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { links } from '../../constants/links';
import { getShortTitle } from '../../helper/shortTitle';
import { dialogTypes } from '../../constants/dialogs';
import {
    COMMON_SET_DISABLED,
    COMMON_EDIT,
    COMMON_SET_ENABLED,
} from '../../constants/translationLabels/common';
import {
    DELETE_TITLE_LABEL,
    FORM_SHOW_STUDENTS,
    FORM_STUDENT_ADD_LABEL,
    GROUP_LABEL,
} from '../../constants/translationLabels/formElements';

const GroupCard = (props) => {
    const {
        disabled,
        groupItem,
        handleAddUser,
        onShowStudentByGroup,
        showConfirmDialog,
        handleSetGroupToUpdateForm,
    } = props;
    const { t } = useTranslation('formElements');
    return (
        <section key={groupItem.id} className="group-card">
            <div className="group__buttons-wrapper">
                {!disabled ? (
                    <>
                        <IoMdEye
                            className="group__buttons-hide link-href"
                            title={t(COMMON_SET_DISABLED)}
                            onClick={() => {
                                showConfirmDialog(
                                    groupItem.id,
                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                );
                            }}
                        />
                        <Link to={`${links.GroupList}/${groupItem.id}${links.Edit}`}>
                            <FaEdit
                                className="group__buttons-edit link-href"
                                title={t(COMMON_EDIT)}
                                onClick={() => handleSetGroupToUpdateForm(groupItem.id)}
                            />
                        </Link>
                    </>
                ) : (
                    <GiSightDisabled
                        className="group__buttons-hide link-href"
                        title={t(COMMON_SET_ENABLED)}
                        onClick={() => {
                            showConfirmDialog(groupItem.id, dialogTypes.SET_VISIBILITY_ENABLED);
                        }}
                    />
                )}
                <MdDelete
                    className="group__buttons-delete link-href"
                    title={t(DELETE_TITLE_LABEL)}
                    onClick={() => showConfirmDialog(groupItem.id, dialogTypes.DELETE_CONFIRM)}
                />
                <Link to={`${links.GroupList}/${groupItem.id}${links.AddStudent}`}>
                    <FaUserPlus
                        title={t(FORM_STUDENT_ADD_LABEL)}
                        className="svg-btn copy-btn align-left info-btn"
                        onClick={() => {
                            handleAddUser(groupItem.id);
                        }}
                    />
                </Link>
            </div>
            <p className="group-card__description">{`${t(GROUP_LABEL)}:`}</p>
            <h1 className="group-card__number">{getShortTitle(groupItem.title, 5)}</h1>
            <Link to={`${links.GroupList}/${groupItem.id}${links.ShowStudents}`}>
                <span className="students-group">
                    <FaUsers
                        title={t(FORM_SHOW_STUDENTS)}
                        className="svg-btn copy-btn align-left info-btn students"
                        onClick={() => {
                            onShowStudentByGroup(groupItem.id);
                        }}
                    />
                </span>
            </Link>
        </section>
    );
};

export default GroupCard;
