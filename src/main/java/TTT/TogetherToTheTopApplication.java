package TTT;

import TTT.databaseUtils.CommentsDAO;
import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Comments;
import TTT.trips.GPX;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableScheduling //for automatic check (DateChecker)
public class TogetherToTheTopApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TogetherToTheTopApplication.class, args);
    }

}
