import { navigationNames } from './navigation';
import {
    CLASS_SCHEDULE_TITLE,
    DEPARTMENTS_TITLE,
    GROUP_LIST_TITLE,
    ROOM_LIST_TITLE,
    SUBJECT_PAGE_TITLE,
    SEMESTER_PAGE_TITLE,
    TEACHER_LIST_TITLE,
    LESSON_PAGE_TITLE,
    MERGE_ROLE_PAGE_TITLE,
    CHANGES_TITLE,
    BUSY_ROOMS_TITLE,
} from './translationLabels/common';

export const general = [
    { name: navigationNames.CLASS_SCHEDULE_TITLE, title: CLASS_SCHEDULE_TITLE },
    { name: navigationNames.DEPARTMENTS, title: DEPARTMENTS_TITLE },
    { name: navigationNames.GROUP_LIST, title: GROUP_LIST_TITLE },
    { name: navigationNames.ROOM_LIST, title: ROOM_LIST_TITLE },
    { name: navigationNames.SUBJECT_PAGE, title: SUBJECT_PAGE_TITLE },
    { name: navigationNames.SEMESTER_PAGE, title: SEMESTER_PAGE_TITLE },
    { name: navigationNames.TEACHER_LIST, title: TEACHER_LIST_TITLE },
];
export const tabsComponents = [
    { name: navigationNames.LESSON_PAGE, title: LESSON_PAGE_TITLE },
    { name: navigationNames.MERGE_ROLE_PAGE, title: MERGE_ROLE_PAGE_TITLE },
    { name: navigationNames.CHANGES, title: CHANGES_TITLE },
    { name: navigationNames.BUSY_ROOMS, title: BUSY_ROOMS_TITLE },
];
