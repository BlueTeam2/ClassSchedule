package com.softserve.service.impl;

import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherForUpdateDTO;
import com.softserve.dto.TeacherImportDTO;
import com.softserve.dto.enums.ImportSaveStatus;
import com.softserve.entity.Department;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import com.softserve.exception.EntityAlreadyExistsException;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.exception.FieldNullException;
import com.softserve.mapper.TeacherMapper;
import com.softserve.repository.TeacherRepository;
import com.softserve.service.DepartmentService;
import com.softserve.service.MailService;
import com.softserve.service.TeacherService;
import com.softserve.service.UserService;
import com.softserve.util.CsvFileParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final MailService mailService;
    private final DepartmentService departmentService;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, UserService userService, MailService mailService,
                              TeacherMapper teacherMapper, DepartmentService departmentService) {
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.teacherMapper = teacherMapper;
        this.departmentService = departmentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getById(Long id) {
        log.info("Enter into getById of TeacherServiceImpl with id {}", id);
        return teacherRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Teacher.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAll() {
        log.info("Enter into getAll()");
        return teacherRepository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher save(Teacher teacher) {
        log.info("Enter into save method with entity:{}", teacher);
        return teacherRepository.save(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher save(TeacherDTO teacherDTO) {
        log.info("Enter into save method with dto:{}", teacherDTO);
        Teacher teacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        if (isEmailNullOrEmpty(teacherDTO.getEmail())) {
            return save(teacher);
        }
        return save(registerTeacher(teacher, teacherDTO.getEmail()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher update(TeacherForUpdateDTO teacherForUpdateDTO) {
        log.info("Enter into update method with dto:{}", teacherForUpdateDTO);
        Teacher teacher = teacherMapper.teacherForUpdateDTOToTeacher(teacherForUpdateDTO);
        if (isEmailNullOrEmpty(teacherForUpdateDTO.getEmail())) {
            return update(teacher);
        }
        Long userId = getById(teacherForUpdateDTO.getId()).getUserId();
        if (userId != null) {
            teacher.setUserId(userId);
            updateEmailInUserForTeacher(teacherForUpdateDTO.getEmail(), userId);
            return update(teacher);
        }
        return update(registerTeacher(teacher, teacherForUpdateDTO.getEmail()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher update(Teacher teacher) {
        log.info("Enter into update method with entity:{}", teacher);
        return teacherRepository.update(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher delete(Teacher teacher) {
        log.info("Enter into delete method with entity:{}", teacher);
        if (teacher.getUserId() != null) {
            User user = userService.getById(teacher.getUserId());
            user.setRole(Role.ROLE_USER);
            userService.update(user);
        }
        return teacherRepository.delete(teacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getDisabled() {
        log.info("Enter into getAll of getDisabled");
        return teacherRepository.getDisabled();
    }

    /**
     * The method used for join Teacher and User.
     *
     * @param teacherId Long teacherId used to find Teacher by it
     * @param userId    Long userId used to find User by it
     * @return Teacher entity
     * @throws EntityAlreadyExistsException when user already exist in some teacher/manager or teacher contains some userId
     */
    @Override
    public Teacher joinTeacherWithUser(Long teacherId, Long userId) {
        log.info("Enter into joinTeacherWithUser method with teacherId {} and userId:{}", teacherId, userId);
        User user = userService.getById(userId);
        Teacher getTeacher = getById(teacherId);

        if (user.getRole() != Role.ROLE_USER || getTeacher.getUserId() != null) {
            throw new EntityAlreadyExistsException("You cannot doing this action.");
        }

        getTeacher.setUserId(userId);
        user.setRole(Role.ROLE_TEACHER);
        userService.update(user);

        String message = "Hello, " + user.getEmail() + ".\n" +
                "You received this email, because you now have all the teacher rights in the system.\n" +
                "Congratulations!";
        String subject = "You - Teacher";
        mailService.send(user.getEmail(), subject, message);

        return update(getTeacher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher findByUserId(Long userId) {
        log.info("Enter into getByUserId with userId {}", userId);
        if (userId == null) {
            throw new FieldNullException(Teacher.class, "userId");
        }
        return teacherRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(Teacher.class, "userId", String.valueOf(userId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAllTeacherWithoutUser() {
        log.info("Enter into getAllTeacherWithoutUser of TeacherServiceImpl");
        return teacherRepository.getAllTeacherWithoutUser();
    }

    private Teacher registerTeacher(Teacher teacher, String email) {
        log.info("Enter into registerTeacher method with teacher {} and email:{}", teacher, email);
        User registeredUserForTeacher = userService.automaticRegistration(email, Role.ROLE_TEACHER);
        teacher.setUserId(registeredUserForTeacher.getId());
        return teacher;
    }

    private void updateEmailInUserForTeacher(String email, long userId) {
        log.info("Enter into updateEmailInUserForTeacher method with email {} and userId:{}", email, userId);
        User userForTeacher = userService.getById(userId);
        userForTeacher.setEmail(email);
        userService.update(userForTeacher);
    }

    private boolean isEmailNullOrEmpty(String email) {
        return email == null || email.isEmpty();
    }

    /**
     * {@inheritDoc}
     * Each line of the file should consist of five fields, separated by commas without spaceBar.
     * First line of the file is a header.
     * All subsequent lines contain data about teachers.
     * <p>
     * name,surname,patronymic,position,email
     * Test1,Test1,Test1,test1,test1@gmail.com
     * Test2,Test2,Test2,test2,test2@gmail.com
     * etc.
     */
    @Override
    public List<TeacherImportDTO> saveFromFile(MultipartFile file, Long departmentId) {
        log.info("Enter into saveFromFile of TeacherServiceImpl with departmentId {}", departmentId);

        List<TeacherImportDTO> teachers = CsvFileParser.getTeachersFromFile(file);

        return teachers.stream().map(teacher -> saveTeacher(departmentId, teacher)).collect(Collectors.toList());
    }

    public TeacherImportDTO saveTeacher(Long departmentId, TeacherImportDTO teacher) {
        try {

            Optional<User> userOptional = userService.findSocialUser(teacher.getEmail());
            Teacher newTeacher = teacherMapper.teacherImportDTOToTeacher(teacher);
            Optional<Teacher> teacherFromBase = teacherRepository.getExistingTeacher(newTeacher);

            Department department = departmentService.getById(departmentId);

            if (userOptional.isEmpty() && teacherFromBase.isEmpty()) {
                return registerAndSaveNewTeacher(teacher, newTeacher, department);
            } else if (userOptional.isEmpty()) {
                return registerUserAndUpdateTeacher(teacher, teacherFromBase, department);
            } else if (teacherFromBase.isEmpty()) {
                return assignUserToNewTeacher(teacher, userOptional, newTeacher, department);
            } else {
                return checkForEmptyFieldsOfExistingTeacher(teacher, userOptional, teacherFromBase, department);
            }
        } catch (ConstraintViolationException e) {
            teacher.setImportSaveStatus(ImportSaveStatus.VALIDATION_ERROR);
            log.error("Error occurred while saving teacher with email {}", teacher.getEmail(), e);
            return teacher;

        }
    }

    /**
     * Assigns existing user to provided new teacher.
     *
     * @param teacher      the teacher from file
     * @param userOptional the user from database
     * @param newTeacher   the teacher which we will save to the repository
     * @param department   department which provided from server
     * @return the saved teacher or {code null} if given optional does not contain user
     */
    private TeacherImportDTO assignUserToNewTeacher(TeacherImportDTO teacher, Optional<User> userOptional,
                                                    Teacher newTeacher, Department department) {
        log.debug("Enter to method if email EXIST and teacher DONT EXIST");
        if (userOptional.isPresent()) {

            newTeacher.setUserId(userOptional.get().getId());
            newTeacher.setDepartment(department);
            teacherRepository.save(newTeacher);
            TeacherImportDTO savedTeacher = teacherMapper.teacherToTeacherImportDTO(newTeacher);
            savedTeacher.setEmail(teacher.getEmail());
            savedTeacher.setImportSaveStatus(ImportSaveStatus.SAVED);
            return savedTeacher;
        }
        return null;
    }

    /**
     * Registers provided user and update existed teacher.
     *
     * @param teacher         the teacher from file
     * @param teacherFromBase the teacher from dataBase
     * @param department      department which provided from server
     * @return the saved teacher or {@code null} if given optional does not contain teacher
     */
    private TeacherImportDTO registerUserAndUpdateTeacher(TeacherImportDTO teacher, Optional<Teacher> teacherFromBase, Department department) {
        log.debug("Enter to method if email DONT EXIST and teacher EXIST");
        if (teacherFromBase.isPresent()) {

            Teacher ourTeacherFromBase = getById(teacherFromBase.get().getId());
            Teacher registeredTeacher1 = registerTeacher(ourTeacherFromBase, teacher.getEmail());
            if (ourTeacherFromBase.getDepartment() == null) {
                registeredTeacher1.setDepartment(department);
            }
            if (ourTeacherFromBase.getUserId() == null) {
                registeredTeacher1.setUserId(registeredTeacher1.getUserId());
            }
            teacherRepository.update(registeredTeacher1);
            TeacherImportDTO savedTeacher = teacherMapper.teacherToTeacherImportDTO(registeredTeacher1);
            savedTeacher.setEmail(teacher.getEmail());
            savedTeacher.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
            return savedTeacher;
        }
        return null;
    }

    /**
     * Registers provided user and save provided teacher.
     *
     * @param teacher    the teacher from file
     * @param newTeacher the teacher which we will save to database
     * @param department department which provided from server
     * @return saved teacher
     */
    private TeacherImportDTO registerAndSaveNewTeacher(TeacherImportDTO teacher, Teacher newTeacher, Department department) {
        log.debug("Enter to method if email and teacher DONT EXIST");

        Teacher registeredTeacher = registerTeacher(newTeacher, teacher.getEmail());
        registeredTeacher.setDepartment(department);
        teacherRepository.save(registeredTeacher);
        TeacherImportDTO savedTeacher = teacherMapper.teacherToTeacherImportDTO(registeredTeacher);
        savedTeacher.setEmail(teacher.getEmail());
        savedTeacher.setImportSaveStatus(ImportSaveStatus.SAVED);
        return savedTeacher;
    }

    /**
     * Registers provided user and save provided teacher.
     *
     * @param teacher         the teacher from file
     * @param teacherFromBase the teacher from dataBase
     * @param userOptional    the user from database
     * @param department      the department which provided from server
     * @return the existed teacher of {@code null} if given optional does not contain user
     */
    private TeacherImportDTO checkForEmptyFieldsOfExistingTeacher(TeacherImportDTO teacher, Optional<User> userOptional,
                                                                  Optional<Teacher> teacherFromBase, Department department) {
        log.debug("Enter to method if email EXIST and teacher EXIST");
        if (userOptional.isPresent() && teacherFromBase.isPresent()) {

            Teacher ourTeacherFromBase = getById(teacherFromBase.get().getId());
            if (ourTeacherFromBase.getDepartment() == null || ourTeacherFromBase.getUserId() == null) {
                if (ourTeacherFromBase.getDepartment() == null) {
                    ourTeacherFromBase.setDepartment(department);
                }
                if (ourTeacherFromBase.getUserId() == null) {
                    ourTeacherFromBase.setUserId(userOptional.get().getId());
                }
                teacherRepository.update(ourTeacherFromBase);
            }
            TeacherImportDTO existedTeacher = teacherMapper.teacherToTeacherImportDTO(ourTeacherFromBase);
            existedTeacher.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
            log.error("Teacher with current email exist ", new FieldAlreadyExistsException(Teacher.class, "email", teacher.getEmail()));
            return existedTeacher;
        }
        return null;
    }

}
