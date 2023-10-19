import React from 'react';
import { Field } from 'redux-form';
import { useTranslation } from 'react-i18next';
import { createClasslabel } from '../../../utils/formUtils';
import renderCheckboxField from '../../../share/renderedFields/checkbox';

const SetSemesterCheckboxes = (props) => {
    const { checked, method, name, classScheduler } = props;
    const { t } = useTranslation('formElements');
    const setCheckedHandler = (event, item, prevChecked, setSchecked) => {
        const changedItem = { [item]: event.target.checked };
        setSchecked({
            ...prevChecked,
            ...changedItem,
        });
    };
    const checkboxes = Object.keys(checked);
    return checkboxes.map((item) => {
        return (
            <Field
                key={item}
                name={`${name}${item}`}
                label={
                    name.indexOf('semester_days_markup_') >= 0
                        ? t(`common:day_of_week_${item}`)
                        : createClasslabel(classScheduler, item)
                }
                labelPlacement="end"
                component={renderCheckboxField}
                defaultValue={checked[item]}
                checked={checked[item]}
                onChange={(e) => {
                    setCheckedHandler(e, item, checked, method);
                }}
                color="primary"
            />
        );
    });
};

export default SetSemesterCheckboxes;
