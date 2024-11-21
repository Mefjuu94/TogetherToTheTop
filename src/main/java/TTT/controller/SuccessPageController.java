package TTT.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SuccessPageController {

    @GetMapping("/actionSuccess")
    public String getAnnouncementsPage(@RequestParam String page, Model model) {
        model.addAttribute("page", page); //where to redirect after action
        System.out.println(page);
        return page;
    }
}
