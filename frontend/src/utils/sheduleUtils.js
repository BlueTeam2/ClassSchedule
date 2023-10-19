import { isEmpty } from 'lodash';

const getScheduleByType = (entityId, semesterId) => ({});

const isNotReadySchedule = (schedule, loading) => isEmpty(schedule) && !loading;

const filterClassesArray = (inputArray) => {
    return inputArray.filter((item, index, array) => {
        const resIndex = array.findIndex((findItem) => findItem.id === item.id);
        return resIndex === index;
    });
};

export { getScheduleByType, isNotReadySchedule, filterClassesArray };
