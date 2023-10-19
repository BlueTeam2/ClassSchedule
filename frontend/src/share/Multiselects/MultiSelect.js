import React, { useRef } from 'react';
import ReactSelect from 'react-select';
import './MultiSelect.scss';
import { useTranslation } from 'react-i18next';
import {
    SCHEDULE_FOR_SEMESTER,
    CHOOSE_TEACHER,
    ALL_TEACHERS,
} from '../../constants/translationLabels/common';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogCancelButton, dialogSendSchedule } from '../../constants/dialogs';

const MultiSelect = (props) => {
    const { t } = useTranslation('common');
    const valueRef = useRef(props.value);
    valueRef.current = props.value;
    const { open, defaultSemester, semesters, onCancel, onSentTeachers, isEnabledSentBtn } = props;

    const selectAllOption = {
        value: '<SELECT_ALL>',
        label: t(ALL_TEACHERS),
    };

    const isSelectAllSelected = () => valueRef.current.length === props.options.length;

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
    const handleChange = (newValue) => {
        props.onChangeSemesterValue(newValue);
    };

    return (
        <CustomDialog
            className="select-dialog"
            title={t(SCHEDULE_FOR_SEMESTER)}
            open={open}
            onClose={onCancel}
            buttons={[
                dialogSendSchedule(onSentTeachers, !isEnabledSentBtn),
                dialogCancelButton(onCancel),
            ]}
        >
            <div className="teacher-semester">
                <ReactSelect
                    classNamePrefix="react-select"
                    defaultValue={defaultSemester}
                    options={semesters}
                    onChange={handleChange}
                />

                <ReactSelect
                    classNamePrefix="react-select"
                    isOptionSelected={isOptionSelected}
                    options={getOptions()}
                    value={getValue()}
                    onChange={onChange}
                    hideSelectedOptions={false}
                    closeMenuOnSelect={false}
                    isMulti
                    placeholder={t(CHOOSE_TEACHER)}
                />
            </div>
        </CustomDialog>
    );
};

export default MultiSelect;
