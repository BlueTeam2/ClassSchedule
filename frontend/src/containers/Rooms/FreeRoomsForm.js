import { reduxForm, change, untouch } from 'redux-form';
import { connect } from 'react-redux';

import { FREE_ROOMS } from '../../constants/reduxForms';

import { getAllSemestersStart } from '../../actions/semesters';
import { setRoomsLoading } from '../../actions/loadingIndicator';
import { getFreeRoomsStart } from '../../actions/rooms';
import FreeRoomForm from '../../components/FreeRoomsDialog/freeRoomForm';

const mapStateToProps = (state) => ({
    initialValues: { semesterId: state.schedule.currentSemester.id },
    freeRooms: state.rooms.freeRooms,
    semesters: state.semesters.semesters,
});
const mapDispatchToProps = (dispatch) => ({
    getAllSemestersItems: () => dispatch(getAllSemestersStart()),
    setRoomsLoading: (newState) => dispatch(setRoomsLoading(newState)),
    getFreeRoomsByParams: (params) => dispatch(getFreeRoomsStart(params)),
    clearField: (fieldName) => {
        dispatch(change(FREE_ROOMS, fieldName));
        dispatch(untouch(FREE_ROOMS, fieldName));
    },
});

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(
    reduxForm({
        form: FREE_ROOMS,
        enableReinitialize: true,
    })(FreeRoomForm),
);
