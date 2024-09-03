package TTT.controller;

import TTT.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CustomUser customUser;

    @PostMapping("/addUser")
    public String saveUser() {
        return "";
    }


}
