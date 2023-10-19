import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import SchedulePageForm from '../../components/GroupSchedulePage/SchedulePageForm';
import { SCHEDULE_SEARCH_FORM } from '../../constants/reduxForms';

const mapStateToProps = (state) => ({
    semester: state.schedule.scheduleSemester,
    group: state.schedule.scheduleGroup,
    teacher: state.schedule.scheduleTeacher,
});

export default connect(mapStateToProps)(
    reduxForm({
        form: SCHEDULE_SEARCH_FORM,
    })(SchedulePageForm),
);
