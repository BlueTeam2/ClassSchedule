package com.softserve.mapper;

import com.softserve.dto.SemesterDTO;
import com.softserve.dto.SemesterWithGroupsDTO;
import com.softserve.entity.Semester;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    SemesterDTO semesterToSemesterDTO(Semester semester);
    Semester semesterDTOToSemester(SemesterDTO semesterDTO);
    List<SemesterDTO> semestersToSemesterDTOs(List<Semester> semesters);
    SemesterWithGroupsDTO semesterToSemesterWithGroupsDTO(Semester semester);
}
