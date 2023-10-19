package com.softserve.service;

import com.softserve.dto.SubjectWithTypeDTO;
import com.softserve.entity.Subject;
import com.softserve.entity.enums.LessonType;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.SubjectRepository;
import com.softserve.service.impl.SubjectServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void getSubjectById() {
        Subject subject = new Subject();
        subject.setName("some subject");
        subject.setId(1L);

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Subject result = subjectService.getById(1L);
        assertNotNull(result);
        assertEquals(subject.getId(), result.getId());
        verify(subjectRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfSubjectNotFounded() {
        Subject subject = new Subject();
        subject.setName("some subject");

        subjectService.getById(2L);
        verify(subjectRepository, times(1)).findById(2L);
    }

    @Test
    public void saveSubjectIfNameDoesNotExists() {
        Subject subject = new Subject();
        subject.setName("some subject");

        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
        when(subjectRepository.countSubjectsWithName(anyString())).thenReturn(0L);

        Subject result = subjectService.save(subject);
        assertNotNull(result);
        assertEquals(subject.getName(), result.getName());
        verify(subjectRepository, times(1)).save(subject);
        verify(subjectRepository, times(1)).countSubjectsWithName(anyString());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionIfNameAlreadyExists() {
        Subject subject = new Subject();
        subject.setName("some subject");

        when(subjectRepository.countSubjectsWithName(anyString())).thenReturn(1L);

        subjectService.save(subject);
        verify(subjectRepository, times(1)).save(subject);
        verify(subjectRepository, times(1)).countSubjectsWithName(anyString());
    }

    @Test
    public void updateSubjectIfNameDoesNotExists() {
        Subject oldSubject = new Subject();
        oldSubject.setName("some subject");
        oldSubject.setId(1L);
        Subject updatedSubject = new Subject();
        updatedSubject.setName("updated name");
        updatedSubject.setId(1L);

        when(subjectRepository.countBySubjectId(anyLong())).thenReturn(1L);
        when(subjectRepository.countSubjectsWithNameAndIgnoreWithId(anyLong(), anyString())).thenReturn(0L);
        when(subjectRepository.update(updatedSubject)).thenReturn(updatedSubject);

        oldSubject = subjectService.update(updatedSubject);
        assertNotNull(oldSubject);
        assertEquals(updatedSubject, oldSubject);
        verify(subjectRepository, times(1)).update(oldSubject);
        verify(subjectRepository, times(1)).countBySubjectId(anyLong());
        verify(subjectRepository, times(1)).countSubjectsWithNameAndIgnoreWithId(anyLong(), anyString());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionIfUpdatedNameAlreadyExists() {
        Subject oldSubject = new Subject();
        oldSubject.setName("some subject");
        oldSubject.setId(1L);
        Subject updatedSubject = new Subject();
        updatedSubject.setName("updated name");
        updatedSubject.setId(1L);

        when(subjectRepository.countBySubjectId(anyLong())).thenReturn(1L);
        when(subjectRepository.countSubjectsWithNameAndIgnoreWithId(anyLong(), anyString())).thenReturn(1L);

        subjectService.update(updatedSubject);
        verify(subjectRepository, times(1)).countBySubjectId(anyLong());
        verify(subjectRepository, times(1)).countSubjectsWithNameAndIgnoreWithId(anyLong(), anyString());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfUpdatedSubjectNotFounded() {
        Subject subject = new Subject();
        subject.setName("some subject");
        subject.setId(1L);

        when(subjectRepository.countBySubjectId(anyLong())).thenReturn(0L);

        subjectService.update(subject);
        verify(subjectRepository, times(1)).countBySubjectId(anyLong());
    }

    @Test
    public void getSubjectsWithTypes() {

        Subject firstSubject = new Subject();
        firstSubject.setName("Organic chemistry");
        firstSubject.setId(1L);

        Subject secondSubject = new Subject();
        secondSubject.setName("Economics");
        secondSubject.setId(1L);

        SubjectWithTypeDTO firstSubjectWithType = new SubjectWithTypeDTO(firstSubject, LessonType.LECTURE);
        SubjectWithTypeDTO secondSubjectWithType = new SubjectWithTypeDTO(secondSubject, LessonType.PRACTICAL);

        List<SubjectWithTypeDTO> expectedSubjectsWithTypes = List.of(firstSubjectWithType, secondSubjectWithType);

        when(subjectRepository.getSubjectsWithTypes(1L, 3L)).thenReturn(expectedSubjectsWithTypes);

        List<SubjectWithTypeDTO> actualSubjectsWithTypes = subjectService.getSubjectsWithTypes(1L, 3L);
        assertNotNull(actualSubjectsWithTypes);
        assertEquals(expectedSubjectsWithTypes, actualSubjectsWithTypes);
        verify(subjectRepository).getSubjectsWithTypes(1L, 3L);
    }
}
