package TTT;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class TogetherToTheTopApplication {

    static String city = "Praha";
    static String APIKEY = "zjEFy9NsTMuP_e3U9_B0sDu_axPSSl28smWg1PXW4i0";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TogetherToTheTopApplication.class, args);

//        CustomUserDAO customUserDAO = new CustomUserDAO();
//        List<CustomUser> users = customUserDAO.listAllUsers();
//        CustomUser a = users.get(1);
//        CustomUser b = users.get(0);
//        System.out.println(a.getCustomUserName() + " " + b.getCustomUserName());
//
//        UserRating rating = new UserRating(4,"Dobry kompan",a,b);
//        UserRatingDAO userRatingDAO = new UserRatingDAO();
//        System.out.println("Trying to rate: " + userRatingDAO.addRate(rating));

    }
}
