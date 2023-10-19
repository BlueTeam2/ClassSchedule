import { isEmpty } from 'lodash';
import i18n from '../i18n';
import { COMMON_AVAILABLE, COMMON_UNAVAILABLE } from '../constants/translationLabels/common';
import { getFirstLetter } from '../helper/renderTeacher';

export const getGroupsOptionsForSelect = (groupOptions) => {
    if (!isEmpty(groupOptions)) {
        return groupOptions.map((item) => {
            return { id: item.id, value: item.id, label: `${item.title}` };
        });
    }
    return groupOptions;
};

export const getOptionLabelWithAvailable = (option) => {
    return `${option.name} (${
        option.available ? i18n.t(COMMON_AVAILABLE) : i18n.t(COMMON_UNAVAILABLE)
    })`;
};

export const setDepartmentOptions = (dep) => {
    return dep.map((item) => {
        return { id: item.id, value: item.id, label: `${item.name}` };
    });
};

export const setSemesterOptions = (semesters) => {
    return semesters !== undefined
        ? semesters.map((item) => {
              return { id: item.id, value: item.id, label: `${item.description}` };
          })
        : null;
};

export const setOptions = (enabledTeachers) => {
    return enabledTeachers.map((item) => {
        return {
            id: item.id,
            value: item.id,
            label: `${item.surname} ${getFirstLetter(item.name)} ${getFirstLetter(
                item.patronymic,
            )}`,
        };
    });
};
