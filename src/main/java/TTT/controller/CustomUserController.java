package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class CustomUserController {


    CustomUserDAO customUserDAO = new CustomUserDAO();

    @GetMapping("/user")
    public String getMainPage(Model model) {
        String email = getLoggedInUserName();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);

        TripDAO tripDAO = new TripDAO();
        List<Trip> trips = tripDAO.listAllAnnouncements();

        int numberOfTripsOwned = 0;
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getOwner().getId() == customUser.getId()){
               numberOfTripsOwned ++;
            }
        }

        List<UserRating> rating = customUser.getRatings();
        int rate = 0;

        if (!rating.isEmpty()){
        for (int i = 0; i < rating.size(); i++) {
            rate += rating.get(i).getRating();
        }
        rate = rate/rating.size();
        }else {
            rate = 1;
        }

        model.addAttribute("customUser",customUser);
        model.addAttribute("numberOfTripsOwned",numberOfTripsOwned);
        model.addAttribute("rating",rating);
        model.addAttribute("rate",rate);

        return "user";
    }

    @PostMapping("/updateField")
    public String updateField(@RequestParam("fieldName") String fieldName,
                              @RequestParam("newValue") String newValue) {

        String email = getLoggedInUserName();
        System.out.println(email);

        // Zaktualizuj odpowiednie pole na podstawie nazwy pola
        if (fieldName.equals("age")){
            customUserDAO.updateUserAge(email, Integer.parseInt(newValue));
        }else {
            customUserDAO.updateUserField(newValue,email,fieldName);
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
