package aajb.domain.school;

/**
 * Created by ayed.h on 22/02/2016.
 */
public enum Gender {
    MALE,FEMALE;

    public static Gender getGender(String gender) {
        if (gender!=null && gender.compareToIgnoreCase("MALE")==0)
            return MALE;
        return FEMALE;
    }
}
