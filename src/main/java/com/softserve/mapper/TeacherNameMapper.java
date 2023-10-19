package com.softserve.mapper;

import com.softserve.dto.TeacherNameDTO;
import com.softserve.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherNameMapper {
    TeacherNameDTO teacherDTOToTeacher(Teacher teacher);

    @Mapping(target = "position", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "disable", ignore = true)
    Teacher teacherToTeacherDTO(TeacherNameDTO teacherNameDTO);
}
