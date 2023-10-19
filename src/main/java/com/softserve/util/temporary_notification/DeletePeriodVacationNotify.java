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
public class DeletePeriodVacationNotify extends AbstractTemporaryNotification {
    private final TemporaryScheduleService temporaryScheduleService;
    private final MailService mailService;

    @Autowired
    public DeletePeriodVacationNotify(@Lazy TemporaryScheduleService temporaryScheduleService, MailService mailService) {
        this.temporaryScheduleService = temporaryScheduleService;
        this.mailService = mailService;
    }

    @Override
    public boolean check(TemporarySchedule temporarySchedule) throws MessagingException {
        log.info("In check of DeleteVacationNotification(temporarySchedule = [{}])", temporarySchedule);
        if (temporarySchedule.getPeriod() != null) {
            String temporaryTeacherEmail = temporaryScheduleService.getTeacherEmailFromTemporarySchedule(temporarySchedule.getTeacher());
            if (temporaryTeacherEmail != null) {
                if (temporarySchedule.isVacation()) {
                    mailService.send(temporaryTeacherEmail, "test", temporarySchedule,
                            "mail/deleteVacationByPeriodForOriginTeacher");
                } else {
                    mailService.send(temporaryTeacherEmail, "test", temporarySchedule,
                            "mail/deleteTemporaryScheduleByPeriodForTemporaryTeacher");

                    String originalScheduleTeacherEmail = temporaryScheduleService
                            .getTeacherEmailFromTemporarySchedule(temporaryScheduleService.getTeacherByScheduleId(temporarySchedule.getScheduleId()));
                    if (originalScheduleTeacherEmail != null && !temporaryTeacherEmail.equals(originalScheduleTeacherEmail)) {
                        mailService.send(originalScheduleTeacherEmail, "test", temporarySchedule,
                                "mail/deleteTemporaryScheduleByPeriodForOriginTeacher");
                    }
                }
            }
        }
        return checkNext(temporarySchedule);

    }
}
