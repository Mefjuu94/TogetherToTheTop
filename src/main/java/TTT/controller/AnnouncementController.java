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

    private final TripDAO tripDAO = new TripDAO();
    private final CommentsDAO commentsDAO = new CommentsDAO();
    private final MethodsHandler methodsHandler = new MethodsHandler();
    private final CustomUserDAO customUserDAO = new CustomUserDAO();

    @GetMapping("/announcement")
    public String getAnnouncementsPage(Model model) {
        List<Trip> rawTrips = tripDAO.listAllAnnouncements();

        LocalDateTime localDateTime = LocalDateTime.now();
        List<Trip> trips = new ArrayList<>();
        for (Trip rawTrip : rawTrips) {
            if (!localDateTime.isAfter(rawTrip.getTripDateTime())) { //added to list only before dateTime of trip
                trips.add(rawTrip);
            }
        }

        Collections.reverse(trips);
        String email = methodsHandler.getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        boolean flag = false;

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);

        model.addAttribute("trips", trips);
        model.addAttribute("customUser", customUser);
        model.addAttribute("flag", flag);


        return "announcement";
    }

    @GetMapping("/trips/{id}")
    public String getTripDetails(@PathVariable Long id, Model model) {
        String email = methodsHandler.getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        Trip trip = tripDAO.findTripID(id);
        CustomUser owner = trip.getOwner();
        List<Comments> comments = commentsDAO.findCommentsByTripID(trip.getId());

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

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource fileResource = new FileSystemResource(file);

        String fileName = trip.getOwner().getId() + "_" + trip.getTripDateTime().toString().replace(":", "_") + "_route.gpx";

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
            for (UserRating userRating : rating) {
                rate += userRating.getRating();
            }
            rate = rate / rating.size();
        } else {
            rate = 1;
        }

        List<Trip> trips = tripDAO.listAllAnnouncements();

        int numberOfTripsOwned = 0;
        for (Trip trip : trips) {
            if (trip.getOwner().getId() == customUser.getId()) {
                numberOfTripsOwned++;
            }
        }

        Map<Long,List<Long>> mapOfTripAndUsersID = tripDAO.listAllTripParticipantIds();
        List<Trip> tripsParticipated = methodsHandler.listOfTrips(mapOfTripAndUsersID, customUser);

        model.addAttribute("customUser", customUser);
        model.addAttribute("rating", rating);
        model.addAttribute("rate", rate);
        model.addAttribute("tripsParticipated", tripsParticipated);
        model.addAttribute("numberOfTripsOwned", numberOfTripsOwned);


        return "userProfile";
    }

    @PostMapping("/addMe")
    public String addMeToTrip(@RequestParam String tripId, @RequestParam String userId, Model model) {

        Trip trip = tripDAO.findTripID(Long.parseLong(tripId));
        CustomUser customUser = customUserDAO.findCustomUserByID(userId);

        boolean newUserToTrip = false;

        List<CustomUser> users = trip.getParticipants();
        for (CustomUser user : users) {
            if (user.getId() == Long.parseLong(userId)) {
                newUserToTrip = true;
                break;
            }
        }

        String email = "";
        List<Trip> userTrips = new ArrayList<>();
        if (!newUserToTrip) {
            List<CustomUser> participants = trip.getParticipants();
            participants.add(customUser);
            trip.setParticipants(participants);

            userTrips = customUser.getTripsParticipated();
            userTrips.add(trip);
            customUser.setTripsParticipated(userTrips);

            tripDAO.updateTrip(trip);

            email = methodsHandler.getLoggedInUserName();
        }
        if (customUserDAO.updateUserTrips(email, userTrips)){
            String nextPage = "/trips/" + tripId;
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        }else {
            String information = "something went wrong: Cannot add user as participant to Trip";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam long tripIdComment, @RequestParam String comment, @RequestParam long userIdComment, @RequestParam String userName, Model model) {

        if (commentsDAO.addComment(new Comments(comment, userIdComment, tripIdComment, userName))){
            String nextPage = "/trips/" + tripIdComment;
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        }else {
            String information = "something went wrong: Cannot add Comment to Trip";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam String idComment, @RequestParam String idOfTrip, Model model) {

        long commentID = Long.parseLong(idComment);

        if (commentsDAO.deleteComment(commentID)) {
            System.out.println("comment was deleted!");
            String page = "/trips/" + idOfTrip;
            model.addAttribute("page", page);

            return "actionSuccess";
        } else {
            String information = "something went wrong: Cannot delete comment";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }

    @PostMapping("/deleteParticipant")
    public String deleteParticipant(@RequestParam String user_Identity, @RequestParam String tripId, Model model) {

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
        if (tripDAO.updateTrip(trip)) {

            String nextPage = "/trips/" + tripId;
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        }else {
            String information = "something went wrong: Cannot delete Participant from list!";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }

    @PostMapping("/deleteTrip")
    public String deleteTrip(@RequestParam String tripId, Model model) {

        long tripID = Long.parseLong(tripId);

        Trip trip = tripDAO.findTripID(tripID);

        if (tripDAO.deleteTrip(trip)){
            String nextPage = "/announcement";
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        }else {
            String information = "something went wrong: Cannot delete Participant from list!";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }

    @PostMapping("/renewTrip")
    public String renew_trip(@RequestParam Long trip,@RequestParam LocalDateTime date,@RequestParam String new_description, Model model) {

        Trip renewTrip = tripDAO.findTripID(trip);
        renewTrip.setTripDateTime(date);
        renewTrip.setParticipants(new ArrayList<>());
        renewTrip.setTripVisible(true);
        renewTrip.setTripDescription(new_description);
        Trip trip1 = new Trip(); // to automatically create new ID
        renewTrip.setId(trip1.getId());

        if (tripDAO.addAnnouncement(renewTrip)) {

            String nextPage = "/announcement";
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        }else {
            String information = "something went wrong: cannot renew trip!";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }
}
