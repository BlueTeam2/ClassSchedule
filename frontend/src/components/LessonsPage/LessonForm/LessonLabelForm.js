import React from 'react';
import { useTranslation } from 'react-i18next';
import {
    EDIT_TITLE,
    CREATE_TITLE,
    LESSON_LABEL,
} from '../../../constants/translationLabels/formElements';
import './LessonForm.scss';

const LessonLabelForm = (props) => {
    const { t } = useTranslation('formElements');

    const { lesson, groupId } = props;

    return (
        <section>
            {groupId && (
                <h2 className="under-line">
                    {lesson.id ? t(EDIT_TITLE) : t(CREATE_TITLE)}
                    {t(LESSON_LABEL)}
                </h2>
            )}
        </section>
    );
};

export default LessonLabelForm;
