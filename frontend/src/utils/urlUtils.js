const getDataFromParams = (location) => {
    const params = new URLSearchParams(location.search);

    const semester = params.get('semester');
    const teacher = params.get('teacher');
    const group = params.get('group');

    return { semester, teacher, group };
};

export { getDataFromParams };
