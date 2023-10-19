import { connect } from 'react-redux';
import {
    checkAvailabilityScheduleStart,
    checkAvailabilityChangeRoomScheduleStart,
    deleteScheduleItemStart,
} from '../../actions/schedule';
import { selectGroupId, getLessonsByGroupStart } from '../../actions';
import ScheduleBoard from '../../components/EditCurrentSchedule/ScheduleBoard/ScheduleBoard';

const mapStateToProps = (state) => ({
    scheduleItems: state.schedule.items,
});
const mapDispatchToProps = (dispatch) => ({
    checkScheduleItemAvailability: (item) => dispatch(checkAvailabilityScheduleStart(item)),
    selectByGroupId: (groupId) => dispatch(selectGroupId(groupId)),
    checkRoomAvailability: (item) => dispatch(checkAvailabilityChangeRoomScheduleStart(item)),
    deleteScheduleItem: (item) => dispatch(deleteScheduleItemStart(item)),
    getLessonsByGroupId: (id) => dispatch(getLessonsByGroupStart(id)),
});

export default connect(mapStateToProps, mapDispatchToProps)(ScheduleBoard);
