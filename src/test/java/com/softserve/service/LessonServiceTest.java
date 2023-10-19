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
import java.util.ArrayList;
import java.util.List;
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
    private SubjectService subjectService;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Mock
    private SemesterService semesterService;

    @Test
    public void getLessonById() {
        Semester semester = new Semester();
        semester.setId(4L);
        semester.setCurrentSemester(true);
        semester.setPeriods(Set.of(new Period()));
        semester.setYear(2020);
        semester.setEndDay(LocalDate.of(2020, 2, 20));
        semester.setStartDay(LocalDate.of(2020, 1, 20));
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHours(1);
        lesson.setLessonType(LessonType.LECTURE);
        lesson.setSubjectForSite("Human anatomy");
        lesson.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");
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
        lesson.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");

        lessonService.getById(2L);
        verify(lessonRepository, times(1)).findById(2L);
    }

    @Test
    public void saveLessonIfDuplicatesDoesNotExists() {
        Group group = new Group();
        group.setId(1L);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        teacher.setUserId(1L);
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
        teacher.setId(10L);
        teacher.setUserId(1L);
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
        lesson.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");

        when(lessonRepository.countLessonDuplicates(lesson)).thenReturn(1L);

        lessonService.save(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicates(lesson);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    public void updateLessonIfItDoesNotEqualsWithExistsLessons() {
        Group group = new Group();
        group.setId(1L);
        group.setDisable(false);
        group.setTitle("group");
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        teacher.setDisable(false);
        teacher.setUserId(1L);
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
        lesson.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");

        Semester semester = new Semester();
        semester.setId(4L);
        semester.setCurrentSemester(true);
        semester.setPeriods(Set.of(new Period()));
        semester.setYear(2020);
        semester.setEndDay(LocalDate.of(2020, 2, 20));
        semester.setStartDay(LocalDate.of(2020, 1, 20));
        when(semesterService.getCurrentSemester()).thenReturn(semester);

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
        teacher.setId(10L);
        teacher.setUserId(1L);
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
        lesson.setLinkToMeeting("https://softserveinc.zoom.us/j/93198369163?pwd=Rk1GU281cDFtK1FCK3pJWXphRkJrQT09");

        when(lessonRepository.countLessonDuplicatesWithIgnoreId(lesson)).thenReturn(1L);

        lessonService.update(lesson);
        verify(lessonRepository, times(1)).countLessonDuplicatesWithIgnoreId(lesson);
        verify(lessonRepository, times(1)).update(lesson);
    }

    @Test
    public void updateLinkToMeeting() {

        Semester semester = new Semester();
        semester.setId(7L);
        Teacher teacher = new Teacher();
        teacher.setId(5L);
        Subject subject = new Subject();
        subject.setId(5L);

        Lesson lessonWithSubjectAndType = new Lesson();
        lessonWithSubjectAndType.setLinkToMeeting("https://www.youtube.com/");
        lessonWithSubjectAndType.setLessonType(LessonType.LECTURE);
        lessonWithSubjectAndType.setSemester(semester);
        lessonWithSubjectAndType.setTeacher(teacher);
        lessonWithSubjectAndType.setSubject(subject);

        Lesson lessonWithSubject = new Lesson();
        lessonWithSubject.setLinkToMeeting("https://www.youtube.com/");
        lessonWithSubject.setSemester(semester);
        lessonWithSubject.setTeacher(teacher);
        lessonWithSubject.setSubject(subject);

        Lesson lesson = new Lesson();
        lesson.setLinkToMeeting("https://www.youtube.com/");
        lesson.setSemester(semester);
        lesson.setTeacher(teacher);

        List<Integer> expectedResults = List.of(2, 3, 4);

        when(lessonRepository.updateLinkToMeeting(lessonWithSubjectAndType)).thenReturn(2);
        when(lessonRepository.updateLinkToMeeting(lessonWithSubject)).thenReturn(3);
        when(lessonRepository.updateLinkToMeeting(lesson)).thenReturn(4);

        List<Integer> actualResults = new ArrayList<>();
        actualResults.add(lessonService.updateLinkToMeeting(lessonWithSubjectAndType));
        actualResults.add(lessonService.updateLinkToMeeting(lessonWithSubject));
        actualResults.add(lessonService.updateLinkToMeeting(lesson));

        assertEquals(expectedResults, actualResults);
        verify(lessonRepository).updateLinkToMeeting(lessonWithSubjectAndType);
        verify(lessonRepository).updateLinkToMeeting(lessonWithSubject);
        verify(lessonRepository).updateLinkToMeeting(lesson);
    }
}
