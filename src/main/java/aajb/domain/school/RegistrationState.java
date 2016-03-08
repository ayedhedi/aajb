package aajb.domain.school;

/**
 * Created by ayed.h on 01/03/2016.
 */
public enum RegistrationState {
    WAITING,
    ARCHIVED,
    DELETED,
    VALID;

    public RegistrationState getState(String state) {
        if ("WAITING".equalsIgnoreCase(state)) {
            return WAITING;
        }
        if ("ARCHIVED".equalsIgnoreCase(state)) {
            return ARCHIVED;
        }
        if ("DELETED".equalsIgnoreCase(state)) {
            return DELETED;
        }
        if ("VALID".equalsIgnoreCase(state)) {
            return VALID;
        }
        return null;
    }
}
