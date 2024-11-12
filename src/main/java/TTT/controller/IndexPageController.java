package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexPageController {

    CustomUserDAO customUserDAO = new CustomUserDAO();

    @GetMapping("/index")
    public String getAnnouncementsPage(Model model) {
        List<CustomUser> users = customUserDAO.listAllUsers();

        // Przekazanie listy użytkowników do modelu
        model.addAttribute("users", users);

        return "index";  // Renderowanie strony index.html
    }
}

