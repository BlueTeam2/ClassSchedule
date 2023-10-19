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
public class DeleteTeacherVacationNotify extends AbstractTemporaryNotification {
    private final TemporaryScheduleService temporaryScheduleService;
    private final MailService mailService;

    @Autowired
    public DeleteTeacherVacationNotify(@Lazy TemporaryScheduleService temporaryScheduleService, MailService mailService) {
        this.temporaryScheduleService = temporaryScheduleService;
        this.mailService = mailService;
    }

    @Override
    public boolean check(TemporarySchedule temporarySchedule) throws MessagingException {
        log.info("In check of DeleteVacationNotification(temporarySchedule = [{}])", temporarySchedule);
        if (temporarySchedule.getTeacher() != null) {
            String temporaryTeacherEmail = temporaryScheduleService.getTeacherEmailFromTemporarySchedule(temporarySchedule.getTeacher());
            if (temporaryTeacherEmail != null) {
                if (temporarySchedule.isVacation()) {
                    mailService.send(temporaryTeacherEmail, "test", temporarySchedule, "mail/deleteVacationByTeacher");
                } else {
                    mailService.send(temporaryTeacherEmail, "test", temporarySchedule, "mail/deleteTemporaryScheduleByTeacher");
                }
            }
        }
        return checkNext(temporarySchedule);

    }
}
