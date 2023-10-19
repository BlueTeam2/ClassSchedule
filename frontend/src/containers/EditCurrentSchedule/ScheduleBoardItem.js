import { connect } from 'react-redux';
import {
    checkAvailabilityChangeRoomScheduleStart,
    deleteScheduleItemStart,
} from '../../actions/schedule';
import { selectGroupId, getLessonsByGroupStart } from '../../actions';
import ScheduleBoardItem from '../../components/EditCurrentSchedule/ScheduleBoard/ScheduleBoardItem';

const mapStateToProps = (state) => ({
    scheduleItems: state.schedule.items,
});
const mapDispatchToProps = (dispatch) => ({
    selectByGroupId: (groupId) => dispatch(selectGroupId(groupId)),
    checkRoomAvailability: (item) => dispatch(checkAvailabilityChangeRoomScheduleStart(item)),
    deleteScheduleItem: (item) => dispatch(deleteScheduleItemStart(item)),
    getLessonsByGroupId: (id) => dispatch(getLessonsByGroupStart(id)),
});

export default connect(mapStateToProps, mapDispatchToProps)(ScheduleBoardItem);
