package com.softserve.service.impl;

import com.softserve.entity.Semester;
import com.softserve.entity.Teacher;
import com.softserve.entity.TemporarySchedule;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.repository.TemporaryScheduleRepository;
import com.softserve.service.*;
import com.softserve.util.temporary_notification.DeletePeriodVacationNotify;
import com.softserve.util.temporary_notification.DeleteTeacherVacationNotify;
import com.softserve.util.temporary_notification.DeleteVacationNotify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Transactional
@Service
@Slf4j
public class TemporaryScheduleServiceImpl implements TemporaryScheduleService {

    private final TemporaryScheduleRepository temporaryScheduleRepository;

    private final SemesterService semesterService;
    private final GroupService groupService;
    private final RoomService roomService;
    private final PeriodService periodService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final ScheduleService scheduleService;
    private final UserService userService;

    private final DeleteVacationNotify deleteVacationNotify;
    private final DeleteTeacherVacationNotify deleteTeacherVacationNotify;
    private final DeletePeriodVacationNotify deletePeriodVacationNotify;

    @Autowired
    public TemporaryScheduleServiceImpl(TemporaryScheduleRepository temporaryScheduleRepository, SemesterService semesterService,
                                        GroupService groupService, RoomService roomService, PeriodService periodService,
                                        SubjectService subjectService, TeacherService teacherService, @Lazy ScheduleService scheduleService,
                                        UserService userService, DeleteVacationNotify deleteVacationNotify,
                                        DeleteTeacherVacationNotify deleteTeacherVacationNotify,
                                        DeletePeriodVacationNotify deletePeriodVacationNotify) {
        this.temporaryScheduleRepository = temporaryScheduleRepository;
        this.semesterService = semesterService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.periodService = periodService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.deleteVacationNotify = deleteVacationNotify;
        this.deleteTeacherVacationNotify = deleteTeacherVacationNotify;
        this.deletePeriodVacationNotify = deletePeriodVacationNotify;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporarySchedule getById(Long id) {
        log.info("Enter into getById of TemporaryScheduleServiceImpl with id {}", id);
        TemporarySchedule temporarySchedule = temporaryScheduleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(TemporarySchedule.class, "id", id.toString()));
//        Hibernate.initialize(temporarySchedule.getTeacher());
//        Hibernate.initialize(temporarySchedule.getTeacher().getSurname());
//        Hibernate.initialize(temporarySchedule.getSubject());
//        Hibernate.initialize(temporarySchedule.getRoom());
//        Hibernate.initialize(temporarySchedule.getGroup());
        Hibernate.initialize(temporarySchedule.getSemester().getPeriods());
        return temporarySchedule;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAll() {
        log.info("Enter into getAll of TemporaryScheduleServiceImpl");
        return temporaryScheduleRepository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId) {
        log.info("Enter into getAllByTeacherAndRange of TemporaryScheduleServiceImpl");
        return temporaryScheduleRepository.getAllByTeacherAndRange(fromDate, toDate, teacherId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getTemporaryScheduleByTeacherAndRange(LocalDate fromDate, LocalDate toDate, Long teacherId) {
        log.info("Enter into getAllByTeacherAndRange of TemporaryScheduleServiceImpl");
        List<TemporarySchedule> temporarySchedules = temporaryScheduleRepository.temporaryScheduleByDateRangeForTeacher(fromDate, toDate, teacherId);
        for (TemporarySchedule temp : temporarySchedules) {
            Hibernate.initialize(temp.getSemester().getPeriods());
        }
        return temporarySchedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllBySemesterAndRange(Long semesterId, LocalDate fromDate, LocalDate toDate) {
        log.info("Enter into getAllBySemesterAndRange of TemporaryScheduleServiceImpl");
        return temporaryScheduleRepository.getAllBySemesterAndRange(semesterId, fromDate, toDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllByCurrentSemester() {
        log.info("Enter into getAllByCurrentSemester of TemporaryScheduleServiceImpl");
        List<TemporarySchedule> temporarySchedules = temporaryScheduleRepository.getAllBySemester(semesterService.getCurrentSemester().getId());
        for (TemporarySchedule temporarySchedule : temporarySchedules) {
            Hibernate.initialize(temporarySchedule.getSemester().getPeriods());
        }
        return temporarySchedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllBySemesterId(Long semesterId) {
        log.info("Enter into getAllBySemesterId of TemporaryScheduleServiceImpl");
        List<TemporarySchedule> temporarySchedules = temporaryScheduleRepository.getAllBySemester(semesterId);
        for (TemporarySchedule temp : temporarySchedules) {
            Hibernate.initialize(temp.getSemester().getPeriods());
        }
        return temporarySchedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> getAllByRange(LocalDate fromDate, LocalDate toDate) {
        log.info("Enter into getAllBySemester of TemporaryScheduleServiceImpl");
        List<TemporarySchedule> temporarySchedules = temporaryScheduleRepository.getAllByRange(fromDate, toDate);
        for (TemporarySchedule temp : temporarySchedules) {
            Hibernate.initialize(temp.getSemester().getPeriods());
        }
        return temporarySchedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemporarySchedule> vacationByDateRange(LocalDate fromDate, LocalDate toDate) {
        log.info("Enter into vacationByDateRange of TemporaryScheduleServiceImpl");
        List<TemporarySchedule> temporarySchedules = temporaryScheduleRepository.vacationByDateRangeForTeacher(fromDate, toDate);
        temporarySchedules.forEach(t -> Hibernate.initialize(t.getSemester().getPeriods()));
        return temporarySchedules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTemporarySchedulesBySemesterId(Long semesterId) {
        log.info("In deleteTemporarySchedulesBySemesterId with semesterId = {}", semesterId);
        temporaryScheduleRepository.deleteTemporarySchedulesBySemesterId(semesterId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<EvenOdd, Map<DayOfWeek, List<TemporarySchedule>>> getTemporaryScheduleForEvenOddWeeks(Long semesterId) {
        log.info("In getTemporaryScheduleForEvenOddWeeks with semesterId = {}", semesterId);
        Map<EvenOdd, Map<DayOfWeek, List<TemporarySchedule>>> evenOddListMap = new EnumMap<>(EvenOdd.class);
        Semester semester = semesterService.getById(semesterId);
        LocalDate today = LocalDate.now();
        LocalDate from = today.with(previousOrSame(MONDAY));
        int countStartDate = semester.getStartDay().getDayOfWeek().getValue();
        int countEndDate = today.getDayOfWeek().getValue();
        int countDays = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(
                semester.getStartDay().minusDays(countStartDate), today.plusDays(7L - countEndDate))));

        EvenOdd keyOne = (countDays / 7) % 2 != 0 ? EvenOdd.EVEN : EvenOdd.ODD;
        EvenOdd keyTwo = keyOne.equals(EvenOdd.EVEN) ? EvenOdd.ODD : EvenOdd.EVEN;
        try {
            List<TemporarySchedule> one = getAllBySemesterAndRange(semesterId, from, from.plusDays(7));
            List<TemporarySchedule> two = getAllBySemesterAndRange(semesterId, from.plusDays(8), from.plusDays(14));

            Map<DayOfWeek, List<TemporarySchedule>> oneMap = new EnumMap<>(DayOfWeek.class);
            Map<DayOfWeek, List<TemporarySchedule>> twoMap = new EnumMap<>(DayOfWeek.class);


            for (TemporarySchedule temporarySchedule : one) {
                oneMap.computeIfAbsent(temporarySchedule.getDate().getDayOfWeek(), k -> new ArrayList<>()).add(temporarySchedule);
            }

            for (TemporarySchedule temporarySchedule : two) {
                twoMap.computeIfAbsent(temporarySchedule.getDate().getDayOfWeek(), k -> new ArrayList<>()).add(temporarySchedule);
            }
            evenOddListMap.put(keyOne, oneMap);
            evenOddListMap.put(keyTwo, twoMap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return evenOddListMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> addRange(LocalDate from, LocalDate to, TemporarySchedule object) {
        log.info("Enter into addRange of TemporaryScheduleServiceImpl with entity:{}", object);
        List<String> messagesList = new ArrayList<>();
        if (object.getSemester() == null) {
            Semester semester = new Semester();
            semester.setId(semesterService.getCurrentSemester().getId());
            object.setSemester(semester);
        }
        for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
            TemporarySchedule temporarySchedule = SerializationUtils.clone(object);
            temporarySchedule.setDate(date);
            try {
                this.save(temporarySchedule);
            } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
                messagesList.add(temporarySchedule.getDate() + " " + e.getMessage());
            }
        }
        return messagesList;
    }

    /**
     * Saves the given temporary schedule in the repository.
     *
     * @param object the temporary schedule
     * @return the saved temporary schedule
     * @throws EntityAlreadyExistsException
     */
    @Override
    public TemporarySchedule save(TemporarySchedule object) {
        log.info("Enter into save of TemporaryScheduleServiceImpl with entity:{}", object);
        if (object.getSemester() == null) {
            Semester semester = new Semester();
            semester.setId(semesterService.getCurrentSemester().getId());
            object.setSemester(semester);
        }
        if (object.isVacation()) {
            check(object);
        } else {
            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Please remove vacation before");
            }
            if (isExistTemporarySchedule(object, false)) {
                throw new EntityAlreadyExistsException("Entity with this parameters already exists");
            } else {
                checkReferencedElement(object);
            }
        }
//        if(temporarySchedule.isNotification()){
//            try {
//                DeleteVacationNotification deleteVacationNotification = new DeleteVacationNotification(teacherService, userService, mailService);
//                    deleteVacationNotification.check(temporarySchedule);
//            } catch (MessagingException e) {
//                log.error(e.toString());
//            }
//        }
        return temporaryScheduleRepository.save(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporarySchedule update(TemporarySchedule object) {
        log.info("Enter into update of TemporaryScheduleServiceImpl with entity:{}", object);
        if (object.getSemester() == null) {
            Semester semester = new Semester();
            semester.setId(semesterService.getCurrentSemester().getId());
            object.setSemester(semester);
        }
        if (object.isVacation()) {
            checkUpdate(object);
        } else {
            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Please remove vacation before");
            }
            if (isExistTemporaryScheduleWithIgnoreId(object)) {
                throw new EntityAlreadyExistsException("Entity with this parameters already exists");
            } else {
                checkReferencedElement(object);
            }
        }
        TemporarySchedule entity = temporaryScheduleRepository.update(object);
        return this.getById(entity.getId());
    }

    private void check(TemporarySchedule object) {
        if (object.getTeacher() != null && object.getScheduleId() != null) {
            if (isExistTemporaryScheduleByDateAndScheduleId(object, false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule before add vacation by class");
            }

            if (isExistTemporaryScheduleByDateAndScheduleId(object, true)) {
                throw new EntityAlreadyExistsException("Vacation by class already exist");
            }

            if (isExistTemporaryScheduleByVacationByDateAndTeacher(object.getDate(), object.getSemester().getId(),
                    object.getTeacher().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation  for this teacher by date. We can't add vacation by class");
            }

            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation by date. We can't add vacation by class");
            }
        } else if (object.getTeacher() != null) {
            if (isExistTemporaryScheduleByVacationByDateAndTeacher(object.getDate(), object.getSemester().getId(),
                    object.getTeacher().getId(), false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule for this teacher  before add vacation");
            }

            if (isExistTemporaryScheduleByVacationByDateAndTeacher(object.getDate(), object.getSemester().getId(),
                    object.getTeacher().getId(), true)) {
                throw new EntityAlreadyExistsException("Vacation for this teacher  already exists");
            }

            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation by date. We can't add vacation by teacher");
            }
        } else {
            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule before add vacation");
            }

            if (isExistTemporaryScheduleByVacationByDate(object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Vacation with this date already exists");
            }
        }
    }

    private void checkUpdate(TemporarySchedule object) {
        if (object.getTeacher() != null && object.getScheduleId() != null) {
            if (isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(object, false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule before add vacation by class");
            }

            if (isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(object, true)) {
                throw new EntityAlreadyExistsException("Vacation by class already exist");
            }

            if (isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(object.getId(), object.getDate(),
                    object.getSemester().getId(), object.getTeacher().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation  for this teacher by date. We can't add vacation by class");
            }

            if (isExistTemporaryScheduleByVacationByDateWithIgnoreId(object.getId(), object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation by date. We can't add vacation by class");
            }
        } else if (object.getTeacher() != null) {
            if (isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(object.getId(), object.getDate(),
                    object.getSemester().getId(), object.getTeacher().getId(), false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule for this teacher  before add vacation");
            }

            if (isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(object.getId(), object.getDate(),
                    object.getSemester().getId(), object.getTeacher().getId(), true)) {
                throw new EntityAlreadyExistsException("Vacation for this teacher  already exists");
            }

            if (isExistTemporaryScheduleByVacationByDateWithIgnoreId(object.getId(), object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Added vacation by date. We can't add vacation by teacher");
            }
        } else {
            if (isExistTemporaryScheduleByVacationByDateWithIgnoreId(object.getId(), object.getDate(), object.getSemester().getId(), false)) {
                throw new EntityAlreadyExistsException("Please remove temporary schedule before add vacation");
            }

            if (isExistTemporaryScheduleByVacationByDateWithIgnoreId(object.getId(), object.getDate(), object.getSemester().getId(), true)) {
                throw new EntityAlreadyExistsException("Vacation with this date already exists");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporarySchedule delete(TemporarySchedule object) {
        log.info("Enter into delete of TemporaryScheduleServiceImpl with entity:{}", object);
        TemporarySchedule temporarySchedule = temporaryScheduleRepository.delete(object);
        try {
            deleteVacationNotify.linkWith(deleteTeacherVacationNotify).linkWith(deletePeriodVacationNotify).check(object);
        } catch (MessagingException e) {
            log.error(e.toString());
        }
        return temporarySchedule;
    }

    private boolean isExistTemporaryScheduleByVacationByDate(LocalDate date, Long semesterId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDate(date = [{}], semesterId = [{}] ,vacation = [{}])", date, semesterId, vacation);
        return temporaryScheduleRepository.isExistTemporaryScheduleByVacationByDate(date, semesterId, vacation) != 0;
    }

    private boolean isExistTemporaryScheduleByVacationByDateWithIgnoreId(Long id, LocalDate date, Long semesterId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDateWithIgnoreId(date = [{}], semesterId = [{}] ,vacation = [{}])",
                date, semesterId, vacation);
        return temporaryScheduleRepository.isExistTemporaryScheduleByVacationByDateWithIgnoreId(id, date, semesterId, vacation) != 0;
    }

    private boolean isExistTemporaryScheduleByVacationByDateAndTeacher(LocalDate date, Long semesterId, Long teacherId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDateAndTeacher(date = [{}], semesterId = [{}] , teacherId = [{}], vacation = [{}])",
                date, semesterId, teacherId, vacation);
        return temporaryScheduleRepository.isExistTemporaryScheduleByVacationByDateAndTeacher(date, semesterId, teacherId, vacation) != 0;
    }

    private boolean isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(Long id, LocalDate date, Long semesterId,
                                                                                   Long teacherId, boolean vacation) {
        log.info("In isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId" +
                "(date = [{}], semesterId = [{}] , teacherId = [{}], vacation = [{}])", date, semesterId, teacherId, vacation);
        return temporaryScheduleRepository
                .isExistTemporaryScheduleByVacationByDateAndTeacherWithIgnoreId(id, date, semesterId, teacherId, vacation) != 0;
    }

    private boolean isExistTemporarySchedule(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporarySchedule(object = [{}])", object);
        return temporaryScheduleRepository.isExistTemporarySchedule(object, vacation) != 0;
    }

    private boolean isExistTemporaryScheduleWithIgnoreId(TemporarySchedule object) {
        log.info("In isExistTemporarySchedule(object = [{}])", object);
        return temporaryScheduleRepository.isExistTemporaryScheduleWithIgnoreId(object) != 0;
    }

    private boolean isExistTemporaryScheduleByDateAndScheduleId(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporaryScheduleByDateAndScheduleId(object = [{}])", object);
        return temporaryScheduleRepository.isExistTemporaryScheduleByDateAndScheduleId(object, vacation) != 0;
    }

    private boolean isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(TemporarySchedule object, boolean vacation) {
        log.info("In isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(object = [{}])", object);
        return temporaryScheduleRepository.isExistTemporaryScheduleByDateAndScheduleIdWithIgnoreId(object, vacation) != 0;
    }

    /**
     * {@inheritDoc}
     */
    public Teacher getTeacherByScheduleId(Long scheduleId) {
        if (scheduleId != null) {
            return scheduleService.getById(scheduleId).getLesson().getTeacher();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTeacherEmailFromTemporarySchedule(Teacher teacher) {
        if (teacher != null) {
            Long toTeacherId = teacherService.getById(teacher.getId()).getUserId();
            if (toTeacherId != null) {
                return userService.getById(toTeacherId).getEmail();
            }
        }
        return null;
    }

    private void checkReferencedElement(TemporarySchedule object) throws EntityNotFoundException {
        log.info("In checkReferenceExist(object = [{}])", object);
        //scheduleService.getById(object.getScheduleId());
        subjectService.getById(object.getSubject().getId());
        roomService.getById(object.getRoom().getId());
        groupService.getById(object.getGroup().getId());
        teacherService.getById(object.getTeacher().getId());
        periodService.getById(object.getPeriod().getId());
    }
}



