import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { GROUP_FORM } from '../../constants/reduxForms';
import { AddGroup } from '../../components/AddGroupForm/AddGroupForm';
import { submitGroupStart, clearGroupStart } from '../../actions/groups';

const mapStateToProps = (state) => ({
    groups: state.groups.groups,
});

const mapDispatchToProps = {
    submitGroupStart,
    clearGroupStart,
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(
    reduxForm({
        form: GROUP_FORM,
    })(AddGroup),
);
