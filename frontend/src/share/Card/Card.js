import React from 'react';
import './Card.scss';

const Card = (props) => {
    const { children, additionClassName = '' } = props;

    return <div className={`card ${additionClassName}`}>{children}</div>;
};

export default Card;
