export const getShortTitle = (title, MAX_LENGTH) => {
    return title.length > MAX_LENGTH ? `${title.slice(0, MAX_LENGTH)}...` : title;
};
