import React, { useEffect, useState } from 'react';
import { FaEdit, FaUsers, FaFileArchive } from 'react-icons/fa';
import { MdDelete, MdDonutSmall } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import './SemesterList.scss';
import { GiSightDisabled, IoMdEye, FaCopy } from 'react-icons/all';
import { get, isEqual, isEmpty } from 'lodash';
import Card from '../../share/Card/Card';
import NotFound from '../../share/NotFound/NotFound';
import { dialogTypes, dialogCloseButton } from '../../constants/dialogs';
import {
    EDIT_TITLE,
    DELETE_TITLE,
    COPY_LABEL,
    SEMESTERY_LABEL,
    FORM_SHOW_GROUPS,
    SET_DEFAULT_TITLE,
    SEMESTER_COPY_LABEL,
} from '../../constants/translationLabels/formElements';
import {
    EXIST_LABEL,
    GROUP_EXIST_IN_THIS_SEMESTER,
} from '../../constants/translationLabels/serviceMessages';
import {
    COMMON_SET_DISABLED,
    COMMON_DAYS_LABEL,
    COMMON_CLASS_SCHEDULE_MANAGEMENT_TITLE,
    SEMESTER_LABEL,
    COMMON_MAKE_ARCHIVE,
    COMMON_SET_ENABLED,
    COMMON_GROUP_TITLE,
} from '../../constants/translationLabels/common';
import { search } from '../../helper/search';
import { getGroupsOptionsForSelect } from '../../utils/selectUtils';
import SemesterCopyForm from '../../containers/SemesterPage/SemesterCopyForm';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { MultiselectForGroups } from '../../helper/MultiselectForGroups';
import i18n from '../../i18n';

const SemesterList = (props) => {
    const { t } = useTranslation('formElements');
    const {
        archived,
        disabled,
        setSemesterId,
        term,
        semesters,
        selectSemester,
        createArchivedSemester,
        setOpenConfirmDialog,
        updateSemester,
        removeSemesterCard,
        setDefaultSemesterById,
        isOpenConfirmDialog,
        semesterCopy,
        semesterId,
        options,
        setOpenSuccessSnackbar,
        setGroupsToSemester,
        // archivedSemesters,
        // getArchivedSemester,
    } = props;
    const [isOpenSemesterCopyForm, setIsOpenSemesterCopyForm] = useState(false);
    const [confirmDialogType, setConfirmDialogType] = useState('');
    const [isOpenGroupsDialog, setIsOpenGroupsDialog] = useState(false);

    const [semesterOptions, setSemesterOptions] = useState([]);

    const searchArr = ['year', 'description', 'startDay', 'endDay'];

    const visibleItems = search(semesters, term, searchArr);

    const showConfirmDialog = (id, dialogType) => {
        setSemesterId(id);
        setConfirmDialogType(dialogType);
        setOpenConfirmDialog(true);
    };
    const submitSemesterCopy = ({ toSemesterId }) => {
        semesterCopy({
            fromSemesterId: semesterId,
            toSemesterId,
        });
        setIsOpenSemesterCopyForm(false);
    };
    const closeSemesterCopyForm = () => {
        setIsOpenSemesterCopyForm(false);
    };
    const changeSemesterDisabledStatus = (currentSemesterId) => {
        const foundSemester = semesters.find(
            (semesterItem) => semesterItem.id === currentSemesterId,
        );
        const changeDisabledStatus = {
            [dialogTypes.SET_VISIBILITY_ENABLED]: updateSemester({
                ...foundSemester,
                disable: false,
            }),
            [dialogTypes.SET_VISIBILITY_DISABLED]: updateSemester({
                ...foundSemester,
                disable: true,
            }),
        };
        return changeDisabledStatus[confirmDialogType];
    };

    const acceptConfirmDialog = (currentSemesterId) => {
        setOpenConfirmDialog(false);
        const isDisabled = disabled;
        if (confirmDialogType === dialogTypes.SET_DEFAULT) {
            setDefaultSemesterById(currentSemesterId, isDisabled);
        } else if (confirmDialogType !== dialogTypes.DELETE_CONFIRM) {
            changeSemesterDisabledStatus(currentSemesterId);
        } else {
            removeSemesterCard(currentSemesterId);
        }
    };
    // it doesnt work, need to finish implement archeved functionality
    // const handleSemesterArchivedPreview = (currentSemesterId) => {
    //     getArchivedSemester(+currentSemesterId);
    // };
    const onChangeGroups = () => {
        const semester = semesters.find((semesterItem) => semesterItem.id === semesterId);
        const beginGroups = !isEmpty(semester.semester_groups)
            ? getGroupsOptionsForSelect(semester.semester_groups)
            : [];
        const finishGroups = [...semesterOptions];
        if (isEqual(beginGroups, finishGroups)) {
            setOpenSuccessSnackbar(
                i18n.t(GROUP_EXIST_IN_THIS_SEMESTER, {
                    cardType: i18n.t(COMMON_GROUP_TITLE),
                    actionType: i18n.t(EXIST_LABEL),
                }),
            );
            return;
        }
        setGroupsToSemester(semesterId, semesterOptions);
        setIsOpenGroupsDialog(false);
    };
    const cancelMultiselect = () => {
        setIsOpenGroupsDialog(false);
    };
    return (
        <>
            {isOpenConfirmDialog && (
                <CustomDialog
                    type={confirmDialogType}
                    whatDelete="semester"
                    open={isOpenConfirmDialog}
                    handelConfirm={() => acceptConfirmDialog(semesterId)}
                />
            )}
            {isOpenSemesterCopyForm && (
                <CustomDialog
                    title={t(SEMESTER_COPY_LABEL)}
                    open={isOpenSemesterCopyForm}
                    onClose={closeSemesterCopyForm}
                    buttons={[dialogCloseButton(closeSemesterCopyForm)]}
                >
                    <SemesterCopyForm
                        semesterId={semesterId}
                        onSubmit={submitSemesterCopy}
                        submitButtonLabel={t(COPY_LABEL)}
                        semesters={semesters}
                    />
                </CustomDialog>
            )}
            <section className="container-flex-wrap wrapper">
                {visibleItems.length === 0 && <NotFound name={t(SEMESTERY_LABEL)} />}
                {visibleItems.map((semesterItem) => {
                    const semDays = semesterItem.semester_days.map((day) =>
                        t(`common:day_of_week_${day}`),
                    );
                    const groups = get(semesterItem, 'semester_groups')
                        ? getGroupsOptionsForSelect(semesterItem.semester_groups)
                        : [];

                    return (
                        <Card
                            key={semesterItem.id}
                            additionClassName={`semester-card done-card ${
                                semesterItem.currentSemester ? 'current' : ''
                            }`}
                        >
                            <div className="cards-btns">
                                {!(disabled || archived) && (
                                    <>
                                        <IoMdEye
                                            className="svg-btn copy-btn"
                                            title={t(COMMON_SET_DISABLED)}
                                            onClick={() => {
                                                showConfirmDialog(
                                                    semesterItem.id,
                                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                                );
                                            }}
                                        />
                                        <FaEdit
                                            className="svg-btn edit-btn"
                                            title={t(EDIT_TITLE)}
                                            onClick={() => {
                                                selectSemester(semesterItem.id);
                                            }}
                                        />
                                        <FaCopy
                                            className="svg-btn copy-btn"
                                            title={t(COPY_LABEL)}
                                            onClick={() => {
                                                setIsOpenSemesterCopyForm(true);
                                                setSemesterId(semesterItem.id);
                                            }}
                                        />
                                        {!semesterItem.currentSemester && (
                                            <FaFileArchive
                                                className="svg-btn archive-btn"
                                                title={t(COMMON_MAKE_ARCHIVE)}
                                                onClick={() => {
                                                    createArchivedSemester(semesterItem.id);
                                                }}
                                            />
                                        )}
                                    </>
                                )}
                                {disabled && !archived && (
                                    <GiSightDisabled
                                        className="svg-btn copy-btn"
                                        title={t(COMMON_SET_ENABLED)}
                                        onClick={() => {
                                            showConfirmDialog(
                                                semesterItem.id,
                                                dialogTypes.SET_VISIBILITY_ENABLED,
                                            );
                                        }}
                                    />
                                )}
                                {/* {archived && (
                                <IoMdEye
                                    className="svg-btn copy-btn"
                                    title={t(COMMON_PREVIEW)}
                                    onClick={() => {
                                        handleSemesterArchivedPreview(semesterItem.id);
                                    }}
                                />
                            )} */}
                                <MdDelete
                                    className="svg-btn delete-btn"
                                    title={t(DELETE_TITLE)}
                                    onClick={() =>
                                        showConfirmDialog(
                                            semesterItem.id,
                                            dialogTypes.DELETE_CONFIRM,
                                        )
                                    }
                                />

                                <MdDonutSmall
                                    className={`svg-btn edit-btn ${
                                        semesterItem.defaultSemester ? 'default' : ''
                                    }`}
                                    title={t(SET_DEFAULT_TITLE)}
                                    onClick={() =>
                                        showConfirmDialog(semesterItem.id, dialogTypes.SET_DEFAULT)
                                    }
                                />
                            </div>

                            <p className="semester-card__description">
                                <small>{`${t(SEMESTER_LABEL)}:`}</small>
                                <b>{semesterItem.description}</b>
                                {` ( ${semesterItem.year} )`}
                            </p>
                            <p className="semester-card__description">
                                <b>
                                    {semesterItem.startDay} - {semesterItem.endDay}
                                </b>
                            </p>
                            <p className="semester-card__description">
                                {`${t(COMMON_DAYS_LABEL)}: `}
                                {semDays.join(', ')}
                            </p>
                            <p className="semester-card__description">
                                {`${t(COMMON_CLASS_SCHEDULE_MANAGEMENT_TITLE)}: `}
                                {semesterItem.semester_classes
                                    .map((classItem) => {
                                        return classItem.class_name;
                                    })
                                    .join(', ')}
                            </p>

                            <FaUsers
                                title={t(FORM_SHOW_GROUPS)}
                                className="svg-btn copy-btn  semester-groups"
                                onClick={() => {
                                    setSemesterId(semesterItem.id);
                                    setSemesterOptions(groups);
                                    setIsOpenGroupsDialog(true);
                                }}
                            />
                        </Card>
                    );
                })}
            </section>
            <MultiselectForGroups
                open={isOpenGroupsDialog}
                options={options}
                value={semesterOptions}
                onChange={setSemesterOptions}
                onCancel={cancelMultiselect}
                onClose={onChangeGroups}
            />
        </>
    );
};

export default SemesterList;
