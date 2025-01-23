package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

    private final CustomUserDAO dao = new CustomUserDAO();

    @GetMapping("/login")
    public String getLoginPage() {
        return "/";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "logout";
    }

    @PostMapping("/register")
    public String registerUser(CustomUser customUser, BindingResult bindingResult) {


//        if (customUser.getPassword().length() < 8) {
//            return "index";
//        }

        dao.saveUser(customUser);
        // if has errors, return customUser to register form
        if (bindingResult.hasErrors()) {
            return "security/register";
        }

        return "index";
    }

}
