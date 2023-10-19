import { CircularProgress } from '@material-ui/core';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { isEmpty } from 'lodash';
import { LESSON_NO_LESSON_FOR_GROUP_LABEL } from '../../constants/translationLabels/common';

import LessonsList from './LessonsList/LessonsList';
import './LessonPage.scss';

const Lessons = (props) => {
    const { t } = useTranslation('common');
    const {
        visibleItems,
        onClickOpen,
        onCopyLesson,
        groupId,
        group,
        loading,
        selectLessonCardSuccess,
    } = props;

    if (loading) {
        return (
            <section className="centered-container">
                <CircularProgress />
            </section>
        );
    }

    if (isEmpty(visibleItems) && groupId) {
        return (
            <h2 className="centered-container">
                {t(LESSON_NO_LESSON_FOR_GROUP_LABEL) + group.title}
            </h2>
        );
    }

    return (
        <LessonsList
            lessons={visibleItems}
            onClickOpen={onClickOpen}
            onSelectLesson={selectLessonCardSuccess}
            onCopyLesson={onCopyLesson}
        />
    );
};

export default Lessons;
