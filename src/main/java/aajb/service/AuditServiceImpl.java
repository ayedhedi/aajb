package aajb.service;

import aajb.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by ayed.h on 01/03/2016.
 */
@Component
@Aspect
public class AuditServiceImpl implements AuditService {


    @Override
    @AfterReturning(
            pointcut = "execution(* aajb.service.controller.MainController.login(..))",
            returning = "result")
    public void afterLogin(JoinPoint jp, Object result) {

    }
}
