package aajb.runnable;

import aajb.dao.repository.ParentRepository;
import aajb.domain.school.Parent;
import aajb.domain.user.UserProfileType;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ayed.h on 23/02/2016.
 */
@SpringBootApplication
@ComponentScan("aajb")
public class InitUserDatabaseApp implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(InitUserDatabaseApp.class, args);
        ParentRepository parentRepository = applicationContext.getBean(ParentRepository.class);

        Environment environment = applicationContext.getEnvironment();

        String initUsersData = environment.getProperty("users.initData");
        Arrays.asList(initUsersData.split(";")).stream().filter(user ->
                parentRepository.findByLogin(user.split(",")[2])==null) .forEach(userString -> {
            String[] userSplit = userString.split(",");
            UserProfileType[] types = null;
            if (userSplit.length>4){
                types = getTypes(Arrays.copyOfRange(userSplit,5,userSplit.length));
            }
            Parent parent = createParent(userSplit[0], userSplit[1], userSplit[2], userSplit[3], userSplit[4], types);
            parentRepository.save(parent);
        });

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitUserDatabaseApp.applicationContext = applicationContext;
    }

    private static UserProfileType[] getTypes(String[] tab) {
        if (tab==null){
            return null;
        }
        List<UserProfileType> types = new ArrayList<>();
        for(String t:tab) {
            switch (t) {
                case "ADMIN":
                    types.add(UserProfileType.ADMIN);break;
                case "USER":
                    types.add(UserProfileType.USER);break;
                case "DBA":
                    types.add(UserProfileType.DBA);break;
            }
        }
        return types.toArray(new UserProfileType[types.size()]);
    }

    private static Parent createParent(String firstName,String lastName,String login, String password, String email,
                                   UserProfileType... types) {
        Parent parent = new Parent();
        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setLogin(login);
        parent.setPassword(password);
        parent.setEmail(email);
        if (types!=null && types.length>0) {
            parent.setUserProfiles(new HashSet<>());
            for(UserProfileType type:types) {
                parent.getUserProfiles().add(type);
            }
        }
        return parent;
    }
}
