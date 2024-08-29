package TTT.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public User defaultUser(){
        return new User("admin","admin","nick1",99);
    }
}
