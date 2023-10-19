package com.softserve.util.temporary_notification;

import com.softserve.entity.TemporarySchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.mail.MessagingException;

@NoArgsConstructor
@Setter
@Getter
public abstract class AbstractTemporaryNotification {
    private AbstractTemporaryNotification next;

    public AbstractTemporaryNotification linkWith(AbstractTemporaryNotification next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(TemporarySchedule object) throws MessagingException;

    protected boolean checkNext(TemporarySchedule object) throws MessagingException {
        if (next == null) {
            return true;
        }
        return next.check(object);
    }
}
