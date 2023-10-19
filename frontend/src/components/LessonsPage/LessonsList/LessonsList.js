import React from 'react';
import LessonsCard from './LessonsCard';
import './LessonList.scss';

const LessonsList = (props) => {
    const { lessons, ...rest } = props;

    return (
        <div className="lesson-container">
            {lessons.map((lesson) => (
                <LessonsCard lesson={lesson} key={lesson.id} {...rest} />
            ))}
        </div>
    );
};

export default LessonsList;
