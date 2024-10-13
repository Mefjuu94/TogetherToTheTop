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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class CustomUserController {

    CustomUserDAO customUserDAO = new CustomUserDAO();

    @GetMapping("/user")
    public String getMainPage(Model model) {
        String email = getLoggedInUserName();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        model.addAttribute("customUser",customUser);

        return "user";
    }

    @PostMapping("/updateName")
    public String updateName(@RequestParam String newName) {

        System.out.println("your new name is: " + newName);
        String email = getLoggedInUserName();

        customUserDAO.updateUserName(email, newName);
        return "user"; // lub inna strona po aktualizacji
    }

    @PostMapping("/updateAge")
    public String updateName(@RequestParam int newAge) {

        String email = getLoggedInUserName();

        customUserDAO.updateUserAge(email, newAge);
        return "redirect:/profile"; // lub inna strona po aktualizacji
    }

    @PostMapping("/updateField")
    public String updateField(@RequestParam("fieldName") String fieldName,
                              @RequestParam("newValue") String newValue) {

        String email = getLoggedInUserName();
        System.out.println(email);

        // Zaktualizuj odpowiednie pole na podstawie nazwy pola
        switch (fieldName) {
            case "email":
               // customUserDAO.updateUserEmail(email);
                break;
            case "username":
                customUserDAO.updateUserName(email,newValue);
                break;
            case "age":
                customUserDAO.updateUserAge(email,Integer.parseInt(newValue));
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        return "sendData";
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
