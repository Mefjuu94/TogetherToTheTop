package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.emailService.EmailService;
import TTT.users.CustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        model.addAttribute("message", message);

        return "error/generic";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        if (customUser != null) {
            Random random = new Random();

            customUser.setPassword(newPassword);
            customUserDAO.updateUserField(newPassword, customUser.getEmail(), "password",
                    String.valueOf(random.nextInt(99999)));

            String nextPage = "/";
            model.addAttribute("nextPage", nextPage);

            return "actionSuccess";
        } else {
            String message = "enter valid PASSWORD";
            String nextPage = "passwordRetrieve";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message", message);

            return "error/generic";
        }
    }

    @PostMapping("/sendCodeToEmail")
    public String sendVerifyCode(@RequestParam String email, Model model) throws IOException {
        CustomUser customUser = customUserDAO.findCustomUserByEmail(email);
        if (customUser != null) {
            String code = customUser.getAcivationCode();
            EmailService emailService = new EmailService();

            //change second email for tests
            if (emailService.sendEmail("TogetherToTheTop", email, "Verify Code", email)) {

                model.addAttribute("code", code);
                model.addAttribute("email", email);

                return "/passwordRetrieve";
            } else {
                String message = "Cannot send verification code";
                String nextPage = "/sendVerifyCode";

                model.addAttribute("nextPage", nextPage);
                model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                model.addAttribute("message", message);

                return "error/generic";
            }
        } else {
            String message = "enter valid EMAIL";
            String nextPage = "/sendVerifyCode";

            model.addAttribute("nextPage", nextPage);
            model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("message", message);

            return "error/generic";
        }
    }

    @GetMapping("/sendVerifyCode")
    public String sendVerifyCodePage() {
        return "/sendCode";
    }
}
