package com.softserve.mapper;

import com.softserve.dto.*;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
@MapperConfig(uses = DepartmentMapper.class)
public abstract class TeacherMapper {
    @Autowired
    protected UserService userService;

    public static String teacherDTOToTeacherForSite(TeacherDTO teacherDTO) {
        return StringUtils.join(
                teacherDTO.getPosition(), " ",
                teacherDTO.getSurname(), " ",
                teacherDTO.getName().charAt(0), ". ",
                teacherDTO.getPatronymic().charAt(0), "."
        );
    }

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "userId", qualifiedByName = "userIdToEmail")
    public abstract TeacherDTO teacherToTeacherDTO(Teacher teacher);

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "userId", qualifiedByName = "userIdToEmail")
    @Mapping(target = "importSaveStatus", ignore = true)
    public abstract TeacherImportDTO teacherToTeacherImportDTO(Teacher teacher);

    @Named("userIdToEmail")
    public String userIdToEmail(Long userId) {
        if (userId != null) {
            return userService.getById(userId).getEmail();
        }
        return null;
    }

    @Named("emailToUserId")
    public Long emailToUser(String email) {
        return userService.findSocialUser(email).map(User::getId).orElse(null);
    }

    @Mapping(target = "department", source = "departmentDTO")
    @Mapping(target = "userId", source = "email", qualifiedByName = "emailToUserId")
    public abstract Teacher teacherForUpdateDTOToTeacher(TeacherForUpdateDTO teacherForUpdateDTO);

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "userId", qualifiedByName = "userIdToEmail")
    public abstract TeacherForUpdateDTO teacherToTeacherForUpdateDTO(Teacher teacher);

    @Mapping(target = "department", source = "departmentDTO")
    @Mapping(target = "userId", source = "email", qualifiedByName = "emailToUserId")
    public abstract Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);

    @Mapping(target = "department", source = "departmentDTO")
    @Mapping(target = "disable", ignore = true)
    @Mapping(target = "userId", source = "email", qualifiedByName = "emailToUserId")
    public abstract Teacher teacherImportDTOToTeacher(TeacherImportDTO teacherImportDTO);

    public abstract TeacherWithUserDTO toTeacherWithUserDTO(Teacher teacher);

    @Mapping(target = "teacherId", source = "id")
    @Mapping(target = "teacherName", source = "name")
    @Mapping(target = "teacherSurname", source = "surname")
    @Mapping(target = "teacherPatronymic", source = "patronymic")
    @Mapping(target = "teacherPosition", source = "position")
    @Mapping(target = "teacherDepartmentDTO", source = "department")
    public abstract UserDataDTO teacherToUserDataDTO(Teacher teacher);

    public abstract List<TeacherDTO> teachersToTeacherDTOs(List<Teacher> teachers);

}
