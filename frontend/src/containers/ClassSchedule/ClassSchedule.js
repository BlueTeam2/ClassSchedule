import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';

import { FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';

import { useTranslation } from 'react-i18next';
import ClassForm from '../../components/ClassForm/ClassForm';
import Card from '../../share/Card/Card';
import CustomDialog from '../Dialogs/CustomDialog';
import { dialogTypes } from '../../constants/dialogs';
import { cardType } from '../../constants/cardType';

import {
    classFormHandler,
    getClassScheduleListStart,
    getClassScheduleByIdStart,
    deleteClassScheduleStart,
    clearClassScheduleSuccess,
} from '../../actions/classes';
import { setIsOpenConfirmDialog } from '../../actions/dialog';

import { handleSnackbarOpenService } from '../../services/snackbarService';
import { snackbarTypes } from '../../constants/snackbarTypes';
import {
    CLASS_LABEL,
    CLASS_FROM_LABEL,
    CLASS_TO_LABEL,
    MAX_COUNT_CLASSES_REACHED,
} from '../../constants/translationLabels/formElements';
import {
    COMMON_EDIT_HOVER_TITLE,
    COMMON_DELETE_HOVER_TITLE,
} from '../../constants/translationLabels/common';

const ClassSchedule = (props) => {
    const { t } = useTranslation('formElements');
    const {
        isOpenConfirmDialog,
        setOpenConfirmDialog,
        formHandler,
        getClassScheduleList,
        deleteClassSchedule,
        getClassScheduleById,
        clearClassSchedule,
    } = props;
    const [classId, setClassId] = useState(-1);
    useEffect(() => {
        getClassScheduleList();
    }, []);

    const submit = (values) => {
        if (!values.id && props.classScheduler.length >= 7)
            return handleSnackbarOpenService(
                true,
                snackbarTypes.ERROR,
                t(MAX_COUNT_CLASSES_REACHED),
            );
        return formHandler(values);
    };

    const handleEdit = (id) => {
        getClassScheduleById(id);
    };

    const handleClickOpen = (id) => {
        setClassId(id);
        setOpenConfirmDialog(true);
    };

    const handleDelete = (id) => {
        setOpenConfirmDialog(false);
        deleteClassSchedule(id);
    };

    return (
        <div className="cards-container">
            <CustomDialog
                type={dialogTypes.DELETE_CONFIRM}
                handelConfirm={() => handleDelete(classId)}
                whatDelete={cardType.CLASS.toLowerCase()}
                open={isOpenConfirmDialog}
            />

            <ClassForm onSubmit={submit} onReset={clearClassSchedule} />
            <section className="container-flex-wrap">
                {props.classScheduler.map((schedule) => (
                    <Card additionClassName="class-card-width" key={schedule.id}>
                        <div className="cards-btns">
                            <FaEdit
                                className="svg-btn"
                                title={t(COMMON_EDIT_HOVER_TITLE)}
                                onClick={() => handleEdit(schedule.id)}
                            />
                            <MdDelete
                                className="svg-btn"
                                title={t(COMMON_DELETE_HOVER_TITLE)}
                                onClick={() => handleClickOpen(schedule.id)}
                            />
                        </div>
                        <p>
                            {t(CLASS_LABEL)}: {schedule.class_name}
                        </p>
                        <p>
                            {t(CLASS_FROM_LABEL)} - {t(CLASS_TO_LABEL)}
                        </p>
                        <p>
                            {schedule.startTime} - {schedule.endTime}
                        </p>
                    </Card>
                ))}
            </section>
        </div>
    );
};

const mapStateToProps = (state) => ({
    classScheduler: state.classActions.classScheduler,
    isOpenConfirmDialog: state.dialog.isOpenConfirmDialog,
});

const mapDispatchToProps = (dispatch) => ({
    setOpenConfirmDialog: (newState) => dispatch(setIsOpenConfirmDialog(newState)),
    formHandler: (values) => dispatch(classFormHandler(values)),
    getClassScheduleList: () => dispatch(getClassScheduleListStart()),
    deleteClassSchedule: (id) => dispatch(deleteClassScheduleStart(id)),
    getClassScheduleById: (id) => dispatch(getClassScheduleByIdStart(id)),
    clearClassSchedule: () => dispatch(clearClassScheduleSuccess()),
});

export default connect(mapStateToProps, mapDispatchToProps)(ClassSchedule);
