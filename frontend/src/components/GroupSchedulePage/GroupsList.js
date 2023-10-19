import { isEmpty } from 'lodash';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import { FORM_GROUP_LABEL } from '../../constants/translationLabels/formElements';
import { renderAutocompleteField } from '../../helper/renderAutocompleteField';

const GroupsList = (props) => {
    const { handleChange, groups } = props;
    const { t } = useTranslation('common');

    return (
        <Field
            disabled={isEmpty(groups)}
            name="group"
            component={(values) => renderAutocompleteField(values)}
            label={t(FORM_GROUP_LABEL)}
            type="text"
            handleChange={() => {
                handleChange('teacher', null);
            }}
            values={groups}
            getOptionLabel={(group) => (group ? group.title : '')}
            className="schedule-form_group"
        ></Field>
    );
};

export default GroupsList;
