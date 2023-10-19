import { MenuItem, Select } from '@material-ui/core';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { places } from '../../constants/places';
import { PLACE_FOR_CLASS_LABEL } from '../../constants/translationLabels/common';

const SelectPlace = (props) => {
    const { place, changePlace } = props;
    const { t } = useTranslation('common');

    const setPlace = ({ target }) => {
        if (target) {
            changePlace(target.value);
            localStorage.setItem('place', target.value);
        }
    };

    return (
        <div id="select-place">
            <label htmlFor="demo-controlled-open-select">{t(PLACE_FOR_CLASS_LABEL)}</label>
            <Select
                className="place"
                labelId="demo-controlled-open-select-label"
                id="demo-controlled-open-select"
                value={place}
                onChange={setPlace}
            >
                {Object.entries(places).map(([key, value]) => (
                    <MenuItem value={value} key={key}>
                        {t(`${value}_label`)}
                    </MenuItem>
                ))}
            </Select>
        </div>
    );
};

export default SelectPlace;
