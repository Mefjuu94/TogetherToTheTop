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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class AnnouncementController {

    TripDAO tripDAO = new TripDAO();
    CommentsDAO commentsDAO = new CommentsDAO();

    @GetMapping("/announcement")
    public String getAnnouncementsPage(Model model) {
        TripDAO tripDAO = new TripDAO();
        ArrayList<Trip> trips = new ArrayList<>(tripDAO.listAllAnnouncements());
        Collections.reverse(trips);
        String email = getLoggedInUserName();
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
        String email = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();

        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        Trip trip = tripDAO.findTripID(id);
        CustomUser owner = trip.getOwner();
        List<Comments> comments = commentsDAO.findByTripID(trip.getId());

        boolean isParticipant = false;

        for (int i = 0; i < trip.getParticipantsId().size(); i++) {
            if (trip.getParticipantsId().get(i) == customUser.getId()) {
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

        model.addAttribute("customUser", customUser);
        model.addAttribute("rating", rating);
        model.addAttribute("rate", rate);

        return "userProfile";
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

        if (trip.getOwner().getId() == (customUser.getId())) {
            newUserToTrip = true;
            System.out.println("You are owner of This Trip!");
        } else {

            // if user didnt exist in list add
            if (!newUserToTrip) {
                List<Long> participantId = trip.getParticipantsId();
                participantId.add(customUser.getId());
                trip.setParticipantsId(participantId);

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

    @PostMapping("/addComment")
    public String addComment(@RequestParam long tripIdComment, @RequestParam String comment, @RequestParam long userIdComment, @RequestParam String userName) {

        commentsDAO.addComment(new Comments(comment, userIdComment, tripIdComment, userName));

        return "redirect:/trips/" + tripIdComment;
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam String idComment, @RequestParam String idOfTrip) {

        long commentID = Long.parseLong(idComment);

        System.out.println(commentID);
        System.out.println(idOfTrip);

        if (commentsDAO.deleteComment(commentID)) {
            System.out.println("comment was deleted!");
        } else {
            System.out.println("something went wrong");
        }

        return "redirect:/trips/" + idOfTrip;
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
