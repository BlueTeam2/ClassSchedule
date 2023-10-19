export const sortGroups = (groups, group, afterId) => {
    let newGroups = groups;
    if (afterId) {
        const afterGroupIndex = groups.findIndex(({ id }) => id === afterId);
        newGroups.splice(afterGroupIndex + 1, 0, group);
    } else {
        newGroups = [group, ...groups];
    }
    return newGroups;
};
