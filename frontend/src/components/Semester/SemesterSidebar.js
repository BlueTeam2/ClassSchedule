import React, { useState } from 'react';
import SearchPanel from '../../share/SearchPanel/SearchPanel';
import SemesterForm from '../../containers/SemesterPage/SemesterForm';
import i18n from '../../i18n';
import { COMMON_SEMESTER_IS_NOT_UNIQUE } from '../../constants/translationLabels/common';
import { checkUniqSemester } from '../../validation/storeValidation';
import { checkSemesterYears } from '../../utils/formUtils';

const SemesterSidebar = (props) => {
    const {
        setTerm,
        archived,
        disabled,
        showDisabledHandle,
        setOpenErrorSnackbar,
        handleSemesterFormSubmit,
        semester,
        options,
        classScheduler,
    } = props;

    const [selectedGroups, setSelectedGroups] = useState([]);

    const submitSemesterForm = (values) => {
        const semesterDays = [];
        const semesterClasses = [];
        const semesterGroups = selectedGroups.map((group) => {
            return { id: group.id, title: group.label };
        });

        Object.keys(values).forEach((prop) => {
            if (prop.indexOf('semester_days_markup_') >= 0 && values[prop]) {
                semesterDays.push(prop.substring(21));
            }
            if (prop.indexOf('semester_classes_markup_') >= 0 && values[prop]) {
                semesterClasses.push(
                    classScheduler.find((schedule) => schedule.id === +prop.substring(24)),
                );
            }
        });
        const semesterItem = {
            ...values,
            semester_groups: semesterGroups,
            semester_classes: semesterClasses,
            semester_days: semesterDays,
        };
        if (!checkUniqSemester(semesterItem)) {
            const message = i18n.t(COMMON_SEMESTER_IS_NOT_UNIQUE);
            setOpenErrorSnackbar(message);
            return;
        }
        if (!checkSemesterYears(semesterItem.endDay, semesterItem.startDay, +semesterItem.year))
            return;
        handleSemesterFormSubmit(semesterItem);
        setSelectedGroups([]);
    };

    return (
        <aside className="semester-aside-panel">
            <SearchPanel
                SearchChange={setTerm}
                showDisabled={showDisabledHandle}
                // it doesnt work, need to finish implement archeved functionality
                // showArchived={showArchivedHandler}
            />
            {!(disabled || archived) && (
                <SemesterForm
                    selectedGroups={selectedGroups}
                    setSelectedGroups={setSelectedGroups}
                    className="form"
                    onSubmit={submitSemesterForm}
                    semester={semester}
                    options={options}
                    classScheduler={classScheduler}
                />
            )}
        </aside>
    );
};

export default SemesterSidebar;
