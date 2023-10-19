import { connect } from 'react-redux';

import { getLessonsByGroupStart, setScheduleLoading, showAllGroupsSuccess } from '../../actions';
import { getClassScheduleListStart } from '../../actions/classes';
import { clearScheduleStart, getAllScheduleItemsStart } from '../../actions/schedule';
import { getEnabledGroupsStart } from '../../actions/groups';
import EditCurrentSchedulePage from '../../components/EditCurrentSchedule/EditCurrentSchedulePage';
import { getListOfRoomsStart } from '../../actions/rooms';

const mapStateToProps = (state) => ({
    groups: state.groups.groups,
    lessons: state.lesson.lessons,
    groupId: state.lesson.groupId,
    scheduleLoading: state.loadingIndicator.scheduleLoading,
    scheduleItems: state.schedule.items,
    itemGroupId: state.schedule.itemGroupId,
    currentSemester: state.schedule.currentSemester,
    semester: state.schedule.semester,
});

const mapDispatchToProps = (dispatch) => ({
    getEnabledGroups: () => dispatch(getEnabledGroupsStart()),
    getAllLessonsByGroup: (groupId) => dispatch(getLessonsByGroupStart(groupId)),
    getAllScheduleItems: () => dispatch(getAllScheduleItemsStart()),
    clearScheduleItems: (id) => dispatch(clearScheduleStart(id)),
    setScheduleLoading: (newState) => dispatch(setScheduleLoading(newState)),
    showAllGroups: () => dispatch(showAllGroupsSuccess()),
    getClassScheduleList: () => dispatch(getClassScheduleListStart()),
    getListOfRooms: () => dispatch(getListOfRoomsStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(EditCurrentSchedulePage);
