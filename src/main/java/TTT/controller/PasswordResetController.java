package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PasswordResetController {

    private final CustomUserDAO customUserDAO = new CustomUserDAO();

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

    @GetMapping("loginFailure")
    public String getInformationPage(Model model) {

        String message = "please enter valid email or password";
        String nextPage = "/";

        model.addAttribute("nextPage", nextPage);
        model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message",message);

        return "error/generic";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        if (customUser != null) {
            String pass = customUser.getPassword();
            if (customUserDAO.isValidPassword(pass)){

            customUser.setPassword(newPassword);
            customUserDAO.updateUserField(newPassword, customUser.getEmail(),"password");

            String nextPage = "/";
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
            }else {
                String message = "enter valid PASSWORD";
                String nextPage = "passwordRetrieve";

                model.addAttribute("nextPage", nextPage);
                model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                model.addAttribute("message",message);

                return "error/generic";
            }
        }
        else {
            String message = "enter valid EMAIL";
            String nextPage = "passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message",message);

            return "error/generic";
        }
    }
}
