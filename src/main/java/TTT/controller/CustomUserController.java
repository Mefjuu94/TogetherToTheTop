package TTT.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomUserController {


    @GetMapping("/user")
    public String getMainPage() {
        return "user";
    }


}
