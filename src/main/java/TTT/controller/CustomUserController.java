package TTT.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomUserController {


    @GetMapping("/user")
    public String getMainPage() {
        return "user";
    }

    @GetMapping("/map")
    public String getMapPage(Model model) {
        ArrayList<String> arr1 = new ArrayList<>();
        arr1.add("moje koordynaty");
        ArrayList<String> arr2 = new ArrayList<>();
        ArrayList<String> arr3 = new ArrayList<>();

        model.addAttribute("arr1", arr1);
        model.addAttribute("arr2", arr2);
        model.addAttribute("arr3", arr3);

        return "map";
    }

    @PostMapping("/sendData")
    public String printData(@RequestParam("waypoints") String waypoints,
                          @RequestParam("coordinatesOfTrip") String coordinatesOfTrip,
                          @RequestParam("allRouteDuration") String allRouteDuration) {

        System.out.println("Otrzymano dane: ");
        System.out.println("zmienna 1: " + waypoints);
        System.out.println("zmienna 2: " + coordinatesOfTrip);
        System.out.println("Zmienna 3: " + allRouteDuration);
        return "sendData";
    }

    @GetMapping("/about")
    public String getAboutProjectPage() {
        return "about";
    }

    // stworzyć widok w którym będziesz miał dwa guziki (edytowane)
//     [20:15]
// dodać kontrolery
//     [20:15]
// w momencie kiedy klikniesz guzik ma być:
//     <button th:onclick=""${'myFunction(' + id + ');'}">Jestem guzikiem</button>
// [20:16]
// myFunction ma przypisać do 3 arralist stringów listy Stringów z JS
// [20:16]
// drugi guzik ma drukować wartości z Twojego modelu

}
