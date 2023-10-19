import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

import { isNil } from 'lodash';

import TextField from '@material-ui/core/TextField';
import { Autocomplete } from '@material-ui/lab';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogTypes } from '../../constants/dialogs';
import { cardType } from '../../constants/cardType';
import { COMMON_LESSON_SERVICE_IS_NOT_UNIQUE } from '../../constants/translationLabels/common';
import { search } from '../../helper/search';
import { checkUniqLesson } from '../../validation/storeValidation';
import { cardObjectHandler } from '../../helper/cardObjectHandler';

import Lessons from '../../containers/LessonPage/Lessons';
import Search from '../../containers/LessonPage/Search';
import LessonForm from '../../containers/LessonPage/LessonForm';
import CopyLessonsFromSemesterForm from '../../containers/LessonPage/CopyLessonsFromSemesterForm';
import CopyLessonDialog from './CopyLessonDialog/CopyLessonDialog';

import './LessonPage.scss';
import './LessonForm/LessonForm.scss';
import { showAllSubjectsService } from '../../services/subjectService';

import { FORM_GROUP_LABEL } from '../../constants/translationLabels/formElements';
import { trasformLink } from '../../utils/trasformLink';
import { GROUPED } from '../../constants/common';

const LessonPage = (props) => {
    const {
        currentSemester,
        isUniqueError,
        lessonTypes,
        subjects,
        teachers,
        lessons,
        groupId,
        group,
        groups,
        copyLessonCardStart,
        deleteLessonCardStart,
        getLessonTypesStart,
        getLessonsByGroupStart,
        selectLessonCardSuccess,
        setOpenConfirmDialog,
        isOpenConfirmDialog,
        setOpenErrorSnackbar,
        setUniqueError,
        copyLessonsFromSemester,
        handleLesson,
        selectByGroupId,
        selectGroupSuccess,
        getEnabledGroupsStart,
        showAllTeachers,
        getAllSemesters,
    } = props;
    const { t } = useTranslation('common');
    const [term, setTerm] = useState('');
    const [lessonId, setLessonId] = useState();
    const [copiedLesson, setCopiedLesson] = useState();
    const [isOpenCopyLessonDialog, setIsOpenCopyLessonDialog] = useState(false);

    const visibleItems = search(lessons, term, [
        'teacher.surname',
        'subjectForSite',
        'lessonType',
        GROUPED,
    ]);

    useEffect(() => {
        if (groupId) {
            getLessonsByGroupStart(groupId);
        }
    }, [groupId]);

    useEffect(() => {
        showAllTeachers();
        getLessonTypesStart();
        getEnabledGroupsStart();
        showAllSubjectsService();
        getAllSemesters();
    }, []);

    const submitLessonForm = (card) => {
        const link = trasformLink(card.linkToMeeting);
        const values = cardObjectHandler(card, currentSemester, link);

        if (!checkUniqLesson(lessons, values)) {
            const message = t(COMMON_LESSON_SERVICE_IS_NOT_UNIQUE);

            setOpenErrorSnackbar(message);
            setUniqueError(true);
            return;
        }
        handleLesson({ values, groupId });
    };

    const showConfirmDialog = (lessonCardId) => {
        setLessonId(lessonCardId);
        setOpenConfirmDialog(true);
    };

    const acceptConfirmDialog = () => {
        setOpenConfirmDialog(false);
        deleteLessonCardStart(lessonId);
    };

    const openCopyLessonDialogHandle = (lesson) => {
        setCopiedLesson(lesson);
        setIsOpenCopyLessonDialog(true);
    };

    const closeCopyLessonDialogHandle = (params) => {
        const { group: copiedGroup, lesson } = params;
        setIsOpenCopyLessonDialog(false);
        if (!isNil(copiedGroup)) {
            copyLessonCardStart({ group: copiedGroup, lesson });
        }
    };

    const submitCopySemester = (values) => {
        const toSemesterId = currentSemester.id;
        const fromSemesterId = +values.fromSemesterId;
        copyLessonsFromSemester({ ...values, toSemesterId, fromSemesterId });
    };

    const handleGroupSelect = (selectedGroup) => {
        if (selectedGroup) {
            selectByGroupId(selectedGroup.id);
            selectGroupSuccess(selectedGroup.id);
        }
    };

    return (
        <>
            <div className="lesson-wrapper">
                <div className="lesson-side-bar">
                    <Search setTerm={setTerm} />
                    <LessonForm
                        lessonTypes={lessonTypes}
                        isUniqueError={isUniqueError}
                        subjects={subjects}
                        teachers={teachers}
                        onSubmit={submitLessonForm}
                        onSetSelectedCard={selectLessonCardSuccess}
                    />
                    {!groupId && <CopyLessonsFromSemesterForm onSubmit={submitCopySemester} />}
                </div>
                <div className="lessons-list">
                    <Autocomplete
                        id="group"
                        value={group}
                        options={groups}
                        className="group-lesson"
                        clearOnEscape
                        openOnFocus
                        getOptionLabel={(option) => option.title}
                        onChange={(_, newValue) => {
                            handleGroupSelect(newValue);
                        }}
                        renderInput={(params) => (
                            <TextField
                                className="textField"
                                {...params}
                                label={t(FORM_GROUP_LABEL)}
                                margin="normal"
                            />
                        )}
                    />
                    <Lessons
                        visibleItems={visibleItems}
                        onClickOpen={showConfirmDialog}
                        onCopyLesson={openCopyLessonDialogHandle}
                    />
                </div>
            </div>

            {isOpenCopyLessonDialog && (
                <CopyLessonDialog
                    open={isOpenCopyLessonDialog}
                    onClose={closeCopyLessonDialogHandle}
                    groupId={groupId}
                    lesson={copiedLesson}
                    groups={groups}
                    translation={t}
                />
            )}
            {isOpenConfirmDialog && (
                <CustomDialog
                    type={dialogTypes.DELETE_CONFIRM}
                    handelConfirm={acceptConfirmDialog}
                    whatDelete={cardType.LESSON.toLowerCase()}
                    open={isOpenConfirmDialog}
                />
            )}
        </>
    );
};

export default LessonPage;
