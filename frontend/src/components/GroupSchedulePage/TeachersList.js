import React from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import { FORM_TEACHER_LABEL } from '../../constants/translationLabels/formElements';
import { renderAutocompleteField } from '../../helper/renderAutocompleteField';
import { getTeacherFullName } from '../../helper/renderTeacher';

const TeachersList = (props) => {
    const { teachers, handleChange } = props;
    const { t } = useTranslation('common');
    return (
        <Field
            name="teacher"
            component={(values) => renderAutocompleteField(values)}
            label={t(FORM_TEACHER_LABEL)}
            type="text"
            handleChange={() => {
                handleChange('group', null);
            }}
            values={teachers}
            getOptionLabel={(teacher) => (teacher ? getTeacherFullName(teacher) : '')}
            className="schedule-form_teacher"
        ></Field>
    );
};

export default TeachersList;
