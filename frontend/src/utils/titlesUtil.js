import { get } from 'lodash';
import { getTeacherWithPosition } from '../helper/renderTeacher';

const getSemesterTitle = (semester) => {
    if (!semester) return '';
    const { description, startDay, endDay } = semester;

    return `${description} (${startDay}-${endDay}) : `;
};

const getGroupScheduleTitle = (semester, group) => {
    const semesterTitle = getSemesterTitle(semester);

    return group ? semesterTitle + get(group, 'title', '') : semesterTitle;
};

const getTeacherScheduleTitle = (semester, teacher) => {
    const semesterTitle = getSemesterTitle(semester);

    return teacher ? semesterTitle + getTeacherWithPosition(teacher) : semesterTitle;
};

export { getSemesterTitle, getGroupScheduleTitle, getTeacherScheduleTitle };
