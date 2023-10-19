export const parseShortPosition = (position) => {
    const teacherPosition = new Map();
    teacherPosition.set('доцент', 'доц.');
    teacherPosition.set('асистент', 'ac.');
    teacherPosition.set('професор', 'проф.');
    return teacherPosition.get(position.toLowerCase());
};
export const getFirstLetter = (word) => {
    return word !== null ? `${word.charAt(0)}.` : '';
};
export const getTeacherName = (teacher) => {
    const { name, surname, patronymic } = teacher;
    return `${surname} ${getFirstLetter(name)} ${getFirstLetter(patronymic)}`;
};
export const getTeacherFullName = (teacher) => {
    const { name, surname, patronymic } = teacher;
    return `${surname} ${name} ${patronymic}`;
};
export const getTeacherForSite = (teacher) => {
    const { name, surname, patronymic, position } = teacher;
    return `${position} ${surname} ${getFirstLetter(name)} ${getFirstLetter(patronymic)}\n`;
};
export const handleTeacherInfo = (teacher) => {
    const { name, surname, patronymic, position } = teacher;
    return `${surname} ${name} ${patronymic} (${position})`;
};
export const getTeacherWithPosition = (teacher) => {
    const { name, surname, patronymic, position } = teacher;
    return `${position} ${surname} ${name} ${patronymic}`;
};

export const getTeacherWithShortPosition = (teacher) => {
    const { name, surname, patronymic, position } = teacher;
    return `${parseShortPosition(position)} ${surname} ${name} ${patronymic}`;
};
