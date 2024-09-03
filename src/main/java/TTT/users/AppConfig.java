package TTT.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CustomUser defaultUser(){
        return new CustomUser("user@mail.123","user");
    }
}
