package com.softserve.service;

import com.softserve.dto.TeacherDTO;
import com.softserve.entity.*;
import com.softserve.entity.enums.EvenOdd;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.mapper.TeacherMapper;
import com.softserve.repository.ScheduleRepository;
import com.softserve.service.impl.ScheduleServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private LessonService lessonService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private SemesterService semesterService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;
    @Mock
    private TemporaryScheduleService temporaryScheduleService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleServiceImpl;

    @Test
    public void getById() {
        Semester semester = new Semester();
        Lesson lesson = new Lesson();
        lesson.setSemester(semester);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setLesson(lesson);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        Schedule result = scheduleServiceImpl.getById(1L);
        assertNotNull(result);
        assertEquals(schedule.getId(), result.getId());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfScheduleNotFounded() {
        Semester semester = new Semester();
        Lesson lesson = new Lesson();
        lesson.setSemester(semester);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setLesson(lesson);

        scheduleServiceImpl.getById(2L);
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test
    public void save() {
        Group group = new Group();
        Semester semester = new Semester();
        Lesson lesson = new Lesson();
        Period period = new Period();
        Room room = new Room();
        lesson.setId(1L);
        lesson.setSemester(semester);
        lesson.setGroup(group);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setLesson(lesson);
        schedule.setPeriod(period);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setRoom(room);

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        when(lessonService.getById(anyLong())).thenReturn(lesson);

        Schedule result = scheduleServiceImpl.save(schedule);
        assertNotNull(result);
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    public void update() {
        Group group = new Group();
        Semester semester = new Semester();
        Lesson lesson = new Lesson();
        Period period = new Period();
        Room room = new Room();
        lesson.setSemester(semester);
        lesson.setGroup(group);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setLesson(lesson);
        schedule.setPeriod(period);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setRoom(room);
        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setId(1L);
        updatedSchedule.setEvenOdd(EvenOdd.EVEN);
        updatedSchedule.setLesson(lesson);
        updatedSchedule.setPeriod(period);
        updatedSchedule.setDayOfWeek(DayOfWeek.MONDAY);
        updatedSchedule.setRoom(room);
        when(scheduleRepository.update(updatedSchedule)).thenReturn(updatedSchedule);
        when(lessonService.getById(lesson.getId())).thenReturn(lesson);
        schedule = scheduleServiceImpl.update(updatedSchedule);
        assertNotNull(schedule);
        assertEquals(updatedSchedule, schedule);
        verify(scheduleRepository, times(1)).update(updatedSchedule);
    }

    @Test
    public void sendScheduleToTeachers() throws MessagingException {
        User user = new User();
        user.setId(1L);
        user.setEmail("Test@gmail.com");
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        teacher.setDisable(false);
        teacher.setUserId(1L);
        Semester semester = new Semester();
        semester.setId(4L);
        TeacherDTO teacherDTO = new TeacherDTO();
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        Long[] id = new Long[]{10L, 20L, 30L, 40L};
        dayOfWeeks.add(DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.TUESDAY);
        when(teacherService.getById(anyLong())).thenReturn(teacher);
        when(semesterService.getById(anyLong())).thenReturn(semester);
        when(userService.getById(anyLong())).thenReturn(user);
        when(teacherMapper.teacherToTeacherDTO(any())).thenReturn(teacherDTO);
        when(scheduleRepository.getDaysWhenTeacherHasClassesBySemester(anyLong(), anyLong())).thenReturn(dayOfWeeks);
        doNothing().when(mailService).send(anyString(), anyString(), anyString(), anyString(), any());
        scheduleServiceImpl.sendScheduleToTeachers(4L, id, Locale.ENGLISH);
        verify(mailService, times(id.length)).send(anyString(), anyString(), anyString(), anyString(), any());
    }

    @Test
    public void sendScheduleToTeacher() throws MessagingException {
        User user = new User();
        user.setId(1L);
        user.setEmail("Test@gmail.com");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1L);
        Semester semester = new Semester();
        semester.setId(1L);
        TeacherDTO teacherDTO = new TeacherDTO();
        Map<EvenOdd, Map<DayOfWeek, List<TemporarySchedule>>> temporarySchedules = new HashMap<>();
        temporarySchedules.put(EvenOdd.EVEN, new HashMap<>());
        List<DayOfWeek> dayOfWeeks = new ArrayList<>();
        dayOfWeeks.add(DayOfWeek.MONDAY);
        dayOfWeeks.add(DayOfWeek.TUESDAY);
        when(teacherService.getById(1L)).thenReturn(teacher);
        when(semesterService.getById(1L)).thenReturn(semester);
        when(userService.getById(1L)).thenReturn(user);
        when(teacherMapper.teacherToTeacherDTO(any())).thenReturn(teacherDTO);
        when(scheduleRepository.getDaysWhenTeacherHasClassesBySemester(anyLong(), anyLong())).thenReturn(dayOfWeeks);
        doNothing().when(mailService).send(anyString(), anyString(), anyString(), anyString(), any());
        scheduleServiceImpl.sendScheduleToTeacher(1L, 1L, Locale.ENGLISH);
        verify(mailService, times(1)).send(anyString(), anyString(), anyString(), anyString(), any());
    }

    @Test
    public void scheduleForGroupedLessons() {
        Group group = new Group();
        Group group2 = new Group();
        Semester semester = new Semester();
        Lesson lesson = new Lesson();
        Lesson lesson2 = new Lesson();
        Period period = new Period();
        Room room = new Room();
        lesson.setId(1L);
        lesson.setSemester(semester);
        lesson.setGroup(group);
        lesson.setGrouped(true);
        lesson2.setId(2L);
        lesson2.setSemester(semester);
        lesson2.setGroup(group2);
        lesson2.setGrouped(true);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setLesson(lesson);
        schedule.setPeriod(period);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setRoom(room);
        when(lessonService.getAllGroupedLessonsByLesson(lesson)).thenReturn(Arrays.asList(lesson2, lesson));
        List<Schedule> schedules = scheduleServiceImpl.schedulesForGroupedLessons(schedule);
        assertEquals(2, schedules.size());
    }

    @Test
    public void getAllOrderedByRoomsDaysPeriodsTest() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room");
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setEvenOdd(EvenOdd.ODD);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setRoom(room);
        Room room2 = new Room();
        room.setId(2L);
        room.setName("Room2");
        Schedule schedule2 = new Schedule();
        schedule2.setId(2L);
        schedule2.setEvenOdd(EvenOdd.ODD);
        schedule2.setDayOfWeek(DayOfWeek.MONDAY);
        schedule2.setRoom(room2);
        when(scheduleRepository.getAllOrdered(1L)).thenReturn(List.of(schedule, schedule2));
        Map<Room, List<Schedule>> expected = new HashMap<>();
        expected.put(room2, List.of(schedule2));
        expected.put(room, List.of(schedule));
        Map<Room, List<Schedule>> actual = scheduleServiceImpl.getAllOrdered(1L);
        assertEquals(expected, actual);
        verify(scheduleRepository, times(1)).getAllOrdered(1L);
    }
}
