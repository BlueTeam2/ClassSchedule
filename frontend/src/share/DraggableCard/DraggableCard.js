import React from 'react';
import './DraggableCard.scss';

export const DraggableCard = (props) => {
    const { children, item, dragAndDropItem, setGroupStart } = props;

    const styleCard = (e) => {
        if (e.target.className === 'group-card drag-border-card') {
            e.target.className = 'group-card';
        }
    };
    const dragStartHandler = (card) => {
        setGroupStart(card);
    };
    const dragLeaveHandler = (e) => {
        styleCard(e);
    };
    const dragOverHandler = (e) => {
        e.preventDefault();
        if (e.target.className === 'group-card') {
            e.target.className = 'group-card drag-border-card';
        }
    };
    const dropHandler = (e, card) => {
        e.preventDefault();
        styleCard(e);
        dragAndDropItem(card.id);
    };

    return (
        <div
            className="drag-and-drop-card"
            onDragStart={() => dragStartHandler(item)}
            onDragLeave={(e) => dragLeaveHandler(e)}
            onDragOver={(e) => dragOverHandler(e)}
            onDrop={(e) => dropHandler(e, item)}
            draggable
        >
            {children}
        </div>
    );
};
