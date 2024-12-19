package TTT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling //for automatic check (DateChecker)
public class TogetherToTheTopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TogetherToTheTopApplication.class, args);

    }
}
