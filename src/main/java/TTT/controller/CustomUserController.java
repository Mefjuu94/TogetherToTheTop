package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CustomUserController {

    CustomUserDAO customUserDAO = new CustomUserDAO();
    TripDAO tripDAO = new TripDAO();
    MethodsHandler methodsHandler = new MethodsHandler();

    @GetMapping("/myProfile")
    public String getMainPage(Model model) {
        UserRatingDAO userRatingDAO = new UserRatingDAO();
        String email = methodsHandler.getLoggedInUserName();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);

        int numberOfTripsOwned = customUser.getTripsOwned().size();

        int rate = 0;
        List<UserRating> rating = userRatingDAO.listAllRates(customUser.getId());
        System.out.println(rating.size());

        for (UserRating userRating : rating) {
            rate += userRating.getRating();
        }
        if (rating.size() < 2){
            rate = 1;
        }else {
            rate = rate/rating.size();
        }

        List<Object[]> objects = tripDAO.listAllTripParticipantIds();
        List<Trip> tripsParticipated = methodsHandler.listOfTrips(objects, customUser);
        List<UserRating> users = methodsHandler.usersToRate(customUser);

        model.addAttribute("customUser", customUser);
        model.addAttribute("numberOfTripsOwned", numberOfTripsOwned);
        model.addAttribute("rating", rating);
        model.addAttribute("rate", rate);
        model.addAttribute("tripsParticipated", tripsParticipated);
        model.addAttribute("users", users.size());

        return "myProfile";
    }

    @GetMapping("/findFriend")
    public String findFriend(@RequestParam String friendName, Model model) {

        System.out.println("Search name: " + friendName);
        List<CustomUser> customUsers = customUserDAO.findCustomUserByName(friendName);
        model.addAttribute("customUser", customUsers);

        return "results";
    }

    @GetMapping("/findTrip")
    public String findTrip(@RequestParam String tripDestination, Model model) {

        System.out.println("Search Trip: " + tripDestination);
        List<Trip> trips = tripDAO.findTrip(tripDestination);
        model.addAttribute("trips", trips);

        return "results";
    }

    @GetMapping("/tripsOwned")
    public String getTripsOwned(@RequestParam String userID, Model model) {

        List<Trip> trips = tripDAO.listAllAnnouncementsByUserId(Long.valueOf(userID));
        model.addAttribute("trips", trips);

        return "results";
    }

    @GetMapping("/tripsParticipated")
    public String getTripsWhereParticipated(@RequestParam String userID, Model model) {

        List<Object[]> obejcts = tripDAO.listAllTripParticipantIds();
        CustomUser customUser = customUserDAO.findCustomUserByID(userID);
        List<Trip> trips = methodsHandler.listOfTrips(obejcts, customUser);

        model.addAttribute("trips", trips);

        return "results";
    }

    @PostMapping("/updateField")
    public String updateField(@RequestParam("fieldName") String fieldName,
                              @RequestParam("newValue") String newValue, Model model) {

        String email = methodsHandler.getLoggedInUserName();

        // update field
        if (fieldName.equals("age")) {
            customUserDAO.updateUserAge(email, Integer.parseInt(newValue));
        } else {
            customUserDAO.updateUserField(newValue, email, fieldName);
        }

        String nextPage = "/myProfile";
        model.addAttribute("nextPage", nextPage);

        return "actionSuccess";
    }

    @GetMapping("/rate")
    public String getUsersToRate(Model model) {

        String email = methodsHandler.getLoggedInUserName();
        CustomUser me = customUserDAO.findCustomUserByEmail(email);

        List<UserRating> rates = methodsHandler.usersToRate(me);

        model.addAttribute("rates", rates);
        model.addAttribute("me", me);

        return "rate";
    }

    @PostMapping("/addRate")
    public String getUsersToRate(@RequestParam String rate,
                                 @RequestParam String behavior,
                                 @RequestParam String tripId,
                                 @RequestParam String rateId,
                                 Model model) {

        Trip trip = tripDAO.findTripID(Long.parseLong(tripId));

        UserRatingDAO userRatingDAO = new UserRatingDAO();
        userRatingDAO.editRate(Long.parseLong(rateId), Integer.parseInt(rate),trip,behavior);

        String nextPage = "/rate";
        model.addAttribute("nextPage", nextPage);

        return "actionSuccess";
    }
}
