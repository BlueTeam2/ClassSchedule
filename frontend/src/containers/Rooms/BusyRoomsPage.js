import { connect } from 'react-redux';
import { getAllScheduleItemsStart } from '../../actions/schedule';
import BusyRoomPage from '../../components/BusyRoomsPage/BusyRoomsPage';
import { getBusyRoomsStart } from '../../actions/rooms';
import { getClassScheduleListStart } from '../../actions/classes';
import { setScheduleLoading } from '../../actions';

const mapStateToProps = (state) => ({
    busyRooms: state.rooms.rooms,
    semesterId: state.schedule.currentSemester.id,
    scheduleLoading: state.loadingIndicator.scheduleLoading,
    currentSemester: state.schedule.currentSemester,
});

const mapDispatchToProps = (dispatch) => ({
    getAllScheduleItems: () => dispatch(getAllScheduleItemsStart()),
    setScheduleLoading: (newState) => dispatch(setScheduleLoading(newState)),
    getBusyRooms: (semesterId) => dispatch(getBusyRoomsStart(semesterId)),
    getClassScheduleList: () => dispatch(getClassScheduleListStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(BusyRoomPage);
