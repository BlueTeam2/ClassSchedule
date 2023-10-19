import { connect } from 'react-redux';
import { selectTeacherCard, setIsOpenConfirmDialog } from '../../actions';
import {
    getAllPublicSemestersStart,
    getCurrentSemesterRequsted,
    getDefaultSemesterRequsted,
    sendTeacherScheduleStart,
} from '../../actions/schedule';
import { getPublicClassScheduleStart } from '../../actions/classes';
import { getAllSemestersStart } from '../../actions/semesters';
import {
    deleteTeacherStart,
    handleTeacherStart,
    setDisabledTeachersStart,
    showAllTeachersStart,
    toggleTeacherStart,
} from '../../actions/teachers';
import TeachersPage from '../../components/TeachersPage/TeachersPage';

const mapStateToProps = (state) => ({
    enabledTeachers: state.teachers.teachers,
    disabledTeachers: state.teachers.disabledTeachers,
    defaultSemester: state.schedule.defaultSemester,
    semesters: state.schedule.semesters,
    departments: state.departments.departments,
    department: state.departments.department,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
    teacher: state.teachers.teacher,
});
const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    getAllSemestersItems: () => dispatch(getAllSemestersStart()),
    getCurrentSemester: () => dispatch(getCurrentSemesterRequsted()),
    getDefaultSemester: () => dispatch(getDefaultSemesterRequsted()),
    getAllPublicSemesters: () => dispatch(getAllPublicSemestersStart()),
    sendTeacherSchedule: (data) => dispatch(sendTeacherScheduleStart(data)),
    selectedTeacherCard: (teacherCardId) => dispatch(selectTeacherCard(teacherCardId)),
    deleteTeacher: (id) => dispatch(deleteTeacherStart(id)),
    showAllTeachers: () => dispatch(showAllTeachersStart()),
    getDisabledTeachers: () => dispatch(setDisabledTeachersStart()),
    handleTeacher: (values) => dispatch(handleTeacherStart(values)),
    getPublicClassSchedule: () => dispatch(getPublicClassScheduleStart()),
    toggleDisabledTeacher: (teacherId, isDisabled) =>
        dispatch(toggleTeacherStart(teacherId, isDisabled)),
});

export default connect(mapStateToProps, mapDispatchToProps)(TeachersPage);
