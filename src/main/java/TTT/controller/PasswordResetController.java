package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PasswordResetController {

    CustomUserDAO customUserDAO = new CustomUserDAO();
    CustomUser customUser = new CustomUser();

    @GetMapping("passwordRetrieve")
    public String getPasswordRetrievePage(Model model) {
        List<CustomUser> usersList = customUserDAO.listAllUsers();
        List<String> users = new ArrayList<>();
        for (CustomUser customUser : usersList) {
            users.add(customUser.getEmail());
        }

        model.addAttribute("users", users);

        return "passwordRetrieve";
    }

    @GetMapping("information")
    public String getInformationPage(Model model) {

        String information = "please enter valid email or password";
        String nextPage = "/";

        model.addAttribute("nextPage", nextPage);
        model.addAttribute("information",information);
        return "information";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
        customUser = customUserDAO.findCustomUserByEmail(email);
        if (customUser != null) {
            String pass = customUser.getPassword();
            if (customUserDAO.isValidPassword(pass)){

            customUser.setPassword(newPassword);
            customUserDAO.updateUserField(newPassword, customUser.getEmail(),"password");

            String nextPage = "/";
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
            }else {
                String information = "enter valid PASSWORD";
                String nextPage = "/passwordRetrieve";

                model.addAttribute("nextPage", nextPage);
                model.addAttribute("information",information);

                return "information";
            }
        }
        else {
            String information = "enter valid EMAIL";
            String nextPage = "/passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("information",information);

            return "information";
        }
    }
}
