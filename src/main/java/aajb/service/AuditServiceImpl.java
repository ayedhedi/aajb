package aajb.service;

import aajb.domain.audit.Operation;
import aajb.domain.audit.OperationType;
import aajb.domain.school.Parent;
import aajb.repository.OperationRepository;
import aajb.repository.UserRepository;
import aajb.service.dto.RegistrationDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Component
@Aspect
public class AuditServiceImpl implements AuditService {


    @Qualifier("operationRepository")
    @Autowired
    private OperationRepository operationRepository;
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;


    @Override
    @Before("execution(* aajb.service.controller.MainController.login(String, String))")
    public void beforeLogin(JoinPoint jp) {
        Operation operation = new Operation();
        operation.setContent("Trying to get log-in for user "+jp.getArgs()[0]);
        operation.setTime(new Date());
        operation.setType(OperationType.LOG_IN);
        operation.setUser(null);
        operationRepository.save(operation);
    }

    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.controller.MainController.login(String, String))",
            returning = "result")
    public void afterLogin(JoinPoint jp, Object result) {
        HashMap map = (HashMap)result;

        Operation operation = new Operation();
        operation.setType(OperationType.LOG_IN);
        operation.setTime(new Date());

        if ("true".equals(map.get("status"))){
            operation.setContent("User "+jp.getArgs()[0]+" Logged-in successfully ");
            operation.setUser(userRepository.findByLogin(jp.getArgs()[0].toString()));
        }else {
            operation.setContent("User "+jp.getArgs()[0]+" Logged-in failed");
        }
        operationRepository.save(operation);
    }

    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.controller.MainController.logout(..))",
            returning = "result")
    public void afterLogout(JoinPoint jp, Object result) {
        HashMap map = (HashMap)result;
        Operation operation = new Operation();
        operation.setType(OperationType.LOG_IN);
        operation.setTime(new Date());
        if ("true".equals(map.get("status"))){
            operation.setContent("Successfully log-out ");
        }else {
            operation.setContent("Fail log-out ");
        }
        operationRepository.save(operation);
    }

    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.RegistrationServiceImpl.createRegistration(..))",
            returning = "result")
    public void afterCreateRegistration(JoinPoint jp, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegistrationDto registration = (RegistrationDto)result;

        Operation operation = new Operation();
        operation.setType(OperationType.CREATE);
        operation.setTime(new Date());
        operation.setContent("Creation of new Registration id="+registration.getId());
        operation.setUser(userRepository.findByLogin(auth.getName()));
        operationRepository.save(operation);
    }

    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.ParentServiceImpl.createParent(aajb.domain.school.Parent))",
            returning = "result"
    )
    public void afterCreatingParent(JoinPoint jp, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Parent parent = (Parent)result;

        Operation operation = new Operation();
        operation.setType(OperationType.CREATE);
        operation.setTime(new Date());
        operation.setContent("Creation of new Parent id="+parent.getId());
        operation.setUser(userRepository.findByLogin(auth.getName()));
        operationRepository.save(operation);
    }

    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.ParentServiceImpl.readParents(..))"
    )
    public void afterReadingParents(JoinPoint jp) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operation operation = new Operation();
        operation.setType(OperationType.READ);
        operation.setTime(new Date());
        operation.setContent("Read Parents");
        operation.setUser(userRepository.findByLogin(auth.getName()));
        operationRepository.save(operation);
    }
}
