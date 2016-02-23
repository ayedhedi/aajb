package aajb.controller;

import aajb.runnable.Application;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by ayed.h on 23/02/2016.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class WebConfigurationsTest {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        //this will avoid the use of the annotation @RunWith(SpringJUnit4ClassRunner.class)
        new TestContextManager(getClass()).prepareTestInstance(this);

        assert wac != null;
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
