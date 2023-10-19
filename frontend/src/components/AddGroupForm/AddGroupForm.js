import Button from '@material-ui/core/Button';
import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import {
    CREATE_TITLE,
    EDIT_TITLE,
    FORM_GROUP_LABEL_AFTER,
    GROUP_LABEL,
    GROUP_Y_LABEL,
    SAVE_BUTTON_LABEL,
} from '../../constants/translationLabels/formElements';
import { getClearOrCancelTitle, setDisableButton } from '../../helper/disableComponent';
import { renderAutocompleteField } from '../../helper/renderAutocompleteField';
import renderTextField from '../../share/renderedFields/input';
import { minLengthValue, required, uniqueGroup } from '../../validation/validateFields';
import './AddGroupForms.scss';

export function AddGroup(props){
    const {
        clearGroupStart,
        submitGroupStart,
        handleSubmit,
        initialize,
        submitting,
        setGroup,
        pristine,
        invalid,
        groups,
        group,
    } = props;
    const { t } = useTranslation('formElements');

    const removeCurrentGroup = () => groups.filter((el) => el.id !== group.id);
    const groupsForAutocomplete = group.id ? removeCurrentGroup() : groups;
    useEffect(() => {
        const groupIndex = groups.findIndex(({ id }) => id === group.id);
        const afterId = groups.find((item, index) => index === groupIndex - 1);
        if (group.id) {
            initialize({
                id: group.id,
                title: group.title,
                afterId,
            });
        } else {
            initialize();
        }
    }, [group.id]);

    const submitGroup = (data) => {
        const afterId = data.afterId ? data.afterId.id : null;
        submitGroupStart({ ...data, disable: false, afterId });
        setGroup({});
    };

    const onReset = () => {
        setGroup({});
        clearGroupStart();
    };

    return (
        <div className="group-form">
            <h3 className="group-form-title">
                {group.id ? t(EDIT_TITLE) : t(CREATE_TITLE)}
                {t(GROUP_Y_LABEL)}
            </h3>
            <form onSubmit={handleSubmit((data) => submitGroup(data))}>
                <Field
                    className="form-field"
                    name="title"
                    id="title"
                    label={`${t(GROUP_LABEL)}:`}
                    component={renderTextField}
                    validate={[required, uniqueGroup, minLengthValue]}
                />
                <Field
                    className="select-field"
                    name="afterId"
                    component={renderAutocompleteField}
                    label={t(FORM_GROUP_LABEL_AFTER)}
                    type="text"
                    values={groupsForAutocomplete}
                    getOptionLabel={(item) => (item ? item.title : '')}
                ></Field>
                <div className="form-buttons-container">
                    <Button
                        size="small"
                        variant="contained"
                        className="buttons-style "
                        color="primary"
                        disabled={invalid || pristine || submitting}
                        type="submit"
                    >
                        {t(SAVE_BUTTON_LABEL)}
                    </Button>
                    <Button
                        size="small"
                        type="button"
                        className="buttons-style"
                        variant="contained"
                        disabled={setDisableButton(pristine, submitting, group.id)}
                        onClick={onReset}
                    >
                        {getClearOrCancelTitle(group.id, t)}
                    </Button>
                </div>
            </form>
        </div>
    );
};
