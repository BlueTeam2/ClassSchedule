import React from 'react';
import LinkToMeeting from '../components/LinkToMeeting/LinkToMeeting';
import { places } from '../constants/places';
import { getHref } from './getHref';

export const setLink = (card, place) => {
    if (place === places.TOGETHER) {
        return <LinkToMeeting {...card} />;
    }
    if (place === places.ONLINE) {
        return getHref(card.linkToMeeting);
    }
    return null;
};
