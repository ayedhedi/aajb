package aajb;

import aajb.domain.user.User;
import aajb.domain.user.UserProfileType;
import aajb.repository.UserRepository;
import aajb.service.RegistrationService;
import aajb.service.SecurityService;
import aajb.service.dto.ChequeDto;
import aajb.service.dto.ParentDto;
import aajb.service.dto.RegistrationDto;
import aajb.service.dto.StudentDto;
import aajb.service.exceptions.InvalidDataException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ayed.h on 23/02/2016.
 */
@SpringBootApplication
@ComponentScan("aajb")
public class InitUserDatabaseApp implements ApplicationContextAware {
    private static int nbRegistration = 200;
    private static ApplicationContext applicationContext;
    private static SecurityService securityService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(InitUserDatabaseApp.class, args);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        RegistrationService registrationService = applicationContext.getBean(RegistrationService.class);
        UserDetailsService userDetailsService = applicationContext.getBean(UserDetailsService.class);

        Environment environment = applicationContext.getEnvironment();
        securityService = applicationContext.getBean(SecurityService.class);


        String initUsersData = environment.getProperty("users.initData");
        Arrays.asList(initUsersData.split(";")).stream().filter(user ->
                userRepository.findByLogin(user.split(",")[2])==null) .forEach(userString -> {
            String[] userSplit = userString.split(",");
            UserProfileType[] types = null;
            if (userSplit.length>4){
                types = getTypes(Arrays.copyOfRange(userSplit,5,userSplit.length));
            }
            User user = createUser(userSplit[0], userSplit[1], userSplit[2], userSplit[3], userSplit[4], types);
            userRepository.save(user);
        });

        UserDetails userDetails = userDetailsService.loadUserByUsername("aaa");
        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);


        //create registration
        for(int i=0;i<nbRegistration;i++) {
            RegistrationDto registrationDto = createRandomRegistration();
            try {
                registrationService.createRegistration(registrationDto);
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
            StringWriter sw = new StringWriter();
            (new ObjectMapper()).writeValue(sw,registrationDto);
            System.out.println(sw.toString());
        }

        SpringApplication.exit(applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitUserDatabaseApp.applicationContext = applicationContext;
    }

    private static RegistrationDto createRandomRegistration() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstParent(createRandomParent());
        if (Math.random()>0.5) {
            registrationDto.setSecondParent(createRandomParent());
        }
        int nbStudent = (int)(Math.random()*5) + 1;
        registrationDto.setStudents(new HashSet<>());
        while(registrationDto.getStudents().size()<nbStudent) {
            registrationDto.getStudents().add(createRandomStudent());
        }
        int nbCheck = (int)(Math.random()*10) + 1;
        registrationDto.setCheques(new HashSet<>());
        while(registrationDto.getCheques().size()<nbCheck) {
            registrationDto.getCheques().add(createRandomOrder());
        }
        return registrationDto;
    }

    private static ChequeDto createRandomOrder() {
        ChequeDto dto = new ChequeDto();
        dto.setAmount((int)(Math.random()*200) + 1);
        dto.setBankName((Math.random()>0.5?(banks[(int)(Math.random()*banks.length)]):null));
        dto.setNumber((Math.random()> 0.4 ? createSsn(): null));

        if (Math.random() < 0.1) {
            dto.setAdjustable(false);
        }else {
            dto.setAdjustable(true);
            try {
                dto.setAdjustableDate(sdf.parse(dates[(int)(Math.random()* dates.length)]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        dto.setRemarks("Nothing ... !!");
        return dto;
    }

    private static StudentDto createRandomStudent() {
        StudentDto dto = new StudentDto();
        dto.setFirstName(firstNames[(int)(Math.random()*firstNames.length)]);
        dto.setLastName(lastNames[(int)(Math.random()*lastNames.length)]);
        dto.setGender((Math.random()>0.5?"MALE":"FEMALE"));
        dto.setClassName(classNames[(int)(Math.random()*classNames.length)]);
        dto.setBirthDate(new Date((long)(Math.random()*(maxDate-minDate)) + minDate));
        return dto;
    }

    private static ParentDto createRandomParent() {
        ParentDto dto = new ParentDto();
        dto.setFirstName(firstNames[(int)(Math.random()*firstNames.length)]);
        dto.setLastName(lastNames[(int)(Math.random()*lastNames.length)]);
        dto.setGender((Math.random()>0.5?"MALE":"FEMALE"));
        dto.setEmail(dto.getFirstName().toLowerCase()+"."+dto.getLastName().toLowerCase()+emails[(int)(Math.random()*emails.length)]);
        dto.setTel(Math.random()>0.5?createRandTel():null);
        dto.setTelPro(Math.random() > 0.5 ? createRandTel() : null);
        dto.setTelGsm(Math.random() > 0.5 ? createRandTel() : null);
        dto.setSsn(Math.random()> 0.4 ? createSsn(): null);
        dto.setCaf(Math.random()>6 ? (int)(Math.random()*10000):0);
        dto.setJob(Math.random()>0.4?jobs[(int)(Math.random()*jobs.length)]:null);
        return dto;
    }

    private static String createRandTel() {
        String tel = "06";
        while (tel.length()!=10){
            tel += (int)(Math.random()*10);
        }
        return tel;
    }

    private static String createSsn() {
        String ssn = "";
        while(ssn.length()!=13) {
            ssn += (int)(Math.random()*10);
        }
        return ssn;
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
            }
        }
        return types.toArray(new UserProfileType[types.size()]);
    }

    private static User createUser(String firstName,String lastName,String login, String password, String email,
                                   UserProfileType... types) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(securityService.encryptPassword(password));
        user.setEmail(email);
        user.setActive(true);
        if (types!=null && types.length>0) {
            user.setUserProfiles(new HashSet<>());
            for(UserProfileType type:types) {
                user.getUserProfiles().add(type);
            }
        }
        return user;
    }

    private static final long minDate = 631148400000L;
    private static final long maxDate = 1356908400000L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final String[] dates = {"12/01/2016","01/01/2017","03/01/2017","04/01/2017","05/01/2017"};
    private static final String[] classNames = {"GA","MA","PA","GB","MB","GC","MC"};
    private static final String[] firstNames = {"Noah","Liam", "Mason","Jacob","William","Ethan","Michael",
            "Alexander","James","Daniel","Elijah","Benjamin","Logan","Aiden"};
    private static final String[] lastNames = {"SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "WILSON"};
    private static final String[] emails = {"@post.lu","@gmail.com","@rtl.lu","@yahoo.fr","@msn.fr"};
    private static final String[] jobs = {
            "Ambulances",
            "Blanchisserie et pressing (sauf libre-service)",
            "Coiffure",
            "Compositions florales",
            "Contrôle technique",
            "Cordonnerie et réparation d'articles personnels et domestiques",
            "Déménagement",
            "Embaumement, soins mortuaires",
            "Entretien et réparation de machines de bureau et de matériel informatique",
            "Etalage, décoration",
            "Finition et restauration de meubles, dorure, encadrement",
            "Maréchalerie",
            "Pose d'affiches, travaux à façon, conditionnement à façon",
            "Ramonage, nettoyage, entretien de fosses septiques et désinsectisation",
            "Réparation automobile cycles et motocycles",
            "Réparation d'objets d'art",
            "Spectacle de marionnettes",
            "Soins de beauté"
    };
    private static final String[] banks = {"BNP","LAPOSTE","CIC","LCL","CA","CM"};

}
