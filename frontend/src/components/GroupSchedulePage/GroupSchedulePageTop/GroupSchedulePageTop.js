import React from 'react';

import { useTranslation } from 'react-i18next';

import './GroupSchedulePageTop.scss';
import Card from '../../../share/Card/Card';

import {
    GREETING_SCHEDULE_MESSAGE,
    GREETING_SCHEDULE_MESSAGE_HINT,
} from '../../../constants/translationLabels/common';
import SelectPlace from '../../../containers/GroupSchedulePage/SelectPlace';
import SchedulePageForm from '../../../containers/GroupSchedulePage/SchedulePageForm';

const GroupSchedulePageTop = (props) => {
    const { t } = useTranslation('common');
    const { handleSubmit } = props;

    return (
        <section className="schedule_page-container">
            <p>{t(GREETING_SCHEDULE_MESSAGE)}</p>
            <p>{t(GREETING_SCHEDULE_MESSAGE_HINT)}</p>
            <section className="schedule-form-buttons-container">
                <Card additionClassName="form-card schedule-form-card">
                    <SchedulePageForm onSubmit={handleSubmit} />
                </Card>
                <SelectPlace />
            </section>
        </section>
    );
};

export default GroupSchedulePageTop;
