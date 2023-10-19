package com.softserve.mapper;

import com.softserve.dto.StudentDTO;
import com.softserve.dto.StudentImportDTO;
import com.softserve.dto.StudentWithoutGroupDTO;
import com.softserve.entity.Student;
import com.softserve.entity.User;
import com.softserve.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = GroupMapper.class)
public abstract class StudentMapper {
    @Autowired
    protected UserService userService;

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail")
    public abstract StudentDTO studentToStudentDTO(Student student);

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail")
    @Mapping(target = "groupDTO", source = "group")
    @Mapping(target = "importSaveStatus", ignore = true)
    public abstract StudentImportDTO studentToStudentImportDTO(Student student);

    @Named("userToEmail")
    public String userToEmail(User user) {
        if (user != null) {
            //get email
            return userService.getById(user.getId()).getEmail();
        }
        return null;
    }

    @Mapping(target = "user", source = "email", qualifiedByName = "emailToUser")
    @Mapping(target = "group", source = "groupDTO")
    public abstract Student studentImportDTOToStudent(StudentImportDTO studentImportDTO);

    @Mapping(target = "user", source = "email", qualifiedByName = "userToEmail")
    public abstract Student studentDTOToStudent(StudentDTO studentDTO);

    @Named("emailToUser")
    public User emailToUser(String email) {
        Optional<User> optionalUser = userService.findSocialUser(email);
        if (optionalUser.isPresent()) {
            return userService.findByEmail(email);
        }
        return null;
    }

    @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail")
    public abstract StudentWithoutGroupDTO studentToStudentWithoutGroupDTO(Student student);

    @Mapping(target = "user", source = "email", qualifiedByName = "userToEmail")
    @Mapping(target = "group", ignore = true)
    public abstract Student studentWithoutGroupDTOToStudent(StudentWithoutGroupDTO studentWithoutGroupDTO);

    public abstract List<StudentDTO> convertToDTOList(List<Student> studentList);

}
