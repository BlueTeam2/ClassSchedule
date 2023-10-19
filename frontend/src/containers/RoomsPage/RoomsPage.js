import { connect } from 'react-redux';
import {
    handleRoomFormSubmitStart,
    getListOfRoomsStart,
    getListOfDisabledRoomsStart,
    toggleRoomVisibilityStart,
    deleteRoomStart,
    setSelectRoomSuccess,
    clearRoomSuccess,
    getAllRoomTypesStart,
    deleteRoomTypeStart,
    handleRoomTypeFormSubmitStart,
    selectRoomType,
} from '../../actions/rooms';
import { setIsOpenConfirmDialog } from '../../actions/dialog';
import RoomPage from '../../components/RoomsPage/RoomsPage';

const mapStateToProps = (state) => ({
    rooms: state.rooms.rooms,
    disabledRooms: state.rooms.disabledRooms,
    oneRoom: state.rooms.oneRoom,
    roomTypes: state.rooms.roomTypes,
    oneType: state.rooms.oneType,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
    loading: state.loadingIndicator.loading,
});
const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    handleRoomFormSubmit: (values) => dispatch(handleRoomFormSubmitStart(values)),
    getListOfRooms: () => dispatch(getListOfRoomsStart()),
    getListOfDisabledRooms: () => dispatch(getListOfDisabledRoomsStart()),
    getAllRoomTypes: () => dispatch(getAllRoomTypesStart()),
    toggleRoomVisibility: (roomId, isDisabled) =>
        dispatch(toggleRoomVisibilityStart(roomId, isDisabled)),
    deleteRoom: (roomId, isDisabled) => dispatch(deleteRoomStart(roomId, isDisabled)),
    deleteRoomType: (roomTypeId) => dispatch(deleteRoomTypeStart(roomTypeId)),
    handleRoomTypeFormSubmit: (values) => dispatch(handleRoomTypeFormSubmitStart(values)),
    setSelectRoom: (roomId) => dispatch(setSelectRoomSuccess(roomId)),
    clearRoomItem: () => dispatch(clearRoomSuccess()),
    setSelectRoomType: (typeId) => dispatch(selectRoomType(typeId)),
});

export default connect(mapStateToProps, mapDispatchToProps)(RoomPage);
