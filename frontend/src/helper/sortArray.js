export const sortByName = (array) => {
    array.sort((item, secondItem) => item.name.localeCompare(secondItem.name));
    return array;
};
