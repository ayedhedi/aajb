package aajb.service;

/**
 * Created by ayed.h on 24/02/2016.
 */
public interface SecurityService {
    String encryptPassword(String password);
    boolean isMatches(String password, String encoded);
}
