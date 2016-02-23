package aajb.dao.configs;

/**
 * Created by ayed.h on 22/02/2016.
 */

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("aajb.dao.repository")
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    private Map<String,?> jpaProperties() {
        Map<String,String> jpaPropertiesMap = new HashMap<String,String>();
        jpaPropertiesMap.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
        jpaPropertiesMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        jpaPropertiesMap.put("hibernate.generate_statistics", "true");
        jpaPropertiesMap.put("hibernate.show_sql", "true");
        jpaPropertiesMap.put("hibernate.format_sql", "true");
        jpaPropertiesMap.put("hibernate.use_sql_comments", "true");
        return jpaPropertiesMap;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));
        factoryBean.setJpaPropertyMap(jpaProperties());
        return factoryBean;
    }


}
