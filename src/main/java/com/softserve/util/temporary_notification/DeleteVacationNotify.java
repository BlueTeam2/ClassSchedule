package com.softserve.util.temporary_notification;

import com.softserve.entity.TemporarySchedule;
import com.softserve.service.MailService;
import com.softserve.service.TemporaryScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Slf4j
@Component
public class DeleteVacationNotify extends AbstractTemporaryNotification {
    private final TemporaryScheduleService temporaryScheduleService;
    private final MailService mailService;

    @Autowired
    public DeleteVacationNotify(@Lazy TemporaryScheduleService temporaryScheduleService, MailService mailService) {
        this.temporaryScheduleService = temporaryScheduleService;
        this.mailService = mailService;
    }

    @Override
    public boolean check(TemporarySchedule temporarySchedule) throws MessagingException {
        log.info("In check of DeleteVacationNotification(temporarySchedule = [{}])", temporarySchedule);
        String temporaryTeacherEmail = temporaryScheduleService.getTeacherEmailFromTemporarySchedule(temporarySchedule.getTeacher());
        if (temporaryTeacherEmail != null) {
            mailService.send(temporaryTeacherEmail, "test", temporarySchedule, "mail/deleteVacation");
        }
        return checkNext(temporarySchedule);

    }
}
