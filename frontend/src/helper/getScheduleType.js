import { get } from 'lodash';
import { FULL, GROUP, TEACHER } from '../constants/scheduleTypes';

export const getScheduleType = (values) => {
    const { group, teacher } = values;
    if (get(group, 'id')) {
        return GROUP;
    }
    if (get(teacher, 'id')) {
        return TEACHER;
    }
    return FULL;
};
