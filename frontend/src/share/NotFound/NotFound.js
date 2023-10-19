import React from 'react';
import { FaSadCry } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import { THERE_IS_NO } from '../../constants/translationLabels/formElements';

import './NotFound.scss';

const NotFound = ({ name }) => {
    const { t } = useTranslation('formElements');

    return (
        <div className="not-found__wrapper">
            <FaSadCry className="not-found__icon" />
            <p className="not-found__text">
                {t(THERE_IS_NO)} {name}
            </p>
        </div>
    );
};

export default NotFound;
