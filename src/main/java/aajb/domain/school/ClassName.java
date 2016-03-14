package aajb.domain.school;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayed.h on 14/03/2016.
 */
public enum ClassName {
    GA,GB,MA,MB,MC,PA,PB,PC,PD;

    public static ClassName getClassName(String className) {
        switch (className) {
            case "GA":return GA;
            case "GB":return GB;
            case "MA":return MA;
            case "MB":return MB;
            case "MC":return MC;
            case "PA":return PA;
            case "PB":return PB;
            case "PC":return PC;
            case "PD":return PD;
            default:return null;
        }
    }

}
