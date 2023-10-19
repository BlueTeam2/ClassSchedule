import * as moment from 'moment';
import {
    checkUniqClassName,
    checkUniqueGroup,
    checkUniqueRoomName,
    checkUniqueSubject,
    timeIntersectService,
    checkUniqSemester,
    checkUniqueDepartment,
} from './storeValidation';
import i18n from '../i18n';
import { validation } from '../constants/validation';
import {
    EMAIL_MESSAGE,
    LESS_THAN_FIELD_MESSAGE,
    BIGGER_THAN_FIELD_MESSAGE,
    REQUIRED_MESSAGE,
    BIGGER_THAN_ZERO_MESSAGE,
    BIGGER_THAN_THIS_YEAR_MESSAGE,
    BIGGER_THAN_CHAR_MESSAGE,
    LESS_THAN_CHAR_MESSAGE,
    PASSWORD_MESSAGE,
    URL_MESSAGE,
} from '../constants/translationLabels/validationMessages';
import {
    FORM_CLASS_FROM_LABEL,
    FORM_CLASS_TO_LABEL,
} from '../constants/translationLabels/formElements';
import { dateFormat } from '../constants/formats';

export const required = (value) => (value ? undefined : i18n.t(REQUIRED_MESSAGE));

export const isUrl = (value) => {
    const validationUrl = value?.match(
        /(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_+.~#?&//=]*)/g,
    );
    if (value && validationUrl == null) return i18n.t(URL_MESSAGE);
    return '';
};

export const lessThanZero = (value) => (value > 0 ? undefined : i18n.t(BIGGER_THAN_ZERO_MESSAGE));

const minLength = (min) => (value) =>
    value && value.length < min ? i18n.t(BIGGER_THAN_CHAR_MESSAGE, { min }) : undefined;

export const minLengthValue = minLength(3);

const maxLength = (max) => (value) =>
    value && value.length > max
        ? i18n.t(LESS_THAN_CHAR_MESSAGE, {
              max,
          })
        : undefined;

export const maxLengthValue = maxLength(200);

export const lessThanTime = (value, previousValue, allValues) => {
    const otherField = 'endTime';
    if (allValues.values[otherField] === undefined) return undefined;
    return moment(value, 'HH:mm').toDate() <=
        moment(allValues.values[otherField], 'HH:mm').toDate() &&
        allValues.values[otherField] !== undefined
        ? undefined
        : i18n.t(LESS_THAN_FIELD_MESSAGE, {
              field: i18n.t(FORM_CLASS_TO_LABEL),
          });
};

export const greaterThanTime = (value, previousValue, allValues) => {
    const otherField = 'startTime';
    if (allValues.values[otherField] === undefined) return undefined;
    return moment(value, 'HH:mm').toDate() >= moment(allValues.values[otherField], 'HH:mm').toDate()
        ? undefined
        : i18n.t(BIGGER_THAN_FIELD_MESSAGE, {
              field: i18n.t(FORM_CLASS_FROM_LABEL),
          });
};

export const uniqueClassName = (value) => {
    return checkUniqClassName(value);
};

export const uniqueGroup = (value) => {
    return checkUniqueGroup(value);
};

export const uniqueSubject = (value) => {
    return checkUniqueSubject(value);
};

export const uniqueDepartment = (value) => {
    return checkUniqueDepartment(value);
};

export const timeIntersect = (value, previousValue, allValues) => {
    return timeIntersectService(allValues.values.startTime, allValues.values.endTime);
};

export const uniqueRoomName = (value) => {
    return checkUniqueRoomName(value);
};

export const email = (value) =>
    value && !validation.EMAIL.test(value) ? i18n.t(EMAIL_MESSAGE) : undefined;

export const password = (value) =>
    value && !validation.PASSWORD.test(value) ? i18n.t(PASSWORD_MESSAGE) : undefined;

const minYear = (min) => (value) =>
    value < min
        ? i18n.t(BIGGER_THAN_THIS_YEAR_MESSAGE, {
              min,
          })
        : undefined;
const today = new Date();
const year = today.getFullYear();

export const minYearValue = minYear(year);

export const lessThanDate = (value, previousValue, allValues) => {
    const otherField = 'endDay';
    if (allValues.values[otherField] === undefined) return undefined;
    return moment(value, dateFormat).toDate() <=
        moment(allValues.values[otherField], dateFormat).toDate() &&
        allValues.values[otherField] !== undefined
        ? undefined
        : i18n.t(LESS_THAN_FIELD_MESSAGE, {
              field: i18n.t(FORM_CLASS_TO_LABEL),
          });
};

export const greaterThanDate = (value, previousValue, allValues) => {
    const otherField = 'startDay';
    if (allValues.values[otherField] === undefined) return undefined;
    return moment(value, dateFormat).toDate() >=
        moment(allValues.values[otherField], dateFormat).toDate()
        ? undefined
        : i18n.t(BIGGER_THAN_FIELD_MESSAGE, {
              field: i18n.t(FORM_CLASS_FROM_LABEL),
          });
};

export const uniquesSemesterName = (value) => checkUniqSemester(value);
