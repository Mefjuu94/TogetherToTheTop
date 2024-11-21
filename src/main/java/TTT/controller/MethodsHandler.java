package TTT.controller;

import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodsHandler {

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
}
