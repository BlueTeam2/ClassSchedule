import React, { useEffect } from 'react';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import { useTranslation } from 'react-i18next';
import './LanguageSelector.scss';
import { languageShorthands } from '../../constants/languages/shorthands';
import { languageTitles } from '../../constants/languages/titles';
import ukIcon from '../../share/icons/uk.png';
import uaIcon from '../../share/icons/ua.png';

const LanguageSelector = () => {
    const { i18n } = useTranslation();
    const langFlags = [
        {
            lang: languageShorthands.English,
            img: ukIcon,
            title: languageTitles.English,
        },
        {
            lang: languageShorthands.Ukrainian,
            img: uaIcon,
            title: languageTitles.Ukrainian,
        },
    ];
    const radioLangClasses = {};
    langFlags.forEach((lang) => {
        radioLangClasses[lang.lang] = 'languageItem ';
    });
    radioLangClasses[i18n.language] += ' activeLanguage';

    const changeLanguage = (event) => {
        radioLangClasses[i18n.language] = 'languageItem ';
        radioLangClasses[event.target.value] += ' activeLanguage';
        i18n.changeLanguage(event.target.value);
    };
    const renderLangControls = (langItem) => (
        <FormControlLabel
            key={langItem.lang}
            control={
                <>
                    <Radio color="primary" value={langItem.lang} onChange={changeLanguage} />
                    <img
                        className="language-icon"
                        src={langItem.img}
                        alt={langItem.lang}
                        title={langItem.title}
                    />
                </>
            }
            className={radioLangClasses[langItem.lang]}
        />
    );

    return (
        <RadioGroup
            row
            aria-label="lang"
            name="lang"
            value={i18n.language}
            className="lang_selector"
        >
            {langFlags.map((langItem) => renderLangControls(langItem))}
        </RadioGroup>
    );
};

export default LanguageSelector;
