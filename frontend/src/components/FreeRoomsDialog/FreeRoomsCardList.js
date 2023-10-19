import React from 'react';
import { isEmpty } from 'lodash';
import { FaDoorOpen } from 'react-icons/fa';
import { TYPE_LABEL, COMMON_FREE_ROOMS_LIST_EMPTY } from '../../constants/translationLabels/common';
import Card from '../../share/Card/Card';

const FreeRoomsCardList = (props) => {
    const { freeRooms, t } = props;
    return isEmpty(freeRooms) ? (
        <div className="room-icon-container">
            <FaDoorOpen className="room-icon" />
            <p className="description-text">{t(COMMON_FREE_ROOMS_LIST_EMPTY)}</p>
        </div>
    ) : (
        freeRooms.map((freeRoom) => (
            <Card key={freeRoom.id} additionClassName="free-room-card">
                <h4 className="room-card-name">{freeRoom.name}</h4>
                <span>
                    {`${t(TYPE_LABEL)}: `}
                    <p className="room-card-type">{freeRoom.type.description}</p>
                </span>
            </Card>
        ))
    );
};

export default FreeRoomsCardList;
