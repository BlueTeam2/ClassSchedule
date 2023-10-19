import React, { useState } from 'react';
import { IoMdLink } from 'react-icons/all';
import CustomDialog from '../../containers/Dialogs/CustomDialog';
import { dialogTypes } from '../../constants/dialogs';
import './LinkToMeeting.scss';

const LinkToMeeting = (props) => {
    const { linkToMeeting } = props;
    const [isOpenMeetingDialog, setIsOpenMeetingDialog] = useState(false);
    const openWindowByUrl = (url) => {
        const win = window.open(url, '_blank');
        win.focus();
    };
    const handelRedirectToMeeting = (semesterId) => {
        setIsOpenMeetingDialog(false);
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
                    setIsOpenMeetingDialog(true);
                }}
                title={linkToMeeting}
            />
            {isOpenMeetingDialog && (
                <CustomDialog
                    type={dialogTypes.MEETING_LINK}
                    handelConfirm={handelRedirectToMeeting}
                    open={isOpenMeetingDialog}
                    onClose={() => {
                        setIsOpenMeetingDialog(false);
                    }}
                    linkToMeeting={linkToMeeting}
                />
            )}
        </>
    );
};

export default LinkToMeeting;
