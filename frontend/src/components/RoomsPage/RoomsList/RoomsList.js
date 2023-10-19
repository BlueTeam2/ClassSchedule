import React from 'react';
import { useTranslation } from 'react-i18next';
import { CircularProgress } from '@material-ui/core';
import { isEmpty } from 'lodash';
import NotFound from '../../../share/NotFound/NotFound';
import { ROOM_Y_LABEL } from '../../../constants/translationLabels/formElements';
import RoomCard from './RoomCard/RoomCard';
import { search } from '../../../helper/search';

const RoomList = (props) => {
    const { isDisabled, disabledRooms, term, rooms, loading, ...rest } = props;
    const { t } = useTranslation('formElements');

    const visibleItems = isDisabled
        ? search(disabledRooms, term, ['name'])
        : search(rooms, term, ['name']);

    if (loading) {
        return (
            <section className="centered-container">
                <CircularProgress />
            </section>
        );
    }
    if (isEmpty(visibleItems)) {
        return <NotFound name={t(ROOM_Y_LABEL)} />;
    }
    return (
        <section className="container-flex-wrap">
            {visibleItems.map((room) => (
                <RoomCard key={room.id} {...rest} room={room} isDisabled={isDisabled} />
            ))}
        </section>
    );
};

export default RoomList;
