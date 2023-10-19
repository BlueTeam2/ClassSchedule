import { isEmpty } from 'lodash';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { FORM_TEACHER_A_LABEL } from '../../../constants/translationLabels/formElements';
import NotFound from '../../../share/NotFound/NotFound';
import TeachersCard from './TeachersCard';
import './TeachersList.scss';

const TeachersList = (props) => {
    const { t } = useTranslation('common');

    const { visibleItems, ...rest } = props;

    if (isEmpty(visibleItems)) {
        return <NotFound name={t(FORM_TEACHER_A_LABEL)} />;
    }

    return (
        <div className="teachers-list-container">
            {visibleItems.map((teacherItem) => {
                return <TeachersCard teacherItem={teacherItem} key={teacherItem.id} {...rest} />;
            })}
        </div>
    );
};

export default TeachersList;
