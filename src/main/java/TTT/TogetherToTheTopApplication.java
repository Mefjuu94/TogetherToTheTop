package TTT;

import TTT.databaseUtils.CommentsDAO;
import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Comments;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class TogetherToTheTopApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TogetherToTheTopApplication.class, args);

        long id = 902;

        //2024-10-26T15:00
        //date formated: 2024-10-26T15:00

//        TripDAO tripDAO = new TripDAO();
//        Trip trip = tripDAO.findTripID(id);
       // trip.setTripDateTime();
//        CommentsDAO commentsDAO = new CommentsDAO();
//        List<Comments> comments = commentsDAO.findByTripID(id);
//        System.out.println(comments);


    }

}
