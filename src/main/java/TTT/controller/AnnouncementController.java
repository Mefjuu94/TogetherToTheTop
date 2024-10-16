package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AnnouncementController {

    TripDAO tripDAO = new TripDAO();

    @GetMapping("/announcement")
    public String getAnnouncementsPage(Model model) {
        TripDAO tripDAO = new TripDAO();
        ArrayList<Trip> trips = new ArrayList<>(tripDAO.listAllAnnouncements());
        String email = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        boolean flag = false;
        List<Long> tripsID = new ArrayList<>();

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        for (int i = 0; i < trips.size() ; i++) {
            List<CustomUser> participant = trips.get(i).getParticipants();
            for (int j = 0; j < participant.size(); j++) {
                if (participant.get(j).getId() == customUser.getId()){
                    flag = true;
                    break;
                }
                System.out.println(flag +  " " + trips.get(i).getId());
                if (flag){
                   tripsID.add(trips.get(i).getId()) ;
                }
            }
        }

        model.addAttribute("trips", trips);
        model.addAttribute("customUser",customUser);
        model.addAttribute("flag",flag);
        model.addAttribute("tripsID",tripsID);

        return "announcement";
    }

    @GetMapping("/trips/{id}")
    public String getTripDetails(@PathVariable Long id, Model model) {
        String email = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        Trip trip = tripDAO.findTripID(id);
        model.addAttribute("trip", trip);
        model.addAttribute("customUser", customUser);
        return "trip";
    }

    @PostMapping("/addMe")
    public String addMeToTrip(@RequestParam String tripId, @RequestParam String userId) {

        System.out.println("get data trip ID: " + tripId);
        System.out.println("get data user ID: " + userId);

        Trip trip = tripDAO.findTripID(Long.parseLong(tripId));
        CustomUserDAO customUserDAO = new CustomUserDAO();
        CustomUser customUser = customUserDAO.findCustomUserByID(userId);

        boolean newUserToTrip = false;
        //check if user owith this ID is arleady on list
        List<CustomUser> users = trip.getParticipants();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == Long.parseLong(userId)) {
                newUserToTrip = true;
                break;
            }
        }

        if (trip.getOwnerOfTrip().getId() == (customUser.getId())) {
            newUserToTrip = true;
            System.out.println("You are owner of This Trip!");
        } else {

            // if user didnt exist in list add
            if (!newUserToTrip) {
                List<CustomUser> participants = trip.getParticipants();
                participants.add(customUser);
                trip.setParticipants(participants);

                List<Trip> userTrips = customUser.getTripsParticipated();
                userTrips.add(trip);
                customUser.setTripsParticipated(userTrips);

                // Zapisz zmiany w bazie danych
                tripDAO.updateTripParticipants(Long.parseLong(tripId), trip);

                String email = getLoggedInUserName();
                customUserDAO.updateUserTrips(email, userTrips);

                System.out.println("User " + userId + " added to trip " + tripId);
            } else {
                System.out.println("User " + userId + " is already a participant of trip " + tripId);
            }
        }
        return "actionSuccess";
    }

    private String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername();
            }
            return principal.toString();
        }
        return null;
    }


}
