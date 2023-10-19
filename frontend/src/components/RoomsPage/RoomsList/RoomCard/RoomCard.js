import React from 'react';
import { FaEdit } from 'react-icons/fa';
import { MdDelete } from 'react-icons/md';
import { useTranslation } from 'react-i18next';
import { GiSightDisabled, IoMdEye } from 'react-icons/all';
import { dialogTypes } from '../../../../constants/dialogs';
import Card from '../../../../share/Card/Card';
import {
    COMMON_SET_DISABLED,
    COMMON_SET_ENABLED,
} from '../../../../constants/translationLabels/common';
import { cardType } from '../../../../constants/cardType';
import './RoomCard.scss';

const RoomCard = (props) => {
    const { room, isDisabled, showConfirmDialog, setSelectRoom } = props;
    const { t } = useTranslation('formElements');

    return (
        <Card additionClassName="room-card">
            <div className="cards-btns">
                {!isDisabled ? (
                    <>
                        <IoMdEye
                            className="eye-icon-btn"
                            title={t(COMMON_SET_DISABLED)}
                            onClick={() => {
                                showConfirmDialog(
                                    room.id,
                                    dialogTypes.SET_VISIBILITY_DISABLED,
                                    cardType.ROOM,
                                );
                            }}
                        />
                        <FaEdit className="edit-icon-btn" onClick={() => setSelectRoom(room.id)} />
                    </>
                ) : (
                    <GiSightDisabled
                        className="eye-icon-btn"
                        title={t(COMMON_SET_ENABLED)}
                        onClick={() => {
                            showConfirmDialog(
                                room.id,
                                dialogTypes.SET_VISIBILITY_ENABLED,
                                cardType.ROOM,
                            );
                        }}
                    />
                )}

                <MdDelete
                    className="delete-icon-btn"
                    onClick={() => {
                        showConfirmDialog(room.id, dialogTypes.DELETE_CONFIRM, cardType.ROOM);
                    }}
                />
            </div>
            <h2 className="room-card-name">{room.name}</h2>
            <p className="room-card-title">{room.type.description}</p>
        </Card>
    );
};

export default RoomCard;
