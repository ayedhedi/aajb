package aajb.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Configuration
public class AppConfigurations {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
