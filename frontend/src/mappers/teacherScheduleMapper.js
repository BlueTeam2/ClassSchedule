import { isEmpty } from 'lodash';
import { filterClassesArray } from '../utils/sheduleUtils';
import { sortStrings } from '../utils/sortStrings';

const mapTeacherSchedule = (days) => {
    const parsedEvenArray = [];
    const parsedOddArray = [];
    const parsedOddDays = [];
    const parsedEvenDays = [];
    const parsedOddClasses = [];
    const parsedEvenClasses = [];

    days.forEach(({ day, even, odd }) => {
        odd.classes.forEach((classItem) => {
            if (parsedOddClasses.findIndex((oddClass) => oddClass.id === classItem.class.id) < 0) {
                parsedOddClasses.push(classItem.class);
            }
            if (parsedOddDays.indexOf(day) < 0) {
                parsedOddDays.push(day);
            }
            if (!(classItem.class.id in parsedOddArray)) {
                parsedOddArray[classItem.class.id] = [];
            }

            parsedOddArray[classItem.class.id].push({
                day,
                cards: classItem.lessons,
            });
        });

        even.classes.forEach((classItem) => {
            if (
                parsedEvenClasses.findIndex((evenClass) => evenClass.id === classItem.class.id) < 0
            ) {
                parsedEvenClasses.push(classItem.class);
            }
            if (parsedEvenDays.indexOf(day) < 0) {
                parsedEvenDays.push(day);
            }
            if (!(classItem.class.id in parsedEvenArray)) {
                parsedEvenArray[classItem.class.id] = [];
            }

            parsedEvenArray[classItem.class.id].push({
                day,
                cards: classItem.lessons,
            });
        });
    });

    const parsedOdd = {
        days: parsedOddDays,
        classes: filterClassesArray(parsedOddClasses).sort((a, b) =>
            sortStrings(a.startTime, b.startTime),
        ),
        cards: parsedOddArray,
    };
    const parsedEven = {
        days: parsedEvenDays,
        classes: filterClassesArray(parsedEvenClasses).sort((a, b) =>
            sortStrings(a.startTime, b.startTime),
        ),
        cards: parsedEvenArray,
    };
    return { parsedOdd, parsedEven };
};

export const makeTeacherSchedule = (teacherSchedule) => {
    const { semester, days, teacher } = teacherSchedule;
    let odd = {};
    let even = {};

    if (!isEmpty(days)) {
        const { parsedEven, parsedOdd } = mapTeacherSchedule(days);
        odd = parsedOdd;
        even = parsedEven;
    }

    return {
        teacher,
        semester,
        odd,
        even,
    };
};
