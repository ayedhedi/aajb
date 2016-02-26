package aajb.dao.repository;


import aajb.domain.school.Parent;
import aajb.Application;
import aajb.repository.ParentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Created by ayed.h on 23/02/2016.
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ParentRepositoryTest {


    @Qualifier("parentRepository")
    @Autowired
    private ParentRepository parentRepository;
    @Qualifier("environment")
    @Autowired
    private Environment environment;


    @Before
    public void setUp() throws Exception {
        Assert.notNull(parentRepository);
    }

    @Test
    public void testFindByLogin() throws Exception {
        String initUsersData = environment.getProperty("users.initData");
        if (initUsersData!=null) {
            Arrays.asList(initUsersData.split(";")).forEach(userString -> {
                String[] userSplit = userString.split(",");
                Parent parent = parentRepository.findByLogin(userSplit[2]);

                Assert.notNull(parent);
                Assert.isTrue(userSplit[0].equals(parent.getFirstName()));
                Assert.isTrue(userSplit[1].equals(parent.getLastName()));
                Assert.isTrue(userSplit[3].equals(parent.getPassword()));
                Assert.isTrue(userSplit[4].equals(parent.getEmail()));
            });
        }
    }

    @Test
    public void testFindByEmail() throws Exception {
        String initUsersData = environment.getProperty("users.initData");
        if (initUsersData!=null) {
            Arrays.asList(initUsersData.split(";")).forEach(userString -> {
                String[] userSplit = userString.split(",");
                Parent parent = parentRepository.findByEmail(userSplit[4]);

                Assert.notNull(parent);
                Assert.isTrue(userSplit[0].equals(parent.getFirstName()));
                Assert.isTrue(userSplit[1].equals(parent.getLastName()));
                Assert.isTrue(userSplit[2].equals(parent.getLogin()));
                Assert.isTrue(userSplit[3].equals(parent.getPassword()));
            });
        }
    }
}