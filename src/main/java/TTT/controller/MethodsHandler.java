package TTT.controller;

import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodsHandler {

    private final UserRatingDAO userRatingDAO = new UserRatingDAO();
    private final TripDAO tripDAO = new TripDAO();

    protected String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername(); // return name os user (email)
            }else {
                throw new ClassCastException("Principal is not an instance of UserDetails.");
            }
        }
        return null;
    }

    protected ArrayList<Trip> listOfTrips(Map<Long, List<Long>> objects, CustomUser customUser) {
        ArrayList<Trip> tripsParticipated = new ArrayList<>();

        System.out.println("search for: " + customUser.getCustomUserName() + " id: " + customUser.getId());
        List<Long> tripIds = new ArrayList<>();

        List<Long> tripIdsForUser = new ArrayList<>();
        for (Long klucz : objects.keySet()) {
            long key = klucz.longValue();

            if (customUser.getId() == key) {
                tripIds = objects.get(klucz);

                if (tripIds != null) {
                    tripIdsForUser.addAll(tripIds);
                } else {
                    System.out.println("list is empty for userID: " + key);
                }
            }
            break;
        }

        for (Long tripId : tripIdsForUser) {
            Trip trip = tripDAO.findTripID(tripId);
            if (trip != null) {
                tripsParticipated.add(trip);
            } else {
                System.out.println("trip ID " + tripId + " was not found.");
            }
        }

        return tripsParticipated;
    }

    protected List<UserRating> usersToRate(CustomUser me) {

        List<UserRating> usersToRateTemp = userRatingDAO.listUsersToRateByMe(me.getId());
        List<UserRating> usersToRate = new ArrayList<>();

        for (UserRating userRating : usersToRateTemp) {
            if (!userRating.isFilled()) {
                usersToRate.add(userRating);
            }
        }

        return usersToRate;
    }
}
