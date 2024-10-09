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

    @GetMapping("/announcement")
    public String getAnnouncementsPage() {
        return "announcement";
    }



//    @PostMapping("/updateName")
//    public String updateName(@RequestParam String email, @RequestParam String newName) {
//        CustomUserDAO.updateUserName(email, newName);
//        return "redirect:/profile"; // lub inna strona po aktualizacji
//    }

}
