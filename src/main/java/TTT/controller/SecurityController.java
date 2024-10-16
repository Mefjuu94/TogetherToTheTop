package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class SecurityController {

    private final CustomUserDAO dao = new CustomUserDAO();

    @GetMapping("/login")
    public String getLoginPage(){
        return "security/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("customUser", new CustomUser());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerUser(CustomUser customUser, BindingResult bindingResult){


        if (customUser.getPassword().length() < 8){
            return "index";
        }

        dao.saveUser(customUser);
        // if has errors, return customUser to register form
        if (bindingResult.hasErrors()) {
            return "security/register";
        }
      
        return "index";
    }
    
}
