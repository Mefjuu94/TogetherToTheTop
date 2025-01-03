package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexPageController {


    @GetMapping("/")
    public String getEmailsAtIndexPage(Model model) {
    CustomUserDAO customUserDAO = new CustomUserDAO();


        List<CustomUser> usersList = customUserDAO.listAllUsers();
        List<String> users = new ArrayList<>();
        for (CustomUser customUser : usersList) {
            users.add(customUser.getEmail());
        }

        System.out.println(users.size());

        // Przekazanie listy użytkowników do modelu
        model.addAttribute("users", users);

        return "index";  // Renderowanie strony index.html
    }
}

