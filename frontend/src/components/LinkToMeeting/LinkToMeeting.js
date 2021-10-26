import React, { useState } from 'react';
import { IoMdLink } from 'react-icons/all';
import { CustomDialog } from '../../share/DialogWindows';
import { dialogTypes } from '../../constants/dialogs';
import './LinkToMeeting.scss';

const LinkToMeeting = (props) => {
    const { linkToMeeting } = props;
    const [isOpenMeetingLinkDialog, setIsOpenMeetingLinkDialog] = useState(false);
    const openWindowByUrl = (url) => {
        const win = window.open(url, '_blank');
        win.focus();
    };
    const handelRedirectToMeeting = (semesterId) => {
        setIsOpenMeetingLinkDialog(false);
        if (semesterId !== '') {
            openWindowByUrl(linkToMeeting);
        }
    };

    return (
        <>
            <IoMdLink
                color="blue"
                className="svg-btn copy-btn link"
                onClick={() => {
                    setIsOpenMeetingLinkDialog(true);
                }}
                title={linkToMeeting}
            />
            {isOpenMeetingLinkDialog && (
                <CustomDialog
                    type={dialogTypes.MEETING_LINK}
                    handelConfirm={handelRedirectToMeeting}
                    open={isOpenMeetingLinkDialog}
                    linkToMeeting={linkToMeeting}
                />
            )}
        </>
    );
};

export { LinkToMeeting };
