import { getColorByFullness, divideLessonsByOneHourLesson } from './schedule';

const teacherAndSubjectAreTheSame = [
    {
        subject_for_site: 'Web-дизайн',
        teacher_for_site: "Куб'як",
    },
    {
        subject_for_site: 'Web-дизайн',
        teacher_for_site: "Куб'як",
    },
];

const teacherTheSame = [
    {
        subject_for_site: 'Системи штучного інтелекту',
        teacher_for_site: 'Мельник',
    },
    {
        lesson_type: 'LECTURE',
        teacher_for_site: 'Мельник',
    },
];

const teacherIsNotTheSame = [
    {
        subject_for_site: 'Системи штучного інтелекту',
        teacher_for_site: 'Мельник',
    },
    {
        subject_for_site: 'Інтелектуальні інформаційні системи',
        teacher_for_site: "Куб'як",
    },
];
const lessons = [
    {
        id: 767,
        hours: 2,
    },
    {
        id: 784,
        hours: 3,
    },
];

const items = [
    {
        lesson: {
            id: 767,
            hours: 2,
        },
    },
    {
        lesson: {
            id: 772,
            hours: 2,
        },
    },
];

describe('behavior of divideLessonsByOneHourLesson function', () => {
    test('should return lessons with one-hour duration each which length equal amount of all hours of lessons', () => {
        expect(divideLessonsByOneHourLesson([], lessons).length).toBe(5);
    });
    test('should return lessons with one-hour duration each which length equal amount of all hours of lessons minus amount of items which exist in lessons', () => {
        expect(divideLessonsByOneHourLesson(items, lessons).length).toBe(4);
    });
});

describe('behavior of getColorByFullness function', () => {
    test('should add css class "available" if  array of groups is empty', () => {
        expect(getColorByFullness([])).toBe('available');
    });
    test('should add css class "possible" if  lesson is not the same and teacher is the same', () => {
        expect(getColorByFullness(teacherTheSame)).toBe('possible');
    });
    test('should add css class "not-allow" if teacher is not the same', () => {
        expect(getColorByFullness(teacherIsNotTheSame)).toBe('not-allow');
    });
    test('should add css class "allow" if lesson and teacher are the same', () => {
        expect(getColorByFullness(teacherAndSubjectAreTheSame)).toBe('allow');
    });
});
