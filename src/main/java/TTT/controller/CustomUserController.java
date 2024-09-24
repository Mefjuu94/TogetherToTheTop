package TTT.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomUserController {


    @GetMapping("/user")
    public String getMainPage() {
        return "user";
    }

    @GetMapping("/map")
    public String getMapPage() {
        return "map";
    }

    @GetMapping("/about")
    public String getAboutProjectPage() {
        return "about";
    }


}
