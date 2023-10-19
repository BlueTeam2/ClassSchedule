import { daysUppercase } from '../constants/schedule/days';
import i18n from '../i18n';
import { SEMESTER_SERVICE_NOT_AS_BEGIN_OR_END } from '../constants/translationLabels/serviceMessages';

export const initialCheckboxesStateForDays = daysUppercase.reduce((init, item) => {
    const isCheckedDays = init;
    isCheckedDays[item] = false;
    return isCheckedDays;
}, {});

export const initialCheckboxesStateForClasses = (classScheduler) => {
    return classScheduler.reduce((init, classItem) => {
        const isCheckedClass = init;
        isCheckedClass[`${classItem.id}`] = false;
        return isCheckedClass;
    }, {});
};

export const createClasslabel = (lessons, classItem) => {
    const item = lessons.find((lesson) => lesson.id === +classItem);
    return `${item.class_name} (${item.startTime}-${item.endTime})`;
};

export const checkSemesterYears = (endDay, startDay, year) => {
    const dateEndYear = endDay.substring(endDay.length - 4);
    const dateStartYear = startDay.substring(startDay.length - 4);
    let conf = true;
    if (year !== +dateEndYear || year !== +dateStartYear) {
        conf = window.confirm(i18n.t(SEMESTER_SERVICE_NOT_AS_BEGIN_OR_END));
    }
    return conf;
};
