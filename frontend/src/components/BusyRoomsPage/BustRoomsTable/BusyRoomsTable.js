import React from 'react';
import './BusyRoomsTable.scss';
import TableItem from '../TableItem/TableItem';
import ScheduleDaySidebar from '../../ScheduleTable/ScheduleDaySidebar/ScheduleDaySidebar';
import { ROOM_LIST_TITLE } from '../../../constants/translationLabels/common';

const BusyRoomsTable = (props) => {
    const { busyRooms, days, classes, columnsSize, t } = props;
    return (
        <section className="busy-rooms-table-container">
            <ScheduleDaySidebar title={t(ROOM_LIST_TITLE)} days={days} classes={classes} />
            <section className="view-rooms">
                {busyRooms.map((busyRoom) => (
                    <div className="busy-room-section" key={busyRoom.room_id}>
                        <span
                            title={busyRoom.room_type}
                            className={`busy-room-title responsive-table-column-${columnsSize} schedule-card sticky-container`}
                        >
                            {busyRoom.room_name}
                        </span>
                        {busyRoom.schedules.map((schedule, index) => {
                            return days.includes(schedule.day) ? (
                                <TableItem
                                    key={index.toString() + busyRoom.room_id}
                                    index={index.toString() + busyRoom.room_id}
                                    classes={classes}
                                    t={t}
                                    schedule={schedule}
                                />
                            ) : null;
                        })}
                    </div>
                ))}
            </section>
        </section>
    );
};
export default BusyRoomsTable;
