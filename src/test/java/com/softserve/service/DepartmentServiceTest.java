package com.softserve.service;

import com.softserve.entity.Department;
import com.softserve.entity.Teacher;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.DepartmentRepository;
import com.softserve.service.impl.DepartmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentServiceImpl service;

    private Department department;

    @Before
    public void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("some department");
    }

    @Test
    public void testGetAll() {
        List<Department> expected = Collections.singletonList(department);
        when(repository.getAll()).thenReturn(expected);

        List<Department> actual = service.getAll();

        assertThat(actual).hasSameSizeAs(expected).hasSameElementsAs(expected);
        verify(repository).getAll();
    }

    @Test
    public void testGetDisabled() {
        List<Department> expected = Collections.singletonList(department);
        when(repository.getDisabled()).thenReturn(expected);

        List<Department> actual = service.getDisabled();

        assertThat(actual).hasSameSizeAs(expected).hasSameElementsAs(expected);
        verify(repository).getDisabled();
    }

    @Test
    public void testDelete() {
        Department expected = department;
        when(repository.delete(expected)).thenReturn(expected);

        Department actual = service.delete(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(repository).delete(expected);
    }

    @Test
    public void testGetById() {
        Department expected = department;
        when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Department actual = service.getById(expected.getId());

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(repository).findById(expected.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfDepartmentNotFoundedById() {
        Long nonExistentId = 2L;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        service.getById(nonExistentId);
        verify(repository).findById(nonExistentId);
    }

    @Test
    public void testSave() {
        Department expected = department;
        when(repository.isExistsByName(expected.getName())).thenReturn(false);
        when(repository.save(expected)).thenReturn(expected);

        Department actual = service.save(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(repository).save(expected);
        verify(repository).isExistsByName(expected.getName());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionForNameOnSave() {
        when(repository.isExistsByName(department.getName())).thenReturn(true);
        service.save(department);
        verify(repository).isExistsByName(department.getName());
    }

    @Test
    public void update() {
        Department expected = department;
        when(repository.isExistsByNameIgnoringId(expected.getName(), expected.getId())).thenReturn(false);
        when(repository.update(expected)).thenReturn(expected);

        Department actual = service.update(expected);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(repository).update(expected);
        verify(repository).isExistsByNameIgnoringId(expected.getName(), expected.getId());
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionForNameOnUpdate() {
        when(repository.isExistsByNameIgnoringId(department.getName(), department.getId())).thenReturn(true);
        service.update(department);
        verify(repository).isExistsByNameIgnoringId(department.getName(), department.getId());
    }

    @Test
    public void testGetAllTeachers() {
        Teacher firstTeacher = new Teacher();
        firstTeacher.setName("Myroniuk");
        firstTeacher.setSurname("Ihor");
        firstTeacher.setPatronymic("Stepanovych");
        firstTeacher.setPosition("professor");

        Teacher secondTeacher = new Teacher();
        secondTeacher.setName("Adamovych");
        secondTeacher.setSurname("Svitlana");
        secondTeacher.setPatronymic("Petrivna");
        secondTeacher.setPosition("docent");

        List<Teacher> expectedTeachers = Arrays.asList(firstTeacher, secondTeacher);
        when(repository.getAllTeachers(3L)).thenReturn(expectedTeachers);

        List<Teacher> actualTeachers = service.getAllTeachers(3L);

        assertThat(actualTeachers).hasSameSizeAs(expectedTeachers).hasSameElementsAs(expectedTeachers);
        verify(repository).getAllTeachers(3L);
    }
}
