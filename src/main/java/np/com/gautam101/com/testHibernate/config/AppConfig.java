package np.com.gautam101.com.testHibernate.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper geModelMapper(){
        return new ModelMapper();
    }
}
