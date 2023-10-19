export const trasformLink = (url) => {
    if (url) return !/^https?:\/\//i.test(url) ? `http://${url}` : url;
    return url;
};
