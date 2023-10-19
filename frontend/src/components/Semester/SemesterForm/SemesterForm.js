import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Field } from 'redux-form';
import Button from '@material-ui/core/Button';
import { isEmpty } from 'lodash';
import './SemesterForm.scss';
import renderCheckboxField from '../../../share/renderedFields/checkbox';
import renderTextField from '../../../share/renderedFields/input';
import renderMonthPicker from '../../../share/renderedFields/timeSemester';
import MultiselectForGroups from '../../../share/Multiselects/MultiSelectForGroups';
import {
    required,
    minYearValue,
    lessThanDate,
    greaterThanDate,
} from '../../../validation/validateFields';
import {
    getClearOrCancelTitle,
    setDisableButton,
    setDisabledSaveButtonSemester,
} from '../../../helper/disableComponent';
import Card from '../../../share/Card/Card';
import {
    COMMON_EDIT,
    COMMON_CREATE,
    COMMON_SEMESTER,
    COMMON_CURRENT_LABEL,
    COMMON_DEFAULT_LABEL,
    COMMON_YEAR_LABEL,
    COMMON_CHOOSE_GROUPS_BUTTON_LABEL,
    COMMON_SEMESTER_LABEL,
    COMMON_CLASS_FROM_LABEL,
    COMMON_CLASS_TO_LABEL,
    COMMON_DAYS_LABEL,
    COMMON_CLASS_SCHEDULE_MANAGEMENT_TITLE,
    COMMON_SAVE_BUTTON_LABEL,
} from '../../../constants/translationLabels/common';
import SetSemesterCheckboxes from './SemesterCheckboxes';
import {
    initialCheckboxesStateForDays,
    initialCheckboxesStateForClasses,
} from '../../../utils/formUtils';
import { getGroupsOptionsForSelect } from '../../../utils/selectUtils';
import { SEMESTER_FORM } from '../../../constants/reduxForms';

const SemesterForm = (props) => {
    const { t } = useTranslation('formElements');
    const {
        handleSubmit,
        pristine,
        submitting,
        semester,
        classScheduler,
        initialize,
        reset,
        selectedGroups,
        setSelectedGroups,
        options,
        clearSemesterSuccess,
    } = props;

    const [checkedClasses, setCheckedClasses] = useState({});
    const [checkedDates, setCheckedDates] = useState(initialCheckboxesStateForDays);
    const [current, setCurrent] = useState(false);
    const [byDefault, setByDefault] = useState(false);
    const [openGroupDialog, setOpenGroupDialog] = useState(false);

    const clearCheckboxes = () => {
        setCurrent(false);
        setByDefault(false);
        setCheckedDates(initialCheckboxesStateForDays);
    };

    useEffect(() => {
        const prepSetCheckedClasses = initialCheckboxesStateForClasses(classScheduler);
        setCheckedClasses({ ...prepSetCheckedClasses });
        const semesterItem = { ...semester };
        clearCheckboxes();
        if (semester.id) {
            const newDays = {};
            const newClasses = {};
            semesterItem.semester_days.forEach((item) => {
                semesterItem[`semester_days_markup_${item}`] = true;
                newDays[item] = true;
            });

            setCheckedDates({
                ...initialCheckboxesStateForDays,
                ...newDays,
            });

            semesterItem.semester_classes.forEach((item) => {
                semesterItem[`semester_classes_markup_${item.id}`] = true;
                newClasses[item.id] = true;
            });

            setCurrent(semester.currentSemester);
            setByDefault(semester.defaultSemester);

            setCheckedClasses({
                ...prepSetCheckedClasses,
                ...newClasses,
            });
        }
        initialize(semesterItem);
    }, [semester, classScheduler]);

    useEffect(() => {
        if (!isEmpty(semester.semester_groups)) {
            setSelectedGroups(getGroupsOptionsForSelect(semester.semester_groups));
        }
    }, [semester.id]);

    const openDialogForGroup = () => {
        setOpenGroupDialog(true);
    };

    const closeDialogForGroup = () => {
        setOpenGroupDialog(false);
    };

    const resetSemesterForm = () => {
        setSelectedGroups([]);
        clearSemesterSuccess();
        reset(SEMESTER_FORM);
    };

    return (
        <Card additionClassName="semester-form-card">
            <h2 className="card-title">
                {semester.id ? t(COMMON_EDIT) : t(COMMON_CREATE)}
                {` ${t(COMMON_SEMESTER)}`}
            </h2>
            <MultiselectForGroups
                open={openGroupDialog}
                options={options}
                value={selectedGroups}
                onChange={setSelectedGroups}
                onCancel={closeDialogForGroup}
                onClose={closeDialogForGroup}
            />
            <form onSubmit={handleSubmit}>
                <div className="semester-checkboxes">
                    <div>
                        <Field
                            name="currentSemester"
                            label={t(COMMON_CURRENT_LABEL)}
                            labelPlacement="start"
                            component={renderCheckboxField}
                            checked={current}
                            onChange={(event) => setCurrent(event.target.checked)}
                            color="primary"
                        />
                        <Field
                            name="defaultSemester"
                            label={t(COMMON_DEFAULT_LABEL)}
                            labelPlacement="start"
                            component={renderCheckboxField}
                            checked={byDefault}
                            onChange={(event) => setByDefault(event.target.checked)}
                            color="primary"
                        />
                    </div>
                    <Button
                        variant="contained"
                        color="primary"
                        className="buttons-style"
                        onClick={openDialogForGroup}
                    >
                        {t(COMMON_CHOOSE_GROUPS_BUTTON_LABEL)}
                    </Button>
                </div>
                <div className="semester-inputs-block">
                    <Field
                        className="semester-field-input"
                        name="year"
                        type="number"
                        component={renderTextField}
                        label={`${t(COMMON_YEAR_LABEL)}:`}
                        validate={[required, minYearValue]}
                    />
                    <Field
                        className="semester-field-input"
                        name="description"
                        component={renderTextField}
                        label={`${t(COMMON_SEMESTER_LABEL)}:`}
                        validate={[required]}
                    />
                </div>
                <div className="semester-inputs-block">
                    <Field
                        className="semester-field-input"
                        name="startDay"
                        component={renderMonthPicker}
                        label={`${t(COMMON_CLASS_FROM_LABEL)}:`}
                        validate={[required, lessThanDate]}
                    />
                    <Field
                        className="semester-field-input"
                        name="endDay"
                        component={renderMonthPicker}
                        label={`${t(COMMON_CLASS_TO_LABEL)}:`}
                        validate={[required, greaterThanDate]}
                    />
                </div>
                <p>{`${t(COMMON_DAYS_LABEL)}: `}</p>
                <div className="semester-checkboxes-container">
                    <SetSemesterCheckboxes
                        checked={checkedDates}
                        method={setCheckedDates}
                        name="semester_days_markup_"
                        classScheduler={classScheduler}
                    />
                </div>
                <p>{`${t(COMMON_CLASS_SCHEDULE_MANAGEMENT_TITLE)}: `}</p>
                <div className="semester-checkboxes-container">
                    <SetSemesterCheckboxes
                        checked={checkedClasses}
                        method={setCheckedClasses}
                        name="semester_classes_markup_"
                        classScheduler={classScheduler}
                    />
                </div>
                <div className="form-buttons-container">
                    <Button
                        variant="contained"
                        color="primary"
                        className="buttons-style"
                        disabled={setDisabledSaveButtonSemester(
                            pristine,
                            submitting,
                            semester,
                            selectedGroups,
                        )}
                        type="submit"
                    >
                        {t(COMMON_SAVE_BUTTON_LABEL)}
                    </Button>
                    <Button
                        type="button"
                        variant="contained"
                        className="buttons-style"
                        disabled={
                            setDisableButton(pristine, submitting, semester.id) &&
                            selectedGroups.length === 0
                        }
                        onClick={resetSemesterForm}
                    >
                        {getClearOrCancelTitle(semester.id, t)}
                    </Button>
                </div>
            </form>
        </Card>
    );
};

export default SemesterForm;
