package aajb.service;

import aajb.domain.school.Parent;
import aajb.domain.school.RegistrationState;
import aajb.domain.user.State;
import aajb.repository.ParentRepository;
import aajb.repository.RegistrationRepository;
import aajb.repository.UserRepository;
import aajb.service.dto.ParentDto;
import aajb.service.exceptions.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Service(value = "parentService")
@Transactional
public class ParentServiceImpl implements ParentService {
    private static final Logger logger = Logger.getLogger(ParentServiceImpl.class.getSimpleName());

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private Environment env;


    @Override
    @PreAuthorize("isAuthenticated()")
    public Parent createParent(Parent parent) throws InvalidDataException{

        logger.info("Creating new Parent ... "+parent.getFirstName());

        if (parent.getFirstName()==null || !Pattern.compile(env.getProperty("pattern.firstName"))
                .matcher(parent.getFirstName()).matches()) {
            logger.warn("Incorrect first name: "+parent.getFirstName());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidFirstName"),"Invalid First Name");
        }

        if (parent.getLastName()==null || !Pattern.compile(env.getProperty("pattern.lastName"))
                .matcher(parent.getLastName()).matches()) {
            logger.warn("Incorrect last name: "+parent.getLastName());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidLastName"),"Invalid Last Name");
        }

        if (parent.getEmail()==null || !Pattern.compile(env.getProperty("pattern.email"))
                .matcher(parent.getEmail()).matches()) {
            logger.warn("Incorrect email: "+parent.getEmail());
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidEmail"),"Invalid email");
        }

        if (parentRepository.findByEmail(parent.getEmail()).isPresent()) {
            logger.warn("Email in use: "+parent.getEmail());
            throw new InvalidDataException(env.getProperty("api.errorcode.emailAlreadyInUse"),"email used");
        }

        //set active
        parent.setActive(true);

        //save the object
        parent = parentRepository.save(parent);

        if (parent!=null) {
            logger.info("New Parent has been created and saved id="+parent.getId());
            return parent;
        }else {
            logger.warn("Cannot save Object to Parent database !!");
            throw new InvalidDataException(env.getProperty("api.errorcode.internalError"),"Internal Error");
        }
    }

    @Override
    public ParentDto createParent(ParentDto parentDto) throws InvalidDataException {
        Parent parent = ParentDto.asParent(parentDto);
        parent = createParent(parent);
        return ParentDto.asParentDto(parent);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<ParentDto> readParents() {
        logger.info("looking for all parents");
        Iterable<Parent> parents = parentRepository.findAll();
        List<ParentDto> parentDtos = new ArrayList<>();
        parents.forEach(parent ->
            parentDtos.add(ParentDto.asParentDto(parent))
        );

        return parentDtos;
    }

    @Override
    public List<ParentDto> readParents(int page, int size) throws InvalidDataException {
        logger.info("looking for page "+page+" of size "+size);
        if (page<0 || page > getNumberOfPage(size)) {
            throw new InvalidDataException();
        }

        List<ParentDto> parentDtos = new ArrayList<>();
        Page<Parent> parentPage = parentRepository.findAll(new PageRequest(page, size));
        parentPage.forEach(parent ->
            parentDtos.add(ParentDto.asParentDto(parent))
        );

        return parentDtos;
    }

    @Override
    public int getNumberOfPage(int pageSize) throws InvalidDataException {
        if (pageSize<1) {
            throw new InvalidDataException((env.getProperty("api.errorcode.invalidPageSize")),
                    "page size cannot be less than 1: "+pageSize);
        }
        long count = parentRepository.count();
        return pageSize==count?1:((int)(count/pageSize) + 1);
    }

    @Override
    public Parent findParentByEmail(String email) {
        Optional<Parent> op = parentRepository.findByEmail(email);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public Parent deleteParent(int id) throws InvalidDataException{
        logger.info("Deleting parent with id = "+id);

        Parent parent = parentRepository.findOne(id);
        if (parent == null) {
            logger.warn("Cannot delete parent, incorrect id");
            throw new InvalidDataException(env.getProperty("api.errorcode.invalidPersonId"),
                    "Cannot delete parent, incorrect id: "+id);
        }

        //delete the person
        parent.setActive(false);
        parent = parentRepository.save(parent);

        return parent;
    }

    @Override
    public List<ParentDto> findParents(String match) {
        logger.info("looking for parent: "+match);
        match = "%"+match+"%";
        List<Parent> finds = parentRepository.findByFirstNameLikeOrLastNameLike(match, match);
        logger.info(finds.size()+" parents was find ");
        List<ParentDto> parentDtoList = new ArrayList<>();
        finds.stream().forEach(parent -> parentDtoList.add(ParentDto.asParentDto(parent)));
        return parentDtoList;
    }


}
