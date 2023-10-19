import { connect } from 'react-redux';
import { setPlace } from '../../actions/schedule';
import SelectPlace from '../../components/GroupSchedulePage/SelectPlace';

const mapStateToProps = (state) => ({
    place: state.schedule.place,
});

const mapDispatchToProps = (dispatch) => ({
    changePlace: (place) => dispatch(setPlace(place)),
});

export default connect(mapStateToProps, mapDispatchToProps)(SelectPlace);
