package com.softserve.service.impl;

import com.softserve.dto.StudentDTO;
import com.softserve.dto.StudentImportDTO;
import com.softserve.dto.enums.ImportSaveStatus;
import com.softserve.entity.Group;
import com.softserve.entity.Student;
import com.softserve.entity.User;
import com.softserve.entity.enums.Role;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.exception.FieldNullException;
import com.softserve.exception.ImportRoleConflictException;
import com.softserve.mapper.GroupMapper;
import com.softserve.mapper.StudentMapper;
import com.softserve.repository.StudentRepository;
import com.softserve.service.GroupService;
import com.softserve.service.StudentService;
import com.softserve.service.UserService;
import com.softserve.util.Constants;
import com.softserve.util.CsvFileParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final GroupService groupService;
    private final UserService userService;

    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper,
                              GroupService groupService, UserService userService,
                              GroupMapper groupMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Student getById(Long id) {
        log.info("Enter into getById method with id {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Student.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Student> getAll() {
        log.info("Enter into getAll method with no input params");
        return studentRepository.getAll();
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if student with input email already exists
     */
    @Transactional
    @Override
    public Student save(Student object) {
        log.info("Enter into save method with entity:{}", object);
        return studentRepository.save(object);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Student save(StudentDTO studentDTO) {
        log.info("Enter into save method with studentDTO:{}", studentDTO);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        if (isEmailNullOrEmpty(studentDTO.getEmail())) {
            throw new FieldNullException(StudentDTO.class, Constants.EMAIL);
        }
        Optional<User> optionalUser = userService.findSocialUser(studentDTO.getEmail());
        if (optionalUser.isPresent() && isEmailInUse(studentDTO.getEmail())) {
            throw new FieldAlreadyExistsException(Student.class, Constants.EMAIL, studentDTO.getEmail());
        }
        return save(registerStudent(student, studentDTO.getEmail()));
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if student with input email already exists
     */
    @SneakyThrows
    @Transactional
    @Override
    public Student update(Student object) {
        log.info("Enter into update method with entity:{}", object);
        return studentRepository.update(object);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Student update(StudentDTO studentDTO) {
        log.info("Enter into update method with studentDTO:{}", studentDTO);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        if (isEmailNullOrEmpty(studentDTO.getEmail())) {
            throw new FieldNullException(StudentDTO.class, Constants.EMAIL);
        }
        if (!studentRepository.isIdPresent(student.getId())) {
            throw new EntityNotFoundException(Student.class, Constants.EMAIL, studentDTO.getEmail());
        }
        Optional<User> userOptional = userService.findSocialUser(studentDTO.getEmail());
        if (userOptional.isEmpty()) {
            return update(registerStudent(student, studentDTO.getEmail()));
        }
        if (!studentRepository.isEmailForThisStudent(studentDTO.getEmail(), student.getId())) {
            throw new FieldAlreadyExistsException(Student.class, Constants.EMAIL, studentDTO.getEmail());
        }
        student.setUser(userOptional.get());
        return update(student);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Student delete(Student object) {
        log.info("Enter into delete method with entity:{}", object);
        return studentRepository.delete(object);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This asynchronous method used for importing students from csv file.
     * Each line of the file should consist of four fields, separated by commas.
     * Each field may or may not be enclosed in double-quotes.
     * First line of the file is a header.
     * All subsequent lines contain data about students.
     * <p>
     * "surname","name","patronymic",EMAIL
     * "Romanian","Hanna","Stepanov","romaniuk@gmail.com"
     * "Bochum","Oleksandr","Ivanov","boichuk@ukr.net"
     * etc.
     * <p>
     * The method is not transactional in order to prevent interruptions while saving a student.
     * <p>
     * If the student in the returned list have a non-null value of the group title then he already existed.
     * If the student in the returned list have a null value of the group title then he saved as a new student.
     * If the student in the returned list have a null value of the group then he didn't pass a validation.
     */
    @Override
    @Transactional
    @Async
    public CompletableFuture<List<StudentImportDTO>> saveFromFile(MultipartFile file, Long groupId) {
        log.info("Enter into saveFromFile of StudentServiceImpl with groupId {}", groupId);

        List<StudentImportDTO> students = CsvFileParser.getStudentsFromFile(file);

        List<StudentImportDTO> savedStudents = new ArrayList<>();

        for (StudentImportDTO student : students) {
            StudentImportDTO test = saveStudentFromFile(groupId, student);
            savedStudents.add(test);
        }
        return CompletableFuture.completedFuture(savedStudents);
    }

    public StudentImportDTO saveStudentFromFile(Long groupId, StudentImportDTO student) {
        try {
            if (student.getEmail() == null || student.getEmail().isEmpty()) {
                log.error("Empty or null email: {}", student.getEmail());
                student.setImportSaveStatus(ImportSaveStatus.VALIDATION_ERROR);
                return student;
            }

            Optional<User> userOptional = userService.findSocialUser(student.getEmail());
            Student newStudent = studentMapper.studentImportDTOToStudent(student);
            Optional<Student> studentFromBase = studentRepository.getExistingStudent(newStudent);

            Group group = groupService.getById(groupId);

            if (userOptional.isEmpty() && studentFromBase.isEmpty()) {
                return registerAndSaveNewStudent(student, newStudent, group);
            }
            if (studentFromBase.isEmpty()) {
                return assignUserToNewStudent(student, userOptional, newStudent, group);
            }
            return checkForEmptyFieldsOfExistingStudent(student, userOptional, studentFromBase);
        } catch (ConstraintViolationException e) {
            student.setImportSaveStatus(ImportSaveStatus.VALIDATION_ERROR);
            log.error("VALIDATION_ERROR while saving student with email {}", student.getEmail(), e);
            return student;
        } catch (ImportRoleConflictException ex) {
            student.setImportSaveStatus(ImportSaveStatus.ROLE_CONFLICT);
            log.error("User with current email has another ROLE");
            return student;
        }
    }

    /**
     * Registers provided user and save provided student.
     *
     * @param student    the student imported from file
     * @param newStudent our student which we will save to database
     * @param group      group which provided from server
     * @return the saved student
     */
    private StudentImportDTO registerAndSaveNewStudent(StudentImportDTO student, Student newStudent, Group group) {
        log.debug("Enter to method if email and student DONT EXIST");

        Student registeredStudent = registerStudent(newStudent, student.getEmail());
        return saveStudentAndSetEmailGroupStatus(student, group, registeredStudent);
    }

    /**
     * Assigns existing user to provided new student.
     *
     * @param student      the student from file
     * @param userOptional the Optional describing the user provided from database
     * @param newStudent   the student which we will save to database
     * @param group        the group which provided from server
     * @return the saved student
     * @throws ImportRoleConflictException if user with current email has another role in the system
     */
    private StudentImportDTO assignUserToNewStudent(StudentImportDTO student, Optional<User> userOptional, Student newStudent, Group group) {
        log.debug("Enter to method if email EXIST and student DONT EXIST");
        if (userOptional.isPresent() && userOptional.get().getRole() == Role.ROLE_STUDENT) {
            if (isEmailInUse(student.getEmail())) {
                log.error("Student with current email exist ",
                        new FieldAlreadyExistsException(Student.class, Constants.EMAIL, student.getEmail()));
                student.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
                return student;
            }
            newStudent.setUser(userOptional.get());
            return saveStudentAndSetEmailGroupStatus(student, group, newStudent);
        } else {
            throw new ImportRoleConflictException("User with current Email has another ROLE");
        }
    }

    /**
     * Checks for empty fields of existing student.
     *
     * @param student         our student from file
     * @param studentFromBase our student from dataBase
     * @param userOptional    our user from database
     * @return the existed student
     * @throws FieldAlreadyExistsException if student with current email exist
     * @throws ImportRoleConflictException if user with current email has another role in the system
     */
    private StudentImportDTO checkForEmptyFieldsOfExistingStudent(StudentImportDTO student,
                                                                  Optional<User> userOptional,
                                                                  Optional<Student> studentFromBase) {
        log.debug("Enter to method if email EXIST and student EXIST");
        if (userOptional.isPresent() && studentFromBase.isPresent() && userOptional.get().getRole() == Role.ROLE_STUDENT) {
            Student ourStudentFromBase = getById(studentFromBase.get().getId());
            StudentImportDTO existedStudent = studentMapper.studentToStudentImportDTO(ourStudentFromBase);
            existedStudent.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
            existedStudent.setEmail(student.getEmail());
            existedStudent.setGroupDTO(groupMapper.groupToGroupDTO(ourStudentFromBase.getGroup()));
            log.error("Student with current email exist ",
                    new FieldAlreadyExistsException(Student.class, Constants.EMAIL, student.getEmail()));
            return existedStudent;
        } else {
            throw new ImportRoleConflictException("User with current Email has another ROLE");
        }
    }

    /**
     * Checks if email Null or Empty.
     *
     * @param email the string represents the email from studentDTO
     * @return {@code true} if the given email is null or empty
     */
    private boolean isEmailNullOrEmpty(String email) {
        return email == null || email.isEmpty();
    }

    /**
     * Checks if any student in the repository has the given email.
     *
     * @param email the string represents the email from studentDTO
     * @return {@code true} if given email is used, otherwise {@code false}
     */
    private boolean isEmailInUse(String email) {
        return studentRepository.isEmailInUse(email);
    }

    /**
     * Registers new user with provided email and set user_id to provided student.
     *
     * @param email   the string represents the email from studentDTO
     * @param student the student
     * @return the updated student
     */
    private Student registerStudent(Student student, String email) {
        log.info("Enter into registerStudent method with student {} and email:{}", student, email);
        User registeredUserForStudent = userService.automaticRegistration(email, Role.ROLE_STUDENT);
        student.setUser(registeredUserForStudent);
        return student;
    }

    /**
     * Saves new student with registered/found user and set fields to studentDTO.
     *
     * @param student           the provided studentImportDTO from file
     * @param group             the provided group from server
     * @param registeredStudent the student that to be saved
     * @return the saved student
     */
    private StudentImportDTO saveStudentAndSetEmailGroupStatus(StudentImportDTO student,
                                                               Group group, Student registeredStudent) {
        registeredStudent.setGroup(group);
        studentRepository.save(registeredStudent);
        StudentImportDTO savedStudent = studentMapper.studentToStudentImportDTO(registeredStudent);
        savedStudent.setEmail(student.getEmail());
        savedStudent.setGroupDTO(groupMapper.groupToGroupDTO(registeredStudent.getGroup()));
        savedStudent.setImportSaveStatus(ImportSaveStatus.SAVED);
        return savedStudent;
    }
}
