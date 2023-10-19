import React, { useEffect, useState } from 'react';
import { CircularProgress } from '@material-ui/core';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import { useTranslation } from 'react-i18next';
import { get } from 'lodash';
import './BusyRoomsPage.scss';
import BusyRoomsTable from './BustRoomsTable/BusyRoomsTable';
import { COMMON_TABLE_COLUMNS_SIZE } from '../../constants/translationLabels/common';
import { columnSizeArray } from '../../constants/schedule/schedule';

const BusyRoomsPage = (props) => {
    const {
        currentSemester,
        getAllScheduleItems,
        busyRooms,
        getBusyRooms,
        getClassScheduleList,
        setScheduleLoading,
        scheduleLoading,
    } = props;
    const { t } = useTranslation('common');
    const [columnsSize, setColumnsSize] = useState(
        localStorage.getItem('roomsTableColumnsSize') || 'base',
    );

    useEffect(() => {
        setScheduleLoading(true);
        getBusyRooms();
        getAllScheduleItems();
        getClassScheduleList();
    }, []);

    const handleChange = ({ target }) => {
        setColumnsSize(target.value);
        localStorage.setItem('roomsTableColumnsSize', target.value);
    };

    const days = currentSemester.semester_days;
    const classes = currentSemester.semester_classes;

    return (
        <section className="schedule-card busy-rooms-control-panel">
            {scheduleLoading || !get(busyRooms[0], 'schedules') ? (
                <CircularProgress className="loading-circle" />
            ) : (
                <>
                    <div className="table-size-container">
                        <FormControl component="div" className="radio-control">
                            <FormLabel component="legend">{`${t(
                                COMMON_TABLE_COLUMNS_SIZE,
                            )}:`}</FormLabel>
                            <RadioGroup
                                aria-label="gender"
                                className="radio-group"
                                value={columnsSize}
                                onChange={handleChange}
                            >
                                {columnSizeArray.map((item) => (
                                    <FormControlLabel
                                        key={item.value}
                                        value={item.value}
                                        control={<Radio />}
                                        label={t(item.label)}
                                    />
                                ))}
                            </RadioGroup>
                        </FormControl>
                    </div>
                    <BusyRoomsTable
                        days={days}
                        t={t}
                        columnsSize={columnsSize}
                        classes={classes}
                        busyRooms={busyRooms}
                    />
                </>
            )}
        </section>
    );
};

export default BusyRoomsPage;
