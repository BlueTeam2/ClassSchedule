package com.softserve.mapper;

import com.softserve.dto.StudentDTO;
import com.softserve.dto.StudentImportDTO;
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

@Mapper(componentModel = "spring")
public abstract class StudentMapperNew {
    @Autowired
    protected UserService userService;

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail")
    public abstract StudentDTO studentToStudentDTO(Student student);

    @InheritInverseConfiguration
    @Mapping(target = "email", source = "user", qualifiedByName = "userToEmail")
    public abstract StudentImportDTO studentToStudentImportDTO(Student student);

    @Named("userToEmail")
    public String userToEmail(User user) {
        if(user != null) {
            return userService.getById(user.getId()).getEmail();
        }
        return null;
    }

    @Mapping(target = "user", source = "email", qualifiedByName = "userToEmail")
    public abstract Student studentImportDTOToStudent(StudentImportDTO studentImportDTO);

    @Mapping(target = "user", source = "email", qualifiedByName = "userToEmail")
    public abstract Student studentDTOToStudent(StudentDTO studentDTO);

    @Named("emailToUser")
    public User emailToUser(String email) {
        Optional<User> optionalUser = userService.findSocialUser(email);
        if(optionalUser.isPresent()) {
            return userService.findByEmail(email);
        }
        return null;
    }

    public abstract List<StudentDTO> convertToDTOList(List<Student> studentList);

}
