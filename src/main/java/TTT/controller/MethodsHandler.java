package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodsHandler {

    TripDAO tripDAO = new TripDAO();

    protected String getLoggedInUserName() {
        //download active user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if user is instance of UserDetails
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername(); // return name os user (email)
            }
            // if not return something
            return principal.toString();
        }
        return null;
    }

    protected ArrayList<Trip> listOfTrips(List<Object[]> obejcts, CustomUser customUser){
        TripDAO tripDAO = new TripDAO();
        ArrayList<Trip> tripsParticipated = new ArrayList<>();

        for (int i = 0; i < obejcts.size(); i++) {
            String s1 = Arrays.toString(obejcts.get(i));
            String s = s1.replaceAll("[\\[\\]\\s]", "");
            String[] split = s.split(",");
            long tripID = Long.parseLong(split[0]);
            long user_id = Long.parseLong(split[1]);
            if (user_id == customUser.getId()) {
                Trip trip = tripDAO.findTripID(tripID);
                tripsParticipated.add(trip);
            }
        }

        return tripsParticipated;
    }

    protected List<CustomUser> usersToRate(CustomUser me){

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
        for (Trip trip : tripsWhereParticipated) {
            if (!trip.isTripVisible()) {
                usersTemp.addAll(trip.getParticipants());
                usersTemp.add(trip.getOwner());
            }
        }

        UserRatingDAO userRatingDAO = new UserRatingDAO();
        List<UserRating> ratingList = userRatingDAO.listAllARatings();
        List<Long> reviewers = new ArrayList<>();
        List<Long> usersReviewed = new ArrayList<>();

        //collect ids from reviewers and users who reviewed
        for (UserRating userRating : ratingList) {
            reviewers.add(userRating.getReviewer().getId());
            usersReviewed.add(userRating.getUser().getId());
        }

        // if in participants match ids then remove from temporary usersList
        for (int i = 0; i < usersTemp.size(); i++) {
            for (int j = 0; j < usersReviewed.size(); j++) {
                if (me.getId() == reviewers.get(j) && usersTemp.get(i).getId() == usersReviewed.get(j)) {
                    usersTemp.remove(i);
                }
            }
        }

        users = usersTemp;
        return users;
    }
}
