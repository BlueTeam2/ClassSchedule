import React, { useEffect, useState } from 'react';
import Paper from '@material-ui/core/Paper';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import './NavigationPanel.scss';
import { general, tabsComponents } from '../../constants/navigationComponents';
import { ADMIN_PAGE_LINK } from '../../constants/links';
import { COMMON_MORE_LABEL } from '../../constants/translationLabels/common';

const NavigationPanel = () => {
    const history = useHistory();
    const { t } = useTranslation('common');
    const [selectItem, setSelectItem] = useState('');
    const [selectedTab, setSelectedTab] = useState('');

    const getCurrentTabNameByUrl = () => {
        const pathName = history.location.pathname.split('/');
        pathName.shift();
        return pathName[1];
    };

    useEffect(() => {
        const tabByUrl = getCurrentTabNameByUrl();
        const tabIndex = tabsComponents.findIndex((item) => item.name === tabByUrl);
        if (tabIndex === -1) {
            setSelectedTab(tabsComponents.length);
            setSelectItem(tabByUrl);
        } else {
            setSelectedTab(tabIndex);
        }
    }, []);

    const handleNavigate = (item, index) => {
        setSelectedTab(index);
        document.title = t(item.title);
        history.push({ ...history.location, pathname: `${ADMIN_PAGE_LINK}/${item.name}` });
    };

    return (
        <Paper className="admin-navigation">
            <List className="navigation-container">
                {tabsComponents.map((item, index) => (
                    <ListItem
                        selected={selectedTab === index}
                        key={item.name}
                        onClick={() => handleNavigate(item, index)}
                        className="navigation-link"
                    >
                        <ListItemText>{t(item.title)}</ListItemText>
                    </ListItem>
                ))}
                <ListItem
                    className="navigation-link select-container "
                    selected={selectedTab === tabsComponents.length}
                >
                    <Select
                        className="navigation-select"
                        labelId="demo-controlled-open-select-label"
                        id="demo-controlled-open-select"
                        value={selectItem}
                        displayEmpty
                        onChange={(event) => {
                            const { value: eventValue } = event.target;
                            setSelectItem(eventValue);
                        }}
                    >
                        <MenuItem value="" disabled selected className="navigation-select-item">
                            {t(COMMON_MORE_LABEL)}
                        </MenuItem>
                        {general.map((item) => (
                            <MenuItem
                                key={item.name + item.title}
                                className="navigation-select-item"
                                value={item.name}
                                onClick={() => handleNavigate(item, tabsComponents.length)}
                            >
                                {t(item.title)}
                            </MenuItem>
                        ))}
                    </Select>
                </ListItem>
            </List>
        </Paper>
    );
};

export default NavigationPanel;
