import Moment from 'moment';
import { extendMoment } from 'moment-range';
import { store } from '../store';
import i18n from '../i18n';
import {
    UNIQUE_ERROR_MESSAGE,
    INTERSECT_TIME_ERROR_MESSAGE,
} from '../constants/translationLabels/validationMessages';

export const checkUniqClassName = (className) => {
    const classId = store.getState().classActions.classSchedule.id;
    let find = false;
    if (classId) {
        find = store.getState().classActions.classScheduler.some((value) => {
            return value.class_name === className && value.id !== classId;
        });
    } else {
        find = store.getState().classActions.classScheduler.some((value) => {
            return value.class_name === className;
        });
    }
    return find ? i18n.t(UNIQUE_ERROR_MESSAGE) : undefined;
};

export const timeIntersectService = (startTime, endTime) => {
    const classId = store.getState().classActions.classSchedule.id;
    const moment = extendMoment(Moment);
    let find = false;
    if (startTime && endTime) {
        const incomeRange = moment.range(
            moment(startTime, 'HH:mm').toDate(),
            moment(endTime, 'HH:mm').toDate(),
        );
        if (classId) {
            find = store.getState().classActions.classScheduler.some((value) => {
                return (
                    incomeRange.intersect(
                        moment.range(
                            moment(value.startTime, 'HH:mm').toDate(),
                            moment(value.endTime, 'HH:mm').toDate(),
                        ),
                    ) !== null && value.id !== classId
                );
            });
        } else {
            find = store.getState().classActions.classScheduler.some((value) => {
                return (
                    incomeRange.intersect(
                        moment.range(
                            moment(value.startTime, 'HH:mm').toDate(),
                            moment(value.endTime, 'HH:mm').toDate(),
                        ),
                    ) !== null
                );
            });
        }
    }
    return find ? i18n.t(INTERSECT_TIME_ERROR_MESSAGE) : undefined;
};
export const checkUniqLesson = (lessons, currentLesson) => {
    if (!currentLesson?.id) {
        return !lessons.find(
            (lesson) =>
                lesson.subject.id === +currentLesson.subject.id &&
                lesson.teacher.id === +currentLesson.teacher.id &&
                lesson.lessonType === currentLesson.lessonType,
        );
    }
    return !lessons.find(
        (lesson) =>
            lesson.subject.id === +currentLesson.subject.id &&
            lesson.teacher.id === +currentLesson.teacher.id &&
            lesson.lessonType === currentLesson.lessonType &&
            lesson.id !== +currentLesson.id,
    );
};

export const checkUniqueRoomName = (roomName) => {
    const roomdId = store.getState().rooms.oneRoom.id;
    let find = false;
    if (roomdId) {
        find = store.getState().rooms.rooms.some((value) => {
            return value.name.toUpperCase() === roomName.toUpperCase() && value.id !== roomdId;
        });
    } else {
        find = store.getState().rooms.rooms.some((value) => {
            return value.name.toUpperCase() === roomName.toUpperCase();
        });
    }
    return find ? i18n.t(UNIQUE_ERROR_MESSAGE) : undefined;
};

export const checkUniqueGroup = (groupTitle) => {
    if (!groupTitle) {
        return undefined;
    }
    const find = store.getState().groups.groups.some((value) => {
        return value.title.toUpperCase().trim() === groupTitle.toUpperCase().trim();
    });
    return find ? i18n.t(UNIQUE_ERROR_MESSAGE) : undefined;
};

export const checkUniqueSubject = (subjectTitle) => {
    if (!subjectTitle) {
        return undefined;
    }
    const find = store.getState().subjects.subjects.some((value) => {
        return value.name.toUpperCase().trim() === subjectTitle.toUpperCase().trim();
    });
    return find ? i18n.t(UNIQUE_ERROR_MESSAGE) : undefined;
};
export const checkUniqueDepartment = (departmentTitle) => {
    if (!departmentTitle) {
        return undefined;
    }
    const find = store.getState().departments.departments.some((value) => {
        return value.name.toUpperCase().trim() === departmentTitle.toUpperCase().trim();
    });
    return find ? i18n.t(UNIQUE_ERROR_MESSAGE) : undefined;
};

export const checkUniqSemester = (semester) => {
    const { semesters } = store.getState().semesters;
    let isNotUnique;
    if (!semester.id) {
        isNotUnique = !!semesters.find(
            (storeSemester) =>
                storeSemester.year === +semester.year &&
                storeSemester.description.toUpperCase().trim() ===
                    semester.description.toUpperCase().trim(),
        );
    } else {
        isNotUnique = !!semesters.find(
            (storeSemester) =>
                storeSemester.year === +semester.year &&
                storeSemester.description.toUpperCase().trim() ===
                    semester.description.toUpperCase().trim() &&
                storeSemester.id !== +semester.id,
        );
    }

    return !isNotUnique;
};
