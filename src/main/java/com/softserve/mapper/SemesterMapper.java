package com.softserve.mapper;

import com.softserve.dto.SemesterDTO;
import com.softserve.dto.SemesterWithGroupsDTO;
import com.softserve.entity.Semester;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = GroupMapper.class)
public interface SemesterMapper {
    SemesterDTO semesterToSemesterDTO(Semester semester);

    @Mapping(target = "groups", ignore = true)
    Semester semesterDTOToSemester(SemesterDTO semesterDTO);

    Semester semesterWithGroupsDTOToSemester(SemesterWithGroupsDTO semesterWithGroupsDTO);

    @IterableMapping(elementTargetType = SemesterDTO.class)
    List<SemesterDTO> semestersToSemesterDTOs(List<Semester> semesters);

    List<SemesterWithGroupsDTO> semestersToSemesterWithGroupsDTOs(List<Semester> semesters);

    SemesterWithGroupsDTO semesterToSemesterWithGroupsDTO(Semester semester);
}
