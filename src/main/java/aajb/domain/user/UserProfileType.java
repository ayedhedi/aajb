package aajb.domain.user;

/**
 * Created by ayed.h on 22/02/2016.
 */
public enum UserProfileType {
    USER("USER"),
    DBA("DBA"),
    ADMIN("ADMIN");

    String userProfileType;

    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType(){
        return userProfileType;
    }

}