import React from 'react';
import { Route } from 'react-router-dom';
import HomePage from '../../containers/Home/Home';

export default function AccessRoute({ component: Component, condition, ...rest }) {
    return (
        <Route
            {...rest}
            render={(props) => {
                return condition ? <Component {...props} /> : <HomePage />;
            }}
        ></Route>
    );
}
