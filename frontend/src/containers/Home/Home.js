import React, { Fragment, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { connect } from 'react-redux';
import GroupSchedulePage from '../GroupSchedulePage/GroupSchedulePage';
import { getPublicClassScheduleStart } from '../../actions/classes';

import { HOME_TITLE } from '../../constants/translationLabels/common';

const HomePage = (props) => {
    const { getClassScheduleList } = props;
    const { t } = useTranslation('common');

    useEffect(() => {
        getClassScheduleList();
    }, []);

    return (
        <Fragment>
            <h1>{t(HOME_TITLE)}</h1>
            <GroupSchedulePage scheduleType="default" />
        </Fragment>
    );
};
const mapDispatchToProps = (dispatch) => ({
    getClassScheduleList: () => dispatch(getPublicClassScheduleStart()),
});

export default connect(null, mapDispatchToProps)(HomePage);
