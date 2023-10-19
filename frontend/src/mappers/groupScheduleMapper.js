import { cloneDeep, isEmpty } from 'lodash';
import { daysObject } from '../constants/schedule/days';

const transformClassDays = (classDay) => {
    const lessons = [];
    Object.entries(classDay.cards).forEach((pair) => {
        const [key, value] = pair;
        value.day = key;
        lessons.push(value);
    });
    return { class: classDay.class, lessons };
};

const transformParityArray = (array) => array.map(transformClassDays);

const mapGroupSchedule = (schedule) => {
    const prepOddArray = [];
    const prepEvenArray = [];
    const { days, group } = schedule[0];

    days.forEach(({ classes }) => {
        classes.forEach((classItem) => {
            if (classItem.weeks.even) {
                prepEvenArray[classItem.class.id] = {
                    class: classItem.class,
                    cards: cloneDeep(daysObject),
                };
            }
            if (classItem.weeks.odd) {
                prepOddArray[classItem.class.id] = {
                    class: classItem.class,
                    cards: cloneDeep(daysObject),
                };
            }
        });
    });

    days.forEach(({ classes, day }) => {
        classes.forEach((classItem) => {
            if (classItem.weeks.even) {
                prepEvenArray[classItem.class.id].cards[day].card = classItem.weeks.even;
            }
            if (classItem.weeks.odd) {
                prepOddArray[classItem.class.id].cards[day].card = classItem.weeks.odd;
            }
        });
    });

    const parsedOddArray = transformParityArray(prepOddArray);

    const parsedEvenArray = transformParityArray(prepEvenArray);

    return { parsedOddArray, parsedEvenArray, parsedGroup: group };
};

export const makeGroupSchedule = (groupSchedule) => {
    const { semester, schedule } = groupSchedule;
    let evenArray = [];
    let oddArray = [];
    let group = {};

    if (!isEmpty(schedule)) {
        const { parsedOddArray, parsedEvenArray, parsedGroup } = mapGroupSchedule(schedule);
        evenArray = parsedEvenArray;
        oddArray = parsedOddArray;
        group = parsedGroup;
    }
    return {
        semester,
        oddArray,
        evenArray,
        group,
    };
};
