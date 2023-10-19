import { isEmpty } from 'lodash';

const mapFullSchedule = (schedule, semesterDays, semesterClasses) => {
    const parsedGroupList = schedule.map(({ group }) => group);
    const parsedResultArray = [];

    semesterDays.forEach((day) => {
        const tempClassesArray = [];
        semesterClasses.forEach((classItem) => {
            const tepmOddCardsArray = [];
            const tempEvenCardsArray = [];
            parsedGroupList.forEach((groupItem) => {
                const groupFull = schedule.find(
                    (groupFullIterate) => groupFullIterate.group.id === groupItem.id,
                );
                const dayFull = groupFull.days.find((dayFullIterate) => dayFullIterate.day === day);
                const classFull = dayFull.classes.find(
                    (dayFullIterable) => dayFullIterable.class.id === classItem.id,
                );
                tepmOddCardsArray.push({
                    group: groupItem,
                    card: classFull.weeks.odd,
                });
                tempEvenCardsArray.push({
                    group: groupItem,
                    card: classFull.weeks.even,
                });
            });
            tempClassesArray.push({
                class: classItem,
                cards: { odd: tepmOddCardsArray, even: tempEvenCardsArray },
            });
        });
        parsedResultArray.push({ day, classes: tempClassesArray });
    });

    return { parsedGroupList, parsedResultArray };
};

export const makeFullSchedule = (fullSchedule) => {
    const { schedule, semester } = fullSchedule;
    const groupsCount = schedule?.length || 0;
    const semesterDays = semester?.semester_days || [];
    const semesterClasses = semester?.semester_classes || [];
    let groupList = [];
    let resultArray = [];

    if (!isEmpty(schedule)) {
        const { parsedGroupList, parsedResultArray } = mapFullSchedule(
            schedule,
            semesterDays,
            semesterClasses,
        );
        groupList = parsedGroupList;
        resultArray = parsedResultArray;
    }

    return {
        semester,
        schedule,
        semesterClasses,
        semesterDays,
        groupsCount,
        groupList,
        resultArray,
    };
};
