import { isEmpty } from 'lodash';

const hoverLineClassName = 'hover-line';

export const divideLessonsByOneHourLesson = (items, lessons) => {
    const lessonItem = [];
    lessons.forEach((lesson) => {
        let filteredLesson = [];
        const modifiedLesson = lesson;
        const { hours } = modifiedLesson;

        if (!isEmpty(items)) {
            filteredLesson = items.filter((item) => item.lesson.id === lesson.id);
        }

        for (let i = 0; i < hours - filteredLesson.length; i += 1) {
            lessonItem.push(modifiedLesson);
        }
    });
    return lessonItem;
};

export const addClassDayBoard = (dayName, classId) => {
    const dayClassWeek = document.querySelectorAll(`.${dayName}-${classId}`);
    const dayClass = document.querySelector(`#${dayName}-${classId}`);
    dayClass.classList.add('focus-class');
    dayClassWeek[0].classList.add(hoverLineClassName);
    dayClassWeek[1].classList.add(hoverLineClassName);
};

export const removeClassDayBoard = (dayName, classId) => {
    const dayClassWeek = document.querySelectorAll(`.${dayName}-${classId}`);
    const dayClass = document.querySelector(`#${dayName}-${classId}`);
    dayClass.classList.remove('focus-class');
    dayClassWeek[0].classList.remove(hoverLineClassName);
    dayClassWeek[1].classList.remove(hoverLineClassName);
};

export const getColorByFullness = (array = []) => {
    let color = isEmpty(array) ? 'available' : 'allow';
    let prevLesson = {
        teacherName: array[0]?.teacher_for_site,
        lessonName: array[0]?.subject_for_site,
    };
    array.forEach((lesson) => {
        const isTeacherNameTheSame = lesson.teacher_for_site === prevLesson.teacherName;
        const isLessonNotTheSame = lesson.subject_for_site !== prevLesson.lessonName;
        if (isLessonNotTheSame && isTeacherNameTheSame) {
            color = 'possible';
        }
        if (!isTeacherNameTheSame) {
            color = 'not-allow';
        }
        prevLesson = {
            teacherName: lesson.teacher_for_site,
            lessonName: lesson.subject_for_site,
        };
    });
    return color;
};
