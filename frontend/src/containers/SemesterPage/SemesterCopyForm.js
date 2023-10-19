import { reduxForm } from 'redux-form';
import { SEMESTER_COPY_FORM } from '../../constants/reduxForms';
import SemesterCopyForm from '../../components/Semester/SemesterList/SemesterCopyForm/SemesterCopyForm';

export default reduxForm({
    form: SEMESTER_COPY_FORM,
})(SemesterCopyForm);
