package TTT.controller;

import TTT.beans.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private User user;

    @GetMapping("/")
    public String getMainPage(){
        return "index";
    }

    @GetMapping("/user")
    public String getuser(Model model){
        model.addAttribute(user);
        return "user";
    }
}
