package TTT.controller;

import TTT.databaseUtils.CommentsDAO;
import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Comments;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AnnouncementController {

    TripDAO tripDAO = new TripDAO();
    CommentsDAO commentsDAO = new CommentsDAO();

    @GetMapping("/announcement")
    public String getAnnouncementsPage(Model model) {
        TripDAO tripDAO = new TripDAO();
        ArrayList<Trip> rawtrips = new ArrayList<>(tripDAO.listAllAnnouncements());

        LocalDateTime localDateTime = LocalDateTime.now();
        ArrayList<Trip> trips = new ArrayList<>();
        for (int i = 0; i < rawtrips.size(); i++) {
            if (localDateTime.isAfter(rawtrips.get(i).getTripDateTime())){
                System.out.println("ten trip: " + rawtrips.get(i).getDestination() + " jest już odbyta!");
            }else {
                trips.add(rawtrips.get(i));
            }
        }



        Collections.reverse(trips);
        String email = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        boolean flag = false;

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        List<Object[]> obejcts = tripDAO.listAllTripParticipantIds();
        List<String> tripsAndIDs = new ArrayList<>();

        for (int i = 0; i < obejcts.size(); i++) {

            String s1 = Arrays.toString(obejcts.get(i));
            String s = s1.replaceAll("[\\[\\]\\s]", "");
            String[] split = s.split(",");
            String tripID = split[0];
            String userID = split[1];
            System.out.println("trip id = " + tripID);
            System.out.println("user id = " + userID);
            tripsAndIDs.add(userID + "," + tripID);
        }


        model.addAttribute("trips", trips);
        model.addAttribute("customUser", customUser);
        model.addAttribute("flag", flag);
        model.addAttribute("tripsAndIDs", tripsAndIDs);


        return "announcement";
    }

    @GetMapping("/trips/{id}")
    public String getTripDetails(@PathVariable Long id, Model model) {
        String email = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        Trip trip = tripDAO.findTripID(id);
        CustomUser owner = trip.getOwner();
        List<Comments> comments = commentsDAO.findByTripID(trip.getId());

        boolean isParticipant = false;

        for (int i = 0; i < trip.getParticipants().size(); i++) {
            if (trip.getParticipants().get(i).getId() == customUser.getId()) {
                isParticipant = true;
                break;
            }
        }

        model.addAttribute("trip", trip);
        model.addAttribute("customUser", customUser);
        model.addAttribute("owner", owner);
        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("comments", comments);

        return "trip";
    }

    @GetMapping("/downloadGpx")
    public ResponseEntity<Resource> downloadFile(@RequestParam String trip_Identity) {
        Trip trip = tripDAO.findTripID(Long.parseLong(trip_Identity));

        String filePath = "src/main/resources/routes/" + trip.getOwner().getId() + "_" + trip.getTripDateTime().toString().replace(":", "_") + "_route.gpx";

        // if didnbt exist write new file gpx
        File file = new File(filePath);
        if (!file.exists()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(trip.getGpxFile());
            } catch (IOException e) {
                System.out.println("Błąd przy zapisywaniu pliku: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }

        // check if exist and preapare for download
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Pobranie pliku jako zasób
        Resource fileResource = new FileSystemResource(file);

        String fileName = trip.getOwner().getId() + "_" + trip.getTripDateTime().toString().replace(":", "_") + "_route.gpx";

        // set answer HTTP
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM) //  OCTET_STREAM for binary Files
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileResource);

    }

    @GetMapping("/userProfile/{id}")
    public String getAnyUserProfile(@PathVariable Long id, Model model) {
        CustomUserDAO customUserDAO = new CustomUserDAO();

        CustomUser customUser = customUserDAO.findCustomUserByID(String.valueOf(id));
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

        List<Trip> trips = tripDAO.listAllAnnouncements();

        int numberOfTripsOwned = 0;
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getOwner().getId() == customUser.getId()){
                numberOfTripsOwned ++;
            }
        }

        List<Object[]> obejcts = tripDAO.listAllTripParticipantIds();
        List<Trip> tripsParticipated = new ArrayList<>();

        for (int i = 0; i < obejcts.size(); i++) {
            String s1 = Arrays.toString(obejcts.get(i));
            String s = s1.replaceAll("[\\[\\]\\s]", "");
            String[] split = s.split(",");
            long tripID = Long.parseLong(split[0]);
            long user_id = Long.parseLong(split[1]);
            System.out.println("trip id = " + tripID);
            System.out.println("user id = " + user_id);
            if (user_id == customUser.getId()){
                Trip trip = tripDAO.findTripID(tripID);
                tripsParticipated.add(trip);
            }
        }

        model.addAttribute("customUser", customUser);
        model.addAttribute("rating", rating);
        model.addAttribute("rate", rate);
        model.addAttribute("tripsParticipated",tripsParticipated);
        model.addAttribute("numberOfTripsOwned",numberOfTripsOwned);

        return "userProfile";
    }

    @PostMapping("/addMe")
    public String addMeToTrip(@RequestParam String tripId, @RequestParam String userId,Model model) {

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

        // if user didnt exist in list add
        if (!newUserToTrip) {
            List<CustomUser> participants = trip.getParticipants();
            participants.add(customUser);
            trip.setParticipants(participants);
            //add number to trips participated
            int changeNumberOfTrips = customUser.getNumbersOfTrips();
            customUser.setNumbersOfTrips(changeNumberOfTrips + 1);

            List<Trip> userTrips = customUser.getTripsParticipated();
            userTrips.add(trip);
            customUser.setTripsParticipated(userTrips);

            // Zapisz zmiany w bazie danych
            tripDAO.updateTripParticipants(Long.parseLong(tripId), trip);

            String email = getLoggedInUserName();
            customUserDAO.updateUserTrips(email, userTrips);

            System.out.println("User " + userId + " added to trip " + tripId);
        }

        String nextPage = "/trips/" + tripId;
        model.addAttribute("nextPage",nextPage);

        return "actionSuccess";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam long tripIdComment, @RequestParam String comment, @RequestParam long userIdComment, @RequestParam String userName,Model model) {

        commentsDAO.addComment(new Comments(comment, userIdComment, tripIdComment, userName));

        String nextPage = "/trips/" + tripIdComment;
        model.addAttribute("nextPage",nextPage);

        return "actionSuccess";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam String idComment, @RequestParam String idOfTrip, Model model) {

        long commentID = Long.parseLong(idComment);

        System.out.println(commentID);
        System.out.println(idOfTrip);

        if (commentsDAO.deleteComment(commentID)) {
            System.out.println("comment was deleted!");
        } else {
            System.out.println("something went wrong");
        }

        String page = "/trips/" + idOfTrip;
        model.addAttribute("page",page);

        return "actionSuccess";
    }

    @PostMapping("/delete_participant")
    public String deleteParticipant(@RequestParam String user_Identity,@RequestParam String tripId,Model model) {

        long userID = Long.parseLong(user_Identity);
        long tripID = Long.parseLong(tripId);

        Trip trip = tripDAO.findTripID(tripID);
        List<CustomUser> participants = trip.getParticipants();
        List<CustomUser> newParticipants = new ArrayList<>();

        for (CustomUser participant : participants) {
            long id = participant.getId();
            if (id == userID) {
                System.out.println("this user should be deleted!");
            } else {
                newParticipants.add(participant);
            }
        }

        trip.setParticipants(newParticipants);
        tripDAO.updateTripParticipants(Long.parseLong(tripId), trip);

        String nextPage = "/trips/" + tripId;
        model.addAttribute("nextPage",nextPage);

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
