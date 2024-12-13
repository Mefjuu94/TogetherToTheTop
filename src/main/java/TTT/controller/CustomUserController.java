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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomUserController {

    CustomUserDAO customUserDAO = new CustomUserDAO();
    TripDAO tripDAO = new TripDAO();
    MethodsHandler methodsHandler = new MethodsHandler();

    @GetMapping("/myProfile")
    public String getMainPage(Model model) {
        String email = methodsHandler.getLoggedInUserName();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        

        int numberOfTripsOwned = customUser.getNumbersOfTrips();


        List<UserRating> rating = customUser.getRatings();
        int rate = 0;

        if (!rating.isEmpty()) {
            for (int i = 0; i < rating.size(); i++) {
                rate += rating.get(i).getRating();
            }
            rate = rate / rating.size();
        } else {
            rate = 1;
        }

        List<Object[]> obejcts = tripDAO.listAllTripParticipantIds();
        List<Trip> tripsParticipated = methodsHandler.listOfTrips(obejcts, customUser);

        model.addAttribute("customUser", customUser);
        model.addAttribute("numberOfTripsOwned", numberOfTripsOwned);
        model.addAttribute("rating", rating);
        model.addAttribute("rate", rate);
        model.addAttribute("tripsParticipated", tripsParticipated);

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
    public String getTripsOWned(@RequestParam String userID, Model model) {

        List<Trip> trips = tripDAO.listAllAnnouncementsByUserId(Long.valueOf(userID));
        model.addAttribute("trips", trips);

        return "results";
    }

    @GetMapping("/rate")
    public String getUsersToRate(Model model) {

        String email = methodsHandler.getLoggedInUserName();
        CustomUser me = customUserDAO.findCustomUserByEmail(email);

        List<Object[]> participantsIDs = tripDAO.listAllTripParticipantIds();
        List<Trip> tripsWhereParticipated = new ArrayList<>();
        List<CustomUser> usersTemp = new ArrayList<>();
        List<CustomUser> users = new ArrayList<>();

        // get list where user participated.
        for (int i = 0; i < participantsIDs.size(); i++) {
            String s1 = Arrays.toString(participantsIDs.get(i));
            String s = s1.replaceAll("[\\[\\]\\s]", "");
            String[] split = s.split(",");
            long tripID = Long.parseLong(split[0]);
            long user_id = Long.parseLong(split[1]);

            if (me.getId() == user_id) {
                tripsWhereParticipated.add(tripDAO.findTripID(tripID));
            }
        }

        //add all participants who was with me:
        for (int i = 0; i < tripsWhereParticipated.size(); i++) {
            Trip trip = tripsWhereParticipated.get(i);

            if (!trip.isTripVisible()) {
                usersTemp.addAll(trip.getParticipants());
            }
        }

        UserRatingDAO userRatingDAO = new UserRatingDAO();
        List<UserRating> ratingList = userRatingDAO.listAllARatings();

        //check if user already rate participant from trip.
        for (int i = 0; i < usersTemp.size(); i++) {
            CustomUser participant = usersTemp.get(i);

            if (participant.getId() != me.getId()) {

                for (int j = 0; j < ratingList.size(); j++) {
                    UserRating rate = ratingList.get(j);

                    if (rate.getReviewer().getId() == me.getId() && rate.getUser().getId() == participant.getId() || participant.getId() == me.getId()) {
                        System.out.println("you " + me.getId() + " already rate user: " + rate.getUser().getCustomUserName() + " " + rate.getUser().getId());
                        break;
                    } else {
                        users.add(participant); //TODO
                    }
                }
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("me", me);

        return "rate";
    }

    @PostMapping("/addRate")
    public String getUsersToRate(@RequestParam String userId, @RequestParam String rate,
                                 @RequestParam String behavior,
                                 Model model) {

        String email = methodsHandler.getLoggedInUserName();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);

        CustomUser userToRate = customUserDAO.findCustomUserByID(userId);
        int rateInt = Integer.parseInt(rate);

        UserRating userRating = new UserRating(rateInt, behavior, userToRate, customUser);
        UserRatingDAO userRatingDAO = new UserRatingDAO();

        userRatingDAO.addRate(userRating);

        String nextPage = "/rate";
        model.addAttribute("nextPage", nextPage);

        return "actionSuccess";
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

    @PostMapping("/rate")
    public String saveRating(
            @RequestParam("userId") CustomUser userId,
            @RequestParam("behavior") String behavior,
            @RequestParam("rate") Integer rate,
            @RequestParam("me") CustomUser me) {
        UserRatingDAO ratingDAO = new UserRatingDAO();

        ratingDAO.addRate(new UserRating(rate, behavior, userId, me));


        return "redirect:/success";
    }
}
