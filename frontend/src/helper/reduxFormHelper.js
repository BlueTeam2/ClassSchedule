import { getTeacherForSite } from './renderTeacher';

export const setValueToTeacherForSiteHandler = (teachers, id, setValue) => {
    const teacher = teachers.find((teacherItem) => teacherItem.id === +id);
    if (!teacher) setValue('teacherForSite', '');
    else setValue('teacherForSite', getTeacherForSite(teacher));
};

export const setValueToSubjectForSiteHandler = (subjects, subjectId, setValue) => {
    const subject = subjects.find((subjectItem) => subjectItem.id === +subjectId);
    if (!subject) return setValue('subjectForSite', '');
    return setValue('subjectForSite', subject.name);
};
