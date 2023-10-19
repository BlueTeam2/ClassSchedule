import React, { useState } from 'react';
import { FaEdit, MdDelete } from 'react-icons/all';
import { useTranslation } from 'react-i18next';
import { connect } from 'react-redux';

import Divider from '@material-ui/core/Divider';
import shortId from 'shortid';
import Card from '../../../share/Card/Card';
import CustomDialog from '../../../containers/Dialogs/CustomDialog';
import { dialogTypes } from '../../../constants/dialogs';

import {
    deleteTemporaryScheduleService,
    selectTemporaryScheduleService,
    selectVacationService,
} from '../../../services/temporaryScheduleService';
import { setIsOpenConfirmDialog } from '../../../actions/dialog';
import { cardType } from '../../../constants/cardType';
import TemporaryScheduleCard from '../TemporaryScheduleCard/TemporaryScheduleCard';
import { getTeacherForSite } from '../../../helper/renderTeacher';
import {
    EDIT_HOVER_TITLE,
    DELETE_HOVER_TITLE,
    HOLIDAY_LABEL,
    DATE_LABEL,
    FOR_ALL,
} from '../../../constants/translationLabels/common';

const TemporaryScheduleList = (props) => {
    const { t } = useTranslation('common');
    const { isOpenConfirmDialog, setOpenConfirmDialog } = props;
    const temporarySchedules = props.temporarySchedules || [];
    const [temporaryScheduleId, setTemporaryScheduleId] = useState(-1);

    const handleClickOpen = (id) => {
        setTemporaryScheduleId(id);
        setOpenConfirmDialog(true);
    };

    const handleDelete = (id) => {
        setOpenConfirmDialog(false);
        deleteTemporaryScheduleService(id, null, null);
    };

    return (
        <main className="container-flex-wrap">
            <CustomDialog
                handelConfirm={() => handleDelete(temporaryScheduleId)}
                type={dialogTypes.DELETE_CONFIRM}
                whatDelete={cardType.TEMPORARY_SCHEDULE}
                open={isOpenConfirmDialog}
            />

            {temporarySchedules.map((temporarySchedule) => (
                <Card
                    additionClassName={`done-card${
                        temporarySchedule.vacation ? ' vacation-card' : ''
                    }`}
                    key={shortId.generate()}
                >
                    <div className="cards-btns">
                        <FaEdit
                            title={t(EDIT_HOVER_TITLE)}
                            className="svg-btn edit-btn"
                            onClick={() =>
                                temporarySchedule.vacation
                                    ? selectVacationService(temporarySchedule)
                                    : selectTemporaryScheduleService(temporarySchedule)
                            }
                        />
                        <MdDelete
                            title={t(DELETE_HOVER_TITLE)}
                            className="svg-btn delete-btn"
                            onClick={() => {
                                handleClickOpen(temporarySchedule.id);
                            }}
                        />
                    </div>
                    {!temporarySchedule.vacation ? (
                        <>
                            <TemporaryScheduleCard schedule={temporarySchedule} />
                        </>
                    ) : (
                        <>
                            <h2>{t(HOLIDAY_LABEL)}</h2>
                            <p>
                                (
                                {temporarySchedule.teacher?.name
                                    ? getTeacherForSite(temporarySchedule.teacher)
                                    : t(FOR_ALL)}
                                )
                            </p>
                            <Divider />
                        </>
                    )}
                    <p>
                        {!temporarySchedule.vacation && <>{t(DATE_LABEL)} :</>}
                        <b>{temporarySchedule.date}</b>
                    </p>
                </Card>
            ))}
        </main>
    );
};
const mapStateToProps = (state) => ({
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
});
const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
});

export default connect(mapStateToProps, mapDispatchToProps)(TemporaryScheduleList);
