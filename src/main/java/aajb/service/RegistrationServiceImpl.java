package aajb.service;

import aajb.domain.school.Parent;
import aajb.domain.school.Registration;
import aajb.domain.school.RegistrationState;
import aajb.domain.school.Student;
import aajb.domain.school.payment.Cheque;
import aajb.repository.ParentRepository;
import aajb.repository.RegistrationRepository;
import aajb.repository.StudentRepository;
import aajb.service.dto.ChequeDto;
import aajb.service.dto.ParentDto;
import aajb.service.dto.RegistrationDto;
import aajb.service.dto.StudentDto;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private static final Logger logger = Logger.getLogger(RegistrationService.class.getSimpleName());

    @Qualifier("registrationRepository")
    @Autowired
    private RegistrationRepository registrationRepository;
    @Qualifier("parentService")
    @Autowired
    private ParentService parentService;
    @Qualifier("studentServiceImpl")
    @Autowired
    private StudentService studentService;
    @Qualifier("environment")
    @Autowired
    private Environment environment;
    @Qualifier("parentRepository")
    @Autowired
    private ParentRepository parentRepository;
    @Qualifier("studentRepository")
    @Autowired
    private StudentRepository studentRepository;


    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional(rollbackFor = {InvalidDataException.class, ParseException.class})
    public RegistrationDto createRegistration(RegistrationDto registrationDto) throws InvalidDataException {

        //save or read first parent
        Parent firstParent;
        if (registrationDto.getFirstParent() == null) {
            logger.warn("First parent is null !! --> throw exception ");
            throw new InvalidDataException(environment.getProperty("api.errorcode.firstParentNull"),
                    "First Parent Cannot be empty !!");
        }
        if (registrationDto.getFirstParent().getId() != null) {
            logger.info("Looking for first parent with id = "+registrationDto.getFirstParent().getId());
            firstParent = parentRepository.findOne(registrationDto.getFirstParent().getId());
            if (firstParent == null){
                logger.warn("Cannot find parent "+registrationDto.getFirstParent().getId());
                throw new InvalidDataException(environment.getProperty("api.errorcode.invalidPersonId"),"" +
                        "Cannot Find first Parent of id "+registrationDto.getFirstParent().getId());
            }
        }else {
            logger.info("Trying to create new first parent: "+registrationDto.getFirstParent());
            firstParent = parentService.createParent(ParentDto.asParent(registrationDto.getFirstParent()));
        }


        //save or read second parent if any !!
        Parent secondParent = null;
        if (registrationDto.getSecondParent() != null) {
            if (registrationDto.getSecondParent().getId() != null) {
                logger.info("Looking for second parent with id = "+registrationDto.getSecondParent().getId());
                secondParent = parentRepository.findOne(registrationDto.getSecondParent().getId());
                if (secondParent == null){
                    logger.warn("Cannot find parent "+registrationDto.getSecondParent().getId());
                    throw new InvalidDataException(environment.getProperty("api.errorcode.invalidPersonId"),"" +
                            "Cannot Find second Parent of id "+registrationDto.getSecondParent().getId());
                }
            }else {
                logger.info("Trying to create new first parent: "+registrationDto.getSecondParent());
                secondParent = parentService.createParent(ParentDto.asParent(registrationDto.getSecondParent()));
            }
        }

        //save/read students
        if (registrationDto.getStudents()== null ||registrationDto.getStudents().isEmpty()) {
            logger.warn("Registration with no students !! --> throw exception ");
            throw  new InvalidDataException(environment.getProperty("api.errorcode.registrationWithNoStudents"),"" +
                    "Cannot proceed registration with no students ");
        }
        Set<Student> students = new HashSet<>();
        for(StudentDto studentDto:registrationDto.getStudents()) {
            if (studentDto.getId() != null ){
                logger.info("Looking for a student with id "+studentDto.getId());
                Student student = studentRepository.findOne(studentDto.getId());
                if (student==null) {
                    logger.warn("Cannot find student "+studentDto.getId());
                    throw new InvalidDataException(environment.getProperty("api.errorcode.invalidPersonId"),"" +
                            "Cannot Find student of id "+studentDto.getId());
                }else {
                    students.add(student);
                }
            }else {
                logger.info("Trying to create new student: " + studentDto);
                Student student = StudentDto.asStudent(studentDto);
                student.setFirstParent(firstParent);
                if (secondParent != null) {
                    student.setSecondParent(secondParent);
                }
                try {
                    students.add(studentService.createStudent(student));
                } catch (ParseException e) {
                    logger.warn("Error where creating student: "+e.getMessage());
                    throw new InvalidDataException(environment.getProperty("api.errorcode.incorrectDate"),e.getMessage());
                }
            }
        }

        //find the logged user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //create the entity
        Registration registration = new Registration();

        //create cheques if any
        if (registrationDto.getCheques()!=null && !registrationDto.getCheques().isEmpty()){
            registration.setCheques(new ArrayList<>());

            for(ChequeDto chequeDto :registrationDto.getCheques()) {
                //checks the cheque amount
                if (chequeDto.getAmount() < 1 ) {
                    logger.warn("Cheque amount is invalid: "+ chequeDto.getAmount());
                    throw new InvalidDataException(environment.getProperty("api.errorcode.invalidCheckAmount"),
                            "Invalid cheque amount !!");
                }

                //set Date
                if (chequeDto.getAdjustableDate()==null) {
                    chequeDto.setAdjustableDate(new Date());
                }

                Cheque cheque = ChequeDto.toCheck(chequeDto);
                cheque.setRegistration(registration);
                registration.getCheques().add(cheque);
            }
        }

        //set the date & state
        registration.setDateTime(new Date());
        registration.setState(RegistrationState.WAITING);

        //set first parents and students
        registration.setFirstParent(firstParent);
        registration.setStudents(students);
        if (secondParent!=null) {
            registration.setSecondParent(secondParent);
        }

        //set the user name
        registration.setUserName(auth.getName());

        //save the entity
        registration = registrationRepository.save(registration);

        return RegistrationDto.asDto(registration);
    }

    @Override
    public List<RegistrationDto> findRegistrationsByParentId(int parentId) {
        List<Registration> registrationList = registrationRepository.findByParentsId(parentId);
        List<RegistrationDto> registrationDtoList = new ArrayList<>();
        registrationList.forEach(registration ->
            registrationDtoList.add(RegistrationDto.asDto(registration))
        );
        return registrationDtoList;
    }

    @Override
    public void deleteRegistration(int id) throws InvalidDataException {
        logger.info("Deleting registration with id:"+id);

        Registration registration = registrationRepository.findOne(id);
        if (registration == null){
            logger.warn("Cannot delete parent, incorrect id");
            throw new InvalidDataException(environment.getProperty("api.errorcode.invalidRegistrationId"),
                    "Cannot delete registration, invalid id: "+id);
        }

        registration.setState(RegistrationState.DELETED);
        registrationRepository.save(registration);
    }
}
