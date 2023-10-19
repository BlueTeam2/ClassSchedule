import React, { Fragment } from 'react';
import i18n from 'i18next';
import './ScheduleDaySidebar.scss';

const ScheduleDaySidebar = (props) => {
    const { title, days, classes } = props;
    const getDayColour = (index) => {
        return index % 2 ? 'dark-blue-day' : 'blue-day';
    };

    return (
        <aside className="day-classes-aside">
            <div className="schedule-card day-sidebar-title">{title}</div>
            {days.map((day, index) => (
                <div className="cards-container day-container" key={day}>
                    <span className={`${getDayColour(index)} schedule-day card`}>
                        {i18n.t(`day_of_week_${day}`)}
                    </span>
                    <div className="class-section">
                        {classes.map((classScheduler) => (
                            <Fragment key={classScheduler.id}>
                                <div className="day-section">
                                    <p
                                        className={`day-line ${day}-${classScheduler.class_name}`}
                                    ></p>
                                    <span
                                        id={`${day}-${classScheduler.class_name}`}
                                        className="schedule-card schedule-class"
                                    >
                                        {classScheduler.class_name}
                                    </span>
                                    <p
                                        className={`day-line ${day}-${classScheduler.class_name}`}
                                    ></p>
                                </div>
                            </Fragment>
                        ))}
                    </div>
                </div>
            ))}
        </aside>
    );
};
export default ScheduleDaySidebar;
