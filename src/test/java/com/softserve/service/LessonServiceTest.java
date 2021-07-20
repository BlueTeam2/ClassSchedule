package com.softserve.service;

import com.softserve.entity.*;
import com.softserve.entity.enums.LessonType;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.repository.LessonRepository;
import com.softserve.service.impl.LessonServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherService teacherService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private SemesterService semesterService;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Test
    public void getLessonById() {
        Semester semester = new Semester();
        semester.setId(1L);
        semester.setCurrentSemester(true);
        semester.setPeriods(Set.of(new Period()));
        semester.setYear(2020);
        semester.setEndDay(LocalDate.of(2020, 1, 1));
        semester.setStartDay(LocalDate.of(2020, 12, 12));
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("link_1234_link");
        lesson.setSemester(semester);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Lesson result = lessonService.getById(1L);
        assertNotNull(result);
        assertEquals(lesson, result);
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfLessonNotFoundedById() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("Ivanov I.I.");

        lessonService.getById(2L);
        verify(lessonRepository, times(1)).findById(2L);
    }

    @Test
    public void saveLessonIfDuplicatesDoesNotExists() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("");
        lesson.setLinkToMeeting("");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(0L);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(subjectService.getById(subject.getId())).thenReturn(subject);

        Lesson result = lessonService.save(lesson);
        assertNotNull(result);
        assertEquals(lesson, result);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).save(lesson);
        verify(subjectService, times(1)).getById(subject.getId());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfSaveLessonWithSameTeacherSubjectGroupLessonType() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("Ivanov I.I.");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(1L);

        lessonService.save(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    public void updateLessonIfItDoesNotEqualsWithExistsLessons() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("Ivanov I.I.");

        when(lessonRepository.countLessonDuplicatesWithIgnoreId(lesson)).thenReturn(0L);
        when(lessonRepository.update(lesson)).thenReturn(lesson);

        Lesson result = lessonService.update(lesson);
        assertNotNull(result);
        assertEquals(lesson, result);

        verify(lessonRepository, times(1)).countLessonDuplicatesWithIgnoreId(lesson);
        verify(lessonRepository, times(1)).update(lesson);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void throwEntityAlreadyExistsExceptionIfUpdatedLessonEqualsWithExistsLessons() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUserId(1);
        teacher.setName("Ivan");
        teacher.setSurname("Ivanov");
        teacher.setPatronymic("Ivanovych");
        teacher.setPosition("Docent");
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Biology");
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setGroup(group);
        lesson.setTeacher(teacher);
        lesson.setSubject(subject);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("some link...");

        when(lessonRepository.countLessonDuplicatesWithIgnoreId(lesson)).thenReturn(1L);

        lessonService.update(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicatesWithIgnoreId(lesson);
        verify(lessonRepository, times(1)).update(lesson);
    }
}
