package aajb.service;

import aajb.dao.repository.ParentRepository;
import aajb.dao.repository.UserRepository;
import aajb.domain.school.Parent;
import aajb.domain.user.State;
import aajb.domain.user.UserProfileType;
import aajb.service.dto.ParentDto;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
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
    private ModelMapper modelMapper;


    @Override
    public ParentDto convertToDto(Parent parent) {
        return modelMapper.map(parent,ParentDto.class);
    }

    @Override
    public Parent convertFromDto(ParentDto parentDto) {
        return modelMapper.map(parentDto, Parent.class);
    }

    @Override
    public boolean createParent(ParentDto parentDto) {

        logger.info("Creating new Parent ... ");

        if (parentDto.getFirstName()==null || !Pattern.compile(env.getProperty("pattern.firstName"))
                .matcher(parentDto.getFirstName()).matches()) {
            logger.warn("Incorrect first name: "+parentDto.getFirstName());
            return false;
        }

        if (parentDto.getLastName()==null || !Pattern.compile(env.getProperty("pattern.lastName"))
                .matcher(parentDto.getLastName()).matches()) {
            logger.warn("Incorrect last name: "+parentDto.getLastName());
            return false;
        }

        if (parentDto.getLogin()==null || !Pattern.compile(env.getProperty("pattern.login"))
                .matcher(parentDto.getLogin()).matches()) {
            logger.warn("Incorrect login: "+parentDto.getLogin());
            return false;
        }

        if (parentDto.getPassword()==null || !Pattern.compile(env.getProperty("pattern.password"))
                .matcher(parentDto.getPassword()).matches()) {
            logger.warn("Incorrect password: "+parentDto.getPassword());
            return false;
        }

        if (parentDto.getEmail()==null || !Pattern.compile(env.getProperty("pattern.email"))
                .matcher(parentDto.getEmail()).matches()) {
            logger.warn("Incorrect email: "+parentDto.getEmail());
            return false;
        }

        if (isEmailUsed(parentDto.getEmail())) {
            logger.warn("Email in use: "+parentDto.getEmail());
            return false;
        }

        if (isLoginUser(parentDto.getLogin())) {
            logger.warn("Login in use: "+parentDto.getLogin());
            return false;
        }

        //encrypt the password
        String password = securityService.encryptPassword(parentDto.getPassword());

        //create the object
        Parent parent = new Parent();
        parent.setFirstName(parentDto.getFirstName());
        parent.setLastName(parentDto.getLastName());
        parent.setEmail(parentDto.getEmail());
        parent.setPassword(password);
        parent.setLogin(parentDto.getLogin());
        if (parentDto.getTel()!=null && !parentDto.getTel().isEmpty()) {
            parent.setTel(parentDto.getTel());
        }
        if (parentDto.getTelGsm()!=null && !parentDto.getTelGsm().isEmpty()) {
            parent.setTelGsm(parentDto.getTelGsm());
        }
        if (parentDto.getTelPro()!=null && !parentDto.getTelPro().isEmpty()) {
            parent.setTelPro(parentDto.getTelPro());
        }
        parent.setSsn(parentDto.getSsn());
        parent.setCaf(parentDto.getCaf());

        //set account locked ---> to be unlocked by the manager
        parent.setState(State.LOCKED.getState());
        //set user profile USER
        parent.setUserProfiles(new HashSet<>());
        parent.getUserProfiles().add(UserProfileType.USER);

        //save the object
        parent = parentRepository.save(parent);

        if (parent!=null) {
            logger.info("New Parent has been created and saved id="+parent.getId());
            return true;
        }else {
            logger.warn("Cannot save Object to Parent database !!");
            return false;
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

    @Override
    public Parent findByLogin(String login) {
        return parentRepository.findByLogin(login);
    }
}
