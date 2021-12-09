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
import com.softserve.mapper.StudentMapperNew;
import com.softserve.repository.StudentRepository;
import com.softserve.service.GroupService;
import com.softserve.service.StudentService;
import com.softserve.service.UserService;
import com.softserve.util.CsvFileParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final StudentMapperNew studentMapper;
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapperNew studentMapper,
                              GroupService groupService, UserService userService,
                              GroupMapper groupMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
    }

    /**
     * The method used for getting Student by id
     *
     * @param id Identity Student id
     * @return target Student
     * @throws EntityNotFoundException if Student with id doesn't exist
     */
    @Transactional(readOnly = true)
    @Override
    public Student getById(Long id) {
        log.info("Enter into getById method with id {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Student.class, "id", id.toString()));
    }

    /**
     * Method gets information about Students from Repository
     *
     * @return List of all Students
     */
    @Transactional(readOnly = true)
    @Override
    public List<Student> getAll() {
        log.info("Enter into getAll method with no input params");
        return studentRepository.getAll();
    }

    /**
     * Method creates new Student in Repository
     *
     * @param object Student entity with info to be created
     * @return created Student entity
     * @throws FieldAlreadyExistsException if Student with input email already exists
     */
    @Transactional
    @Override
    public Student save(Student object) {
        log.info("Enter into save method with entity:{}", object);
        return studentRepository.save(object);
    }

    /**
     * Method save information for student in Repository and register user if email exists
     * @param studentDTO StudentDTO instance
     * @return saved Student entity
     */
    @Transactional
    @Override
    public Student save(StudentDTO studentDTO) {
        log.info("Enter into save method with studentDTO:{}", studentDTO);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        if(isEmailNullOrEmpty(studentDTO.getEmail())){
            return save(student);
        }
        if(student.getUser() != null && isEmailInUse(studentDTO.getEmail())) {
            throwFieldAlreadyExist(student);
        }
        return save(registerStudent(student, studentDTO.getEmail()));
    }

    /**
     * Method updates information for an existing Student in Repository
     *
     * @param object Student entity with info to be updated
     * @return updated Student entity
     * @throws FieldAlreadyExistsException if Student with input email already exists
     */
    @SneakyThrows
    @Transactional
    @Override
    public Student update(Student object) {
        log.info("Enter into update method with entity:{}", object);
        return studentRepository.update(object);
    }

    @Transactional
    @Override
    public Student update(StudentDTO studentDTO) {
        log.info("Enter into update method with studentDTO:{}", studentDTO);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        if (isEmailNullOrEmpty(studentDTO.getEmail())) {
            throw new FieldNullException(Student.class, "email");
        }
        if(student.getUser() != null) {
            boolean test2 = isEmailInUse(studentDTO.getEmail());
            boolean test3 = isEmailForThisStudent(studentDTO.getEmail(), studentDTO.getId());

//            boolean test = checkStudent(studentDTO.getEmail(), studentDTO.getId());
//            if(student.getUser() != null && !test)

            if(student.getUser() != null && test2 && !test3)

            {
                return throwFieldAlreadyExist(student);
            }
        }
        Optional<User> userOptional = userService.findSocialUser(studentDTO.getEmail());
        if(userOptional.isPresent()){
            User user = userService.findByEmail(studentDTO.getEmail());
            student.setUser(user);
            return update(student);
        }
        return update(registerStudent(student, studentDTO.getEmail()));
    }

//    private boolean checkStudent(String email, Long id) {
//        //findIdByEmail and return long
//        Optional<Student> optionalStudent = studentRepository.findByEmail(email);
//        if (optionalStudent.isPresent()){
//            if(Objects.equals(optionalStudent.get().getId(), id)){
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Method deletes an existing Student from Repository
     *
     * @param object Student entity to be deleted
     * @return deleted Student entity
     */
    @Transactional
    @Override
    public Student delete(Student object) {
        log.info("Enter into delete method with entity:{}", object);
        return studentRepository.delete(object);
    }

    /**
     * This asynchronous method used for importing students from csv file.
     * Each line of the file should consist of four fields, separated by commas.
     * Each field may or may not be enclosed in double-quotes.
     * First line of the file is a header.
     * All subsequent lines contain data about students.
     * <p>
     * "surname","name","patronymic","email"
     * "Romaniuk","Hanna","Stepanivna","romaniuk@gmail.com"
     * "Boichuk","Oleksandr","Ivanovych","boichuk@ukr.net"
     * etc.
     * <p>
     * The method is not transactional in order to prevent interruptions while saving a student
     *
     * @param file file with students data
     * @return list of created students.
     * If the student in the returned list have a non-null value of the group title then he already existed.
     * If the student in the returned list have a null value of the group title then he saved as a new student.
     * If the student in the returned list have a null value of the group then he didn't pass a validation.
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
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
            Optional<User> userOptional = userService.findSocialUser(student.getEmail());
            Student newStudent = studentMapper.studentImportDTOToStudent(student);
            Optional<Student> studentFromBase = studentRepository.getExistingStudent(newStudent);

            Group group = groupService.getById(groupId);

            if (userOptional.isEmpty() && studentFromBase.isEmpty()) {
                return registerAndSaveNewStudent(student, newStudent, group);
            } else if (userOptional.isEmpty()) {
                return registerUserAndUpdateStudent(student, studentFromBase);
            } else if (studentFromBase.isEmpty()) {
                return assignUserToNewStudent(student, userOptional, newStudent, group);
            } else {
                return checkForEmptyFieldsOfExistingStudent(student, userOptional, studentFromBase);
            }
        } catch (ConstraintViolationException e) {
            student.setImportSaveStatus(ImportSaveStatus.VALIDATION_ERROR);
            log.error("Error occurred while saving student with email {}", student.getEmail(), e);
            return student;
        } catch (ImportRoleConflictException ex) {
            student.setImportSaveStatus(ImportSaveStatus.ROLE_CONFLICT);
            log.error("User with current email has another ROLE");
            return student;
        }
    }

    /**
     * The method used for register provided user and save provided teacher
     *
     * @param student    our student from file
     * @param newStudent our student which we will save to database
     * @param group      group which provided from server
     */
    private StudentImportDTO registerAndSaveNewStudent(StudentImportDTO student, Student newStudent, Group group) {
        log.debug("Enter to method if email and student DONT EXIST");

        Student registeredStudent = registerStudent(newStudent, student.getEmail());
        return saveStudentAndSetEmailGroupStatus(student, group, registeredStudent);
    }

    /**
     * The method used for register provided user and update existed teacher
     *
     * @param student         our student from file
     * @param studentFromBase our student from dataBase
     */
    private StudentImportDTO registerUserAndUpdateStudent(StudentImportDTO student, Optional<Student> studentFromBase) {
        log.debug("Enter to method if email DONT EXIST and student EXIST");
        if (studentFromBase.isPresent()) {

            Student ourStudentFromBase = getById(studentFromBase.get().getId());
            Student registeredStudent1 = registerStudent(ourStudentFromBase, student.getEmail());
            if (ourStudentFromBase.getUser() == null) {
                registeredStudent1.setUser(registeredStudent1.getUser());
            }
            studentRepository.update(registeredStudent1);
            StudentImportDTO savedStudent = studentMapper.studentToStudentImportDTO(registeredStudent1);
            savedStudent.setEmail(student.getEmail());
            savedStudent.setGroupDTO(groupMapper.groupToGroupDTO(registeredStudent1.getGroup()));
            savedStudent.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
            return savedStudent;
        }
        return null;
    }

    /**
     * The method used for assigning existing user to provided new teacher
     *
     * @param student      our student from file
     * @param userOptional our user from database
     * @param newStudent   our student which we will save to database
     * @param group        group which provided from server
     */
    private StudentImportDTO assignUserToNewStudent(StudentImportDTO student, Optional<User> userOptional, Student newStudent, Group group) {
        log.debug("Enter to method if email EXIST and student DONT EXIST");
        if (userOptional.isPresent() && userOptional.get().getRole() == Role.ROLE_STUDENT) {

            newStudent.setUser(userOptional.get());
            return saveStudentAndSetEmailGroupStatus(student, group, newStudent);
        } else {
            throw new ImportRoleConflictException("User with current Email has another ROLE");
        }
    }

    /**
     * The method used for register provided user and save provided teacher
     *
     * @param student         our student from file
     * @param studentFromBase our student from dataBase
     * @param userOptional    our user from database
     */
    private StudentImportDTO checkForEmptyFieldsOfExistingStudent(StudentImportDTO student,
                                                                  Optional<User> userOptional,
                                                                  Optional<Student> studentFromBase) {
        log.debug("Enter to method if email EXIST and student EXIST");
        if (userOptional.isPresent() && studentFromBase.isPresent() && userOptional.get().getRole() == Role.ROLE_STUDENT) {

            Student ourStudentFromBase = getById(studentFromBase.get().getId());
            if (ourStudentFromBase.getUser() == null) {
                ourStudentFromBase.setUser(userOptional.get());
                studentRepository.update(ourStudentFromBase);
            }
            StudentImportDTO existedStudent = studentMapper.studentToStudentImportDTO(ourStudentFromBase);
            existedStudent.setImportSaveStatus(ImportSaveStatus.ALREADY_EXIST);
            existedStudent.setEmail(student.getEmail());
            existedStudent.setGroupDTO(groupMapper.groupToGroupDTO(ourStudentFromBase.getGroup()));
            log.error("Student with current email exist ",
                    new FieldAlreadyExistsException(Student.class, "email", student.getEmail()));
            return existedStudent;
        } else {
            throw new ImportRoleConflictException("User with current Email has another ROLE");
        }
    }

    /**
     * The method used check if email Null or Empty
     * @param email our email from studentDTO
     */
    private boolean isEmailNullOrEmpty(String email) {
        return email == null || email.isEmpty();
    }

    /**
     * The method used check if students from DB has this email
     * @param email our email from studentDTO
     */
    private boolean isEmailInUse(String email){return studentRepository.isEmailInUse(email);}

    /**
     * The method used check if provided student from DB has this email
     * @param email our email from studentDTO
     */
    private boolean isEmailForThisStudent(String email, Long id) {
        return  studentRepository.isEmailForThisStudent(email,id);}

    /**
     * The method for register new user with provided email and set user_id to provided student
     * @param email our email from studentDTO
     * @param student is our provided student
     */
    private Student registerStudent(Student student, String email) {
        log.info("Enter into registerStudent method with student {} and email:{}", student, email);
        User registeredUserForStudent = userService.registerAutomatic(email, Role.ROLE_STUDENT);
        student.setUser(registeredUserForStudent);
        return student;
    }

    /**
     * The method throws an exception if student with provided email exist
     * @param student is student with email
     */
    private Student throwFieldAlreadyExist(Student student) {
        throw new FieldAlreadyExistsException(Student.class, "email", student.getUser().getEmail());
    }

    /**
     * The method used for save new student with registered/found user and set fields to studentDTO
     * @param student is provided studentImportDTO from file
     * @param group is provided group from server
     * @param registeredStudent is our student that we're going to save
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
