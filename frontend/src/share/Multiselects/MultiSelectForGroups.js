import React, { useRef } from 'react';
import Select from 'react-select';
import './MultiSelect.scss';
import { useTranslation } from 'react-i18next';
import { CHOOSE_GROUPS, ALL_GROUPS } from '../../constants/translationLabels/common';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogCancelButton, dialogConfirmButton } from '../../constants/dialogs';

const MultiselectForGroups = (props) => {
    const { t } = useTranslation('common');
    const valueRef = useRef(props.value);
    valueRef.current = props.value;
    const { open, onClose, onCancel } = props;

    const selectAllOption = {
        value: '<SELECT_ALL>',
        label: t(ALL_GROUPS),
    };

    const isSelectAllSelected = () => {
        if (props.options !== undefined && valueRef.current) {
            return valueRef.current.length === props.options.length;
        }
        return false;
    };
    const isOptionSelected = (option) =>
        valueRef.current.some(({ value }) => value === option.value) || isSelectAllSelected();

    const getOptions = () => [selectAllOption, ...props.options];

    const getValue = () => (isSelectAllSelected() ? [selectAllOption] : props.value);

    const onChange = (newValue, actionMeta) => {
        const { action, option, removedValue } = actionMeta;

        if (action === 'select-option' && option.value === selectAllOption.value) {
            props.onChange(props.options, actionMeta);
        } else if (
            (action === 'deselect-option' && option.value === selectAllOption.value) ||
            (action === 'remove-value' && removedValue.value === selectAllOption.value)
        ) {
            props.onChange([], actionMeta);
        } else if (actionMeta.action === 'deselect-option' && isSelectAllSelected()) {
            props.onChange(
                props.options.filter(({ value }) => value !== option.value),
                actionMeta,
            );
        } else {
            props.onChange(newValue || [], actionMeta);
        }
    };
    return (
        <CustomDialog
            className="select-dialog"
            open={open}
            onClose={onClose}
            buttons={[dialogConfirmButton(onClose), dialogCancelButton(onCancel)]}
        >
            <Select
                classNamePrefix="react-select"
                isOptionSelected={isOptionSelected}
                options={getOptions()}
                defaultValue={getValue()}
                onChange={onChange}
                hideSelectedOptions={false}
                closeMenuOnSelect={false}
                isMulti
                placeholder={t(CHOOSE_GROUPS)}
            />
        </CustomDialog>
    );
};

export default MultiselectForGroups;
