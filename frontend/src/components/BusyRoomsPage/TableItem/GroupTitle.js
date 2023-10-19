import React from 'react';
import './TableItem.scss';

export const GroupTitle = (props) => {
    const { lessonArray } = props;
    return lessonArray.map((lesson) => {
        const subject = lesson.subject_for_site;
        const teacher = lesson.teacher_for_site;
        return lesson.groups.map((groupItem, index) => {
            const hoverInfo = `${teacher} / ${subject}`;
            return (
                <span
                    className="group-list"
                    title={hoverInfo}
                    key={`${hoverInfo}-${groupItem.group_name}-${index.toString()}`}
                >
                    {groupItem.group_name}
                </span>
            );
        });
    });
};
