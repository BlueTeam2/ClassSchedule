import { connect } from 'react-redux';
import CustomDialog from '../../share/DialogWindows/CustomDialog';
import { setIsOpenConfirmDialog } from '../../actions/dialog';

const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
});

export default connect(null, mapDispatchToProps)(CustomDialog);
