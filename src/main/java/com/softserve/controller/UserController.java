package com.softserve.controller;

import com.softserve.dto.ChangeUserPasswordDTO;
import com.softserve.dto.MessageDTO;
import com.softserve.dto.UserCreateDTO;
import com.softserve.dto.UserDTO;
import com.softserve.entity.CurrentUser;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import com.softserve.security.jwt.JwtUser;
import com.softserve.service.TeacherService;
import com.softserve.service.UserService;
import com.softserve.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
@Api(tags = "User API")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @ApiOperation(value = "Get the list of all users")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserDTO>> getAll() {
        log.info("Enter into getAll method");
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTOs(userService.getAll()));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserDTO> get(@PathVariable("id") Long id) {
        log.info("Enter into get method with id: {} ", id);
        User user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTO(user));
    }

    @PostMapping
    @ApiOperation(value = "Create new user")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserCreateDTO> save(@RequestBody UserCreateDTO createUserDTO) {
        log.info("Enter into save method with createUserDTO: {}", createUserDTO);
        User user = userService.save(userMapper.toUser(createUserDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserCreateDTO(user));
    }


    @PutMapping
    @ApiOperation(value = "Update existing user by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserCreateDTO> update(@RequestBody UserCreateDTO userDTO) {
        log.info("Enter into update method with userDTO: {}", userDTO);
        User updatedUser = userMapper.toUser(userDTO);
        User user = userService.getById(updatedUser.getId());
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.setRole(user.getRole());
        updatedUser.setToken(user.getToken());
        userService.update(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserCreateDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by id")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        log.info("Enter into delete method with group id: {}", id);
        User user = userService.getById(id);
        if (user.getRole() == Role.ROLE_TEACHER) {
            Teacher teacher = teacherService.findByUserId(user.getId().intValue());
            teacher.setUserId(null);
            teacherService.update(teacher);
        }
        userService.delete(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/with-role-user")
    @ApiOperation(value = "Get the list of all users, that have role User")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserDTO>> getAllUsersWithRoleUser() {
        log.info("Enter into getAllUsersWithRoleUser method");
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserDTOs(userService.getAllUsersWithRoleUser()));
    }

    @PutMapping("/change-password")
    @ApiOperation(value = "Change password for current user")
    public ResponseEntity changePasswordForCurrentUser(@CurrentUser JwtUser jwtUser,
                                                       @RequestBody ChangeUserPasswordDTO passwordDTO) {
        userService.changePassword(jwtUser.getId(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new MessageDTO("Password successfully changed."));
    }
}
