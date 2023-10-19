import React from 'react';
import { COMMON_LINK_TO_MEETING_WORD } from '../constants/translationLabels/common';
import i18n from '../i18n';

export const getHref = (link) => (
    <a title={link} className="link-to-meeting" href={link} target="_blank" rel="noreferrer">
        {i18n.t(COMMON_LINK_TO_MEETING_WORD)}
    </a>
);
