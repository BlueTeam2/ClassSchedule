import { connect } from 'react-redux';
import '../../components/LessonsPage/LessonPage.scss';
import LessonPage from '../../components/LessonsPage/LessonPage';
import {
    copyLessonCardStart,
    deleteLessonCardStart,
    getLessonsByGroupStart,
    getLessonTypesStart,
    selectLessonCardSuccess,
    setUniqueError,
    setIsOpenConfirmDialog,
    selectGroupId,
    selectGroupSuccess,
    getEnabledGroupsStart,
} from '../../actions';
import { setOpenErrorSnackbar } from '../../actions/snackbar';
import { copyLessonsFromSemesterStart, getAllSemestersStart } from '../../actions/semesters';
import { handleLessonStart } from '../../actions/lesson';
import { showAllTeachersStart } from '../../actions/teachers';

const mapStateToProps = (state) => ({
    lessons: state.lesson.lessons,
    lessonTypes: state.lesson.lessonTypes,
    groupId: state.lesson.groupId,
    isUniqueError: state.lesson.uniqueError,
    teachers: state.teachers.teachers,
    groups: state.groups.groups,
    group: state.groups.group,
    subjects: state.subjects.subjects,
    loading: state.loadingIndicator.loading,
    semesters: state.semesters.semesters,
    currentSemester: state.schedule.currentSemester,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
});

const mapDispatchToProps = (dispatch) => ({
    getEnabledGroupsStart: () => dispatch(getEnabledGroupsStart()),
    getLessonsByGroupStart: (groupId) => dispatch(getLessonsByGroupStart(groupId)),
    getLessonTypesStart: () => dispatch(getLessonTypesStart()),
    deleteLessonCardStart: (lessonId) => dispatch(deleteLessonCardStart(lessonId)),
    copyLessonCardStart: (group, lesson) => dispatch(copyLessonCardStart(group, lesson)),
    selectLessonCardSuccess: (lessonCardId) => dispatch(selectLessonCardSuccess(lessonCardId)),
    setOpenErrorSnackbar: (message) => dispatch(setOpenErrorSnackbar(message)),
    setUniqueError: (value) => dispatch(setUniqueError(value)),
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    copyLessonsFromSemester: (values) => dispatch(copyLessonsFromSemesterStart(values)),
    handleLesson: (values, groupId) => dispatch(handleLessonStart(values, groupId)),
    selectByGroupId: (groupId) => dispatch(selectGroupId(groupId)),
    selectGroupSuccess: (id) => dispatch(selectGroupSuccess(id)),
    showAllTeachers: () => dispatch(showAllTeachersStart()),
    getAllSemesters: () => dispatch(getAllSemestersStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(LessonPage);
