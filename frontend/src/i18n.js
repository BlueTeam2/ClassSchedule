import i18next from 'i18next';
import Backend from 'i18next-http-backend';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

const supportedLangs = ['en', 'uk'];
const userLang = localStorage.getItem('i18nextLng') || navigator.language;
export const lang = supportedLangs.find((i) => i === userLang) || 'en';

i18next
    .use(Backend)
    .use(LanguageDetector)
    .use(initReactI18next)
    .init(
        {
            lng: lang,
            fallbackLng: 'en',
            debug: false,
            ns: ['formElements', 'validationMessages', 'common', 'serviceMessages'],
            defaultNS: 'common',
            keySeparator: false,
            useDataAttrOptions: true,
            interpolation: {
                escapeValue: false,
                formatSeparator: ',',
            },
            backend: {
                loadPath: '/assets/i18n/translations/{{lng}}/{{ns}}.json',
            },
            react: {
                bindI18n: 'languageChanged',
                bindStore: '',
                transEmptyNodeValue: '',
                transSupportBasicHtmlNodes: true,
                useSuspense: true,
                wait: true,
            },
        },
        (err) => {
            if (err) {
                console.error('Some problems with i18next!');
            }
        },
    );

export default i18next;
