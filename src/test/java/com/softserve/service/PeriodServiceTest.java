package com.softserve.service;

import com.softserve.entity.Period;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.exception.IncorrectTimeException;
import com.softserve.exception.PeriodConflictException;
import com.softserve.repository.PeriodRepository;
import com.softserve.service.impl.PeriodServiceImpl;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Category(UnitTestCategory.class)
@RunWith(MockitoJUnitRunner.class)
public class PeriodServiceTest {
    @Mock
    private PeriodRepository periodRepository;

    @InjectMocks
    private PeriodServiceImpl periodService;

    @Test
    public void getPeriodById() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));

        when(periodRepository.findById(1L)).thenReturn(Optional.of(period));

        Period result = periodService.getById(1L);
        assertNotNull(result);
        assertEquals(period, result);
        verify(periodRepository, times(1)).findById(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void throwEntityNotFoundExceptionIfPeriodNotFoundedById() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));

        periodService.getById(2L);
        verify(periodRepository, times(1)).findById(anyLong());
    }

    @Test
    public void savePeriodIfItHasCorrectTimeAndNotIntersectsWithOtherPeriodsAndNameIsNotExist() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Another period");
        periodInList.setStartTime(LocalTime.parse("05:00:00"));
        periodInList.setEndTime(LocalTime.parse("06:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);
        when(periodRepository.save(period)).thenReturn(period);

        Period result = periodService.save(period);
        assertNotNull(result);
        assertEquals(period, result);
        verify(periodRepository, times(1)).getAll();
        verify(periodRepository, times(1)).save(period);
    }

    @Test(expected = IncorrectTimeException.class)
    public void throwIncorrectTimeExceptionIfSavePeriodBeginsAfterHisEnd() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("05:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));

        periodService.save(period);
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwFieldAlreadyExistsExceptionIfSavePeriodWithExistName() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("01:00:00"));
        period.setEndTime(LocalTime.parse("02:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Some period");
        periodInList.setStartTime(LocalTime.parse("03:00:00"));
        periodInList.setEndTime(LocalTime.parse("04:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);
        when(periodRepository.findByName(anyString())).thenReturn(Optional.of(period));

        periodService.save(period);
        verify(periodRepository, times(1)).getAll();
        verify(periodRepository, times(1)).findByName(anyString());
    }

    @Test(expected = PeriodConflictException.class)
    public void throwPeriodConflictExceptionIfSavedPeriodIntersectsWithOtherPeriods() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("01:00:00"));
        period.setEndTime(LocalTime.parse("02:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Some period");
        periodInList.setStartTime(LocalTime.parse("02:00:00"));
        periodInList.setEndTime(LocalTime.parse("03:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);

        periodService.save(period);
        verify(periodRepository, times(1)).getAll();
    }

    @Test
    public void saveListOfPeriodsIfAllPeriodsHaveCorrectTimeNotIntersectWithOtherPeriodsAndNamesAreNotExist() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));
        Period anotherPeriod = new Period();
        anotherPeriod.setId(2L);
        anotherPeriod.setName("another period");
        anotherPeriod.setStartTime(LocalTime.parse("01:00:00"));
        anotherPeriod.setEndTime(LocalTime.parse("02:00:00"));
        List<Period> periodListForSave = new ArrayList<>();
        periodListForSave.add(period);
        List<Period> periodList = new ArrayList<>();
        periodList.add(anotherPeriod);

        when(periodRepository.getAll()).thenReturn(periodList);
        when(periodRepository.save(period)).thenReturn(period);
        periodList.add(period);

        List<Period> result = periodService.save(periodListForSave);
        assertNotNull(result);
        assertTrue(periodList.containsAll(result));
        verify(periodRepository, times(1)).getAll();
        verify(periodRepository, times(1)).save(period);
    }

    @Test(expected = IncorrectTimeException.class)
    public void throwIncorrectTimeExceptionIfSaveListOfPeriodsAndOneOfThemHasIncorrectTime() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("05:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(period);

        periodService.save(periodList);
    }

    @Test(expected = PeriodConflictException.class)
    public void throwPeriodConflictExceptionIfSaveListOfPeriodsAndOneOfThemIntersectsWithOtherPeriod() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("02:00:00"));
        period.setEndTime(LocalTime.parse("03:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Some period");
        periodInList.setStartTime(LocalTime.parse("03:00:00"));
        periodInList.setEndTime(LocalTime.parse("04:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);
        List<Period> saveList = new ArrayList<>();
        saveList.add(period);

        when(periodRepository.getAll()).thenReturn(periodList);

        periodService.save(saveList);
        verify(periodRepository, times(1)).getAll();
    }

    @Test
    public void updatePeriodIfItHasCorrectTimeAndNotIntersectsWithOtherPeriodsAndNameIsNotExist() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Another period");
        periodInList.setStartTime(LocalTime.parse("06:00:00"));
        periodInList.setEndTime(LocalTime.parse("07:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);
        when(periodRepository.findByName(period.getName())).thenReturn(Optional.of(period));
        when(periodRepository.findById(period.getId())).thenReturn(Optional.of(period));
        when(periodRepository.update(any(Period.class))).thenReturn(period);

        periodInList = periodService.update(period);
        assertNotNull(periodInList);
        assertEquals(period, periodInList);
        verify(periodRepository, times(1)).getAll();
        verify(periodRepository, times(1)).findById(anyLong());
        verify(periodRepository, times(1)).update(period);
    }

    @Test(expected = FieldAlreadyExistsException.class)
    public void throwPeriodConflictExceptionIfUpdatedPeriodsNameAlreadyExist() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("03:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Another period");
        periodInList.setStartTime(LocalTime.parse("06:00:00"));
        periodInList.setEndTime(LocalTime.parse("07:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);
        when(periodRepository.findByName(period.getName())).thenReturn(Optional.of(periodInList));
        when(periodRepository.findById(period.getId())).thenReturn(Optional.of(period));

        periodService.update(period);
        verify(periodRepository, times(1)).getAll();
        verify(periodRepository, times(1)).findByName(anyString());
        verify(periodRepository, times(1)).findById(anyLong());
    }

    @Test(expected = IncorrectTimeException.class)
    public void throwIncorrectTimeExceptionIfUpdatedPeriodBeginsAfterHisEnd() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("05:00:00"));
        period.setEndTime(LocalTime.parse("04:00:00"));

        periodService.update(period);
    }

    @Test(expected = PeriodConflictException.class)
    public void throwPeriodConflictExceptionIfUpdatedPeriodIntersectsWithOther() {
        Period period = new Period();
        period.setId(1L);
        period.setName("Some period");
        period.setStartTime(LocalTime.parse("02:00:00"));
        period.setEndTime(LocalTime.parse("03:30:00"));
        Period periodInList = new Period();
        periodInList.setId(2L);
        periodInList.setName("Another period");
        periodInList.setStartTime(LocalTime.parse("03:00:00"));
        periodInList.setEndTime(LocalTime.parse("04:00:00"));
        List<Period> periodList = new ArrayList<>();
        periodList.add(periodInList);

        when(periodRepository.getAll()).thenReturn(periodList);

        periodService.update(period);
        verify(periodRepository, times(1)).getAll();
    }
}
