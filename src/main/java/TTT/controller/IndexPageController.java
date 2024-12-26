package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexPageController {


    @GetMapping("index")
    public String getEmailsAtIndexPage(Model model) {
        CustomUserDAO customUserDAO = new CustomUserDAO();

        List<CustomUser> usersList = customUserDAO.listAllUsers();
        List<String> users = new ArrayList<>();
        for (CustomUser customUser : usersList) {
            users.add(customUser.getEmail());
        }

        model.addAttribute("users", users);

        return "index";
    }
}

