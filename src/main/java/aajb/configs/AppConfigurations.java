package aajb.configs;

import aajb.service.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;


/**
 * Created by ayed.h on 24/02/2016.
 */
@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories("aajb.repository")
@PropertySource(value = "classpath:configs.properties")
public class AppConfigurations {


    @Qualifier("securityService")
    @Autowired
    private SecurityServiceImpl securityService;
    @Qualifier("environment")
    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(environment.getProperty("mail.host"));
        javaMailSender.setUsername(environment.getProperty("mail.username"));
        javaMailSender.setPassword(securityService.aesDecrypt(environment.getProperty("mail.password")));
        javaMailSender.setPort(587);

        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        return properties;
    }
}
