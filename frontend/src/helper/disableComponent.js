import { isNil, isEmpty } from 'lodash';
import {
    CLEAR_BUTTON_LABEL,
    CANCEL_BUTTON_TITLE,
} from '../constants/translationLabels/formElements';

export const setDisableButton = (pristine, submitting, id) => {
    if (!pristine || !isNil(id)) {
        return false;
    }
    return true;
};
export const getClearOrCancelTitle = (id, t) => {
    return id === undefined ? t(CLEAR_BUTTON_LABEL) : t(CANCEL_BUTTON_TITLE);
};

export const setDisabledSaveButtonSemester = (pristine, submitting, semester, selectedGroups) => {
    if (!isEmpty(semester) && semester.id) {
        const beginGroups = semester.semester_groups.map((item) => item.id);
        const restGroups = selectedGroups.map((item) => item.id);
        const newGroups = restGroups.filter((group) => !beginGroups.includes(group));
        const deleteGroups = beginGroups.filter((group) => !restGroups.includes(group));
        const isChosenGroup =
            isEmpty(semester.semester_groups) || !isEmpty(newGroups) || !isEmpty(deleteGroups);
        return !isChosenGroup && (pristine || submitting);
    }
    return pristine || submitting;
};
