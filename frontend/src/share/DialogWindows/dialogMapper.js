import React from 'react';
import i18n from '../../i18n';
import { dialogTypes, dialogYesButton, dialogNoButton } from '../../constants/dialogs';
import {
    COMMON_DO_YOU_WANNA,
    COMMON_DO_YOU_WANNA_DISABLE,
    COMMON_DELETE_WORD,
    COMMON_DO_YOU_WANNA_SHOW,
    COMMON_THIS_CARD_TYPE,
    COMMON_GO_TO_MEETING_WORD,
    COMMON_SET_DEFAULT_WORD,
    COMMON_ARE_YOU_SURE,
} from '../../constants/translationLabels/common';

import { FORM_SEMESTER_ELEMENT } from '../../constants/translationLabels/formElements';

const dialogMapper = (props) => {
    const {
        type,
        whatDelete,
        handelConfirm,
        onClose,
        setOpenConfirmDialog,
        warnings,
        linkToMeeting = 'none',
    } = props;
    const handelClose = () => {
        setOpenConfirmDialog(false);
    };

    const defaultModalButtons = [
        dialogYesButton(handelConfirm),
        dialogNoButton(onClose || handelClose),
    ];

    switch (type) {
        case dialogTypes.DELETE_CONFIRM:
            return {
                title: (
                    <>
                        {i18n.t(COMMON_DO_YOU_WANNA)}{' '}
                        <span className="delete-word">{i18n.t(COMMON_DELETE_WORD)}</span>{' '}
                        {i18n.t(COMMON_THIS_CARD_TYPE, {
                            cardType: i18n.t(`formElements:${whatDelete}_element`),
                        })}
                    </>
                ),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        case dialogTypes.CONFIRM_WITH_WARNING:
            return {
                title: i18n.t(COMMON_ARE_YOU_SURE),
                children: (
                    <div className="availability-info">
                        {warnings.map((warning) => (
                            <p key={warning} className="availability-warning">
                                {warning}
                            </p>
                        ))}
                    </div>
                ),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        case dialogTypes.SET_VISIBILITY_DISABLED:
            return {
                title: i18n.t(COMMON_DO_YOU_WANNA_DISABLE),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        case dialogTypes.SET_VISIBILITY_ENABLED:
            return {
                title: i18n.t(COMMON_DO_YOU_WANNA_SHOW),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        case dialogTypes.MEETING_LINK:
            return {
                title: (
                    <>
                        {i18n.t(COMMON_DO_YOU_WANNA)}{' '}
                        <span>
                            <a
                                className="go-to-meeting"
                                href={linkToMeeting}
                                target="_blank"
                                title={linkToMeeting}
                                rel="noreferrer"
                            >
                                {i18n.t(COMMON_GO_TO_MEETING_WORD)}
                            </a>
                        </span>
                    </>
                ),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        case dialogTypes.SET_DEFAULT:
            return {
                title: (
                    <>
                        {i18n.t(COMMON_DO_YOU_WANNA)}{' '}
                        <span className="set-default">{i18n.t(COMMON_SET_DEFAULT_WORD)}</span>{' '}
                        {i18n.t(COMMON_THIS_CARD_TYPE, {
                            cardType: i18n.t(FORM_SEMESTER_ELEMENT),
                        })}
                    </>
                ),
                buttons: defaultModalButtons,
                onClose: handelClose,
                ...props,
            };
        default:
            return props;
    }
};

export default dialogMapper;
