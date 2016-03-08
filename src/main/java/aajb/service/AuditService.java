package aajb.service;

import org.aspectj.lang.JoinPoint;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface AuditService {
    void beforeLogin(JoinPoint jp);
    void afterLogin(JoinPoint jp,Object result);
    void afterLogout(JoinPoint jp, Object result);
}
