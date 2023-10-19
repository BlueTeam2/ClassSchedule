import React from 'react';
import { MdPictureAsPdf } from 'react-icons/md';
import i18n from '../../i18n';
import {
    PUBLIC_DOWNLOAD_GROUP_SCHEDULE_URL,
    PUBLIC_DOWNLOAD_TEACHER_SCHEDULE_URL,
} from '../../constants/axios';
import { COMMON_DOWNLOAD_PDF } from '../../constants/translationLabels/common';

const getDownloadLink = (entityId, semesterId, languageToRequest) => ({
    group: `${PUBLIC_DOWNLOAD_GROUP_SCHEDULE_URL}?groupId=${entityId}&semesterId=${semesterId}${languageToRequest}`,
    teacher: `${PUBLIC_DOWNLOAD_TEACHER_SCHEDULE_URL}?teacherId=${entityId}&semesterId=${semesterId}${languageToRequest}`,
});

const DownloadLink = ({ entity, semesterId, entityId }) => {
    if (!semesterId || !entityId) {
        return null;
    }

    const { language } = i18n;
    const languageToRequest = `&language=${language}`;
    const downloadLink = getDownloadLink(entityId, semesterId, languageToRequest)[entity];

    return (
        <a
            href={downloadLink || ''}
            target="_blank"
            rel="noreferrer noopener"
            variant="contained"
            color="primary"
            className="pdf_link"
            download
        >
            <MdPictureAsPdf className="svg-btn" />
            {i18n.t(COMMON_DOWNLOAD_PDF)}
        </a>
    );
};

export default DownloadLink;
