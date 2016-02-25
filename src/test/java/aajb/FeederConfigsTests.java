package aajb;

import com.mifmif.common.regex.Generex;
import org.databene.benerator.anno.InvocationCount;
import org.databene.benerator.anno.Stochastic;
import org.databene.feed4junit.Feeder;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.constraints.Pattern;


/**
 * Created by ayed.h on 24/02/2016.
 */
@RunWith(Feeder.class)
@Stochastic
public class FeederConfigsTests {



    @Test
    @InvocationCount(100)
    public void testAdd(@Pattern(regexp = "[\\w!#\\$%&\\*\\+/=\\?`\\{\\|\\}~\\^\\-]+" +
            "@(\\?:[a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,6}") String ignored) {
        System.out.println(ignored);
    }

}
