package aajb.service;

import aajb.domain.school.Parent;
import aajb.domain.user.State;
import aajb.domain.user.UserProfileType;
import aajb.repository.ParentRepository;
import aajb.repository.UserRepository;
import aajb.service.dto.ParentDto;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Service(value = "parentService")
@Transactional
public class ParentServiceImpl implements ParentService {
    private static final Logger logger = Logger.getLogger(ParentServiceImpl.class.getSimpleName());

    @Qualifier("parentRepository")
    @Autowired
    private ParentRepository parentRepository;
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;
    @Qualifier("securityService")
    @Autowired
    private SecurityServiceImpl securityService;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public ParentDto createParent(ParentDto parentDto) throws InvalidDataException{

        logger.info("Creating new Parent ... "+parentDto.getFirstName());

        if (parentDto.getFirstName()==null || !Pattern.compile(env.getProperty("pattern.firstName"))
                .matcher(parentDto.getFirstName()).matches()) {
            logger.warn("Incorrect first name: "+parentDto.getFirstName());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidFirstName"),"Invalid First Name");
        }

        if (parentDto.getLastName()==null || !Pattern.compile(env.getProperty("pattern.lastName"))
                .matcher(parentDto.getLastName()).matches()) {
            logger.warn("Incorrect last name: "+parentDto.getLastName());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidLastName"),"Invalid Last Name");
        }

        if (parentDto.getLogin()==null || !Pattern.compile(env.getProperty("pattern.login"))
                .matcher(parentDto.getLogin()).matches()) {
            logger.warn("Incorrect login: "+parentDto.getLogin());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidLogin"),"Invalid Login");
        }

        if (parentDto.getPassword()==null || !Pattern.compile(env.getProperty("pattern.password"))
                .matcher(parentDto.getPassword()).matches()) {
            logger.warn("Incorrect password: "+parentDto.getPassword());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidPassword"),"Invalid Password");
        }

        if (parentDto.getEmail()==null || !Pattern.compile(env.getProperty("pattern.email"))
                .matcher(parentDto.getEmail()).matches()) {
            logger.warn("Incorrect email: "+parentDto.getEmail());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidEmail"),"Invalid email");
        }

        if (isEmailUsed(parentDto.getEmail())) {
            logger.warn("Email in use: "+parentDto.getEmail());
            throw new InvalidDataException(env.getProperty("api.errorcode.emailAlreadyInUse"),"email used");
        }

        if (isLoginUser(parentDto.getLogin())) {
            logger.warn("Login in use: "+parentDto.getLogin());
            throw new InvalidDataException(env.getProperty("api.errorcode.loginAlreadyInUse"),"login used");
        }

        //generate activation code
        String code = securityService.generateMailCode();

        //encrypt the password
        String password = securityService.encryptPassword(parentDto.getPassword());

        //create the object
        Parent parent = ParentDto.asParent(parentDto);
        parent.setPassword(password);
        parent.setActivationCode(code);

        //set account locked ---> to be unlocked by the manager
        parent.setState(State.LOCKED.getState());
        //set user profile USER
        parent.setUserProfiles(new HashSet<>());
        parent.getUserProfiles().add(UserProfileType.USER);

        //save the object
        parent = parentRepository.save(parent);

        if (parent!=null) {
            logger.info("New Parent has been created and saved id="+parent.getId());
            parentDto.setId(parent.getId().toString());
            parentDto.setPassword(null);

            //generate activation code
            sendEmail("ayed.h@sfeir.lu",code);

            return parentDto;
        }else {
            logger.warn("Cannot save Object to Parent database !!");
            throw new InvalidDataException(env.getProperty("api.errorcode.internalError"),"Internal Error");
        }
    }

    @Async
    private synchronized void sendEmail(String destination,String code) {
        SimpleMailMessage simpleEmail = new SimpleMailMessage();
        simpleEmail.setFrom(env.getProperty("mail.from"));
        simpleEmail.setTo(destination);
        simpleEmail.setSubject(env.getProperty("mail.title"));
        simpleEmail.setText(env.getProperty("mail.content").replaceAll("@code",code));

        try {
            javaMailSender.send(simpleEmail);
        }catch (Exception e) {
            logger.warn("Cannot send activation email: "+e.getMessage());
        }
    }

    @Override
    public boolean isEmailUsed(String email) {
        return userRepository.findByEmail(email)!=null;
    }

    @Override
    public boolean isLoginUser(String login) {
        return userRepository.findByLogin(login)!=null;
    }

}
