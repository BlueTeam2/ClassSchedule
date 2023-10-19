import React from 'react';
import './GroupPage.scss';
import SearchPanel from '../../share/SearchPanel/SearchPanel';

const Sidebar = (props) => {
    const { setIsDisabled, isDisabled, setSearchItem, children } = props;

    return (
        <aside className="group-aside">
            <SearchPanel
                SearchChange={setSearchItem}
                showDisabled={() => setIsDisabled((prev) => !prev)}
            />
            {!isDisabled && children}
        </aside>
    );
};

export default Sidebar;
