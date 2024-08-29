package TTT.controller;

import TTT.beans.User;
import TTT.security.CustomUserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private User user;

    @PostMapping("/addUser")
    public String saveUser(){
        return "";
    }


}
