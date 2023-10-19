import { isEmpty } from 'lodash';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import { FORM_SEMESTER_LABEL } from '../../constants/translationLabels/formElements';
import { renderAutocompleteField } from '../../helper/renderAutocompleteField';

const SemestersList = (props) => {
    const { semesters, handleSubmit, getAllGroups } = props;
    const { t } = useTranslation('common');

    if (semesters && semesters.length > 1) {
        return (
            <Field
                name="semester"
                component={(values) => renderAutocompleteField(values)}
                label={t(FORM_SEMESTER_LABEL)}
                type="text"
                handleChange={(value) => {
                    if (!isEmpty(value)) getAllGroups(value.id);
                }}
                values={semesters}
                getOptionLabel={(semester) => (semester ? semester.description : '')}
                className="schedule-form_semester"
            ></Field>
        );
    }
    if (semesters && semesters.length === 1) {
        handleSubmit({ semester: semesters[0] });
        return <p className="schedule-form_semester">{semesters[0].description}</p>;
    }
    return null;
};

export default SemestersList;
