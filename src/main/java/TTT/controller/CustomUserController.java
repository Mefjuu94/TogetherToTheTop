package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class CustomUserController {

    private final CustomUserDAO customUserDAO = new CustomUserDAO();
    private final UserRatingDAO userRatingDAO = new UserRatingDAO();
    private final TripDAO tripDAO = new TripDAO();
    private final MethodsHandler methodsHandler = new MethodsHandler();

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

        Map<Long,List<Long>> objects = tripDAO.listAllTripParticipantIds();
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

        List<CustomUser> customUsers = customUserDAO.findCustomUserByName(friendName);
        model.addAttribute("customUser", customUsers);

        return "results";
    }

    @GetMapping("/userRating")
    public String userRating(@RequestParam String userID, Model model) {
        String me = methodsHandler.getLoggedInUserName();
        String myId = String.valueOf(customUserDAO.findCustomUserByEmail(me).getId());

        List<UserRating> userRating = userRatingDAO.listAllRates(Long.parseLong(userID));
        if (userRating.size() < 2){
            if (userID.equals(myId)){
                return "redirect:/myProfile";
            }
            return "redirect:/userProfile/" + userID;
        }

        int averageRate = 0;
        for (UserRating rating : userRating) {
            averageRate += rating.getRating();
        }

        averageRate = averageRate /userRating.size();

        model.addAttribute("averageRate",averageRate);
        model.addAttribute("userRating", userRating);

        return "userRating";
    }

    @GetMapping("/findTrip")
    public String findTrip(@RequestParam String tripDestination, Model model) {

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

        Map<Long,List<Long>> obejcts = tripDAO.listAllTripParticipantIds();
        CustomUser customUser = customUserDAO.findCustomUserByID(userID);
        List<Trip> trips = methodsHandler.listOfTrips(obejcts, customUser);

        model.addAttribute("trips", trips);

        return "results";
    }

    @PostMapping("/updateField")
    public String updateField(@RequestParam("fieldName") String fieldName,
                              @RequestParam("newValue") String newValue, Model model) {

        String email = methodsHandler.getLoggedInUserName();

        if (fieldName.equals("age")) {
            if(customUserDAO.updateUserAge(email, Integer.parseInt(newValue))){
                return "redirect:/myProfile"; //reload page
            }else {
                String message = "something went wrong: Cannot set Age. Try again with digits only!";
                String nextPage = "/myProfile";

                model.addAttribute("nextPage", nextPage);
                model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                model.addAttribute("message",message);

                return "error/generic";
            }
        } else {
            if (customUserDAO.updateUserField(newValue, email, fieldName,"")){
                String nextPage = "/myProfile";
                model.addAttribute("nextPage", nextPage);

                return "actionSuccess";
            }else {
                String message = "something went wrong: Cannot set chosen parameter!";
                String nextPage = "/myProfile";

                model.addAttribute("nextPage", nextPage);
                model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                model.addAttribute("message",message);

                return "error/generic";
            }
        }
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
        if (userRatingDAO.editRate(Long.parseLong(rateId), Integer.parseInt(rate),trip,behavior)) {
            return "redirect:/rate";
        }else {
            String message = "something went wrong: Cannot add rate!";
            String nextPage = "/trips/" + trip;

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message",message);

            return "error/generic";
        }
    }
}
