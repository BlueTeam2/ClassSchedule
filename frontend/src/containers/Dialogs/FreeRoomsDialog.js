import { connect } from 'react-redux';
import { getFreeRoomsStart, clearFreeRooms } from '../../actions/rooms';
import { getClassScheduleListStart } from '../../actions/classes';
import FreeRooms from '../../components/FreeRoomsDialog/FreeRoomsDialog';

const mapStateToProps = (state) => ({
    freeRooms: state.rooms.freeRooms,
    isLoading: state.loadingIndicator.roomsLoading,
});
const mapDispatchToProps = (dispatch) => ({
    getFreeRoomsByParams: (params) => dispatch(getFreeRoomsStart(params)),
    clearFreeRooms: () => dispatch(clearFreeRooms()),
    getClassScheduleList: () => dispatch(getClassScheduleListStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(FreeRooms);
