import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import CircularProgress from '@material-ui/core/CircularProgress';
import { FaDoorOpen } from 'react-icons/fa';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import FreeRoomForm from '../../containers/Rooms/FreeRoomsForm';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogCloseButton } from '../../constants/dialogs';
import './FreeRoomsDialog.scss';
import { FIND_FREE_ROOM } from '../../constants/translationLabels/formElements';
import FreeRoomsCardList from './FreeRoomsCardList';

const FreeRoomsDialog = (props) => {
    const { clearFreeRooms, freeRooms, isLoading, getClassScheduleList } = props;
    const { t } = useTranslation('formElements');
    const [isOpenFreeRoomDialog, setIsOpenFreeRoomDialog] = useState(false);

    useEffect(() => {
        getClassScheduleList();
    }, []);

    const handleIsOpenFreeRoomDialog = () => {
        setIsOpenFreeRoomDialog((prev) => !prev);
        clearFreeRooms();
    };

    return (
        <>
            <span className="navLinks" onClick={handleIsOpenFreeRoomDialog} aria-hidden="true">
                <ListItemIcon>
                    <FaDoorOpen fontSize="normal" />
                </ListItemIcon>
                {t(FIND_FREE_ROOM)}
            </span>
            {isOpenFreeRoomDialog && (
                <CustomDialog
                    open={isOpenFreeRoomDialog}
                    onClose={handleIsOpenFreeRoomDialog}
                    buttons={[dialogCloseButton(handleIsOpenFreeRoomDialog)]}
                    className="free-room-dialog"
                >
                    <div className="dialog-body-container">
                        <section className="free-rooms-container">
                            {isLoading ? (
                                <div className="loading-rooms">
                                    <CircularProgress size="70px" className="loading-circle" />
                                </div>
                            ) : (
                                <FreeRoomsCardList freeRooms={freeRooms} t={t} />
                            )}
                        </section>
                        <aside className="free-rooms-form-container">
                            <FreeRoomForm />
                        </aside>
                    </div>
                </CustomDialog>
            )}
        </>
    );
};

export default FreeRoomsDialog;
