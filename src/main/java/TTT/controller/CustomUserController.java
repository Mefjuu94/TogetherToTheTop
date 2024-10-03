package TTT.controller;


import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

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
    public String collectData(@RequestParam("waypoints") String waypoints,
                            @RequestParam("coordinatesOfTrip") String coordinatesOfTrip,
                            @RequestParam("allRouteDuration") String allRouteDuration,
                            @RequestParam("descriptionOfTrip") String description,
                            @RequestParam("driverCheck") String driverCheck,
                            @RequestParam("amountOfPeopleDriver") String amountOfPeopleDriver,
                            @RequestParam("isCheckedAnimals") String isCheckedAnimals,
                            @RequestParam("isCheckedGroup") String isCheckedGroup,
                            @RequestParam("amountOfPeopleInGroup") String amountOfPeopleInGroup) {

        System.out.println("Otrzymano dane: ");
        System.out.println("zmienna 1: " + waypoints);
        System.out.println("zmienna 2: " + coordinatesOfTrip);
        System.out.println("Zmienna 3: " + allRouteDuration);

        System.out.println("Zmienna 4 opis: " + description);
        System.out.println("Zmienna 5 driver?: " + driverCheck);
        System.out.println("Zmienna 6 liczba osób w samochodzie: " + amountOfPeopleDriver);
        System.out.println("Zmienna 7 zwierzaki?: " + isCheckedAnimals);
        System.out.println("Zmienna 8 grupa zamknięta?: " + isCheckedGroup);
        System.out.println("Zmienna 9 ile osób w grupie: " + amountOfPeopleInGroup);

        String userEmail = getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();
        CustomUser customUser = customUserDAO.findCustomUser(userEmail);
        System.out.println(customUser.getCustomUserName());

        TripDAO tripDAO = new TripDAO();
        //  int amountOfClosedGroup, boolean driverPeople, ...)
        tripDAO.addAnnouncement(new Trip(description,"destination",customUser,allRouteDuration,Boolean.parseBoolean(isCheckedGroup),Integer.parseInt(amountOfPeopleInGroup),
                Boolean.parseBoolean(driverCheck),Integer.parseInt(amountOfPeopleDriver),new ArrayList<>()));

        return "sendData";
    }

//    @PostMapping("/updateName")
//    public String updateName(@RequestParam String email, @RequestParam String newName) {
//        CustomUserDAO.updateUserName(email, newName);
//        return "redirect:/profile"; // lub inna strona po aktualizacji
//    }

    @GetMapping("/about")
    public String getAboutProjectPage() {
        return "about";
    }

    public String getLoggedInUserName() {
        // Pobierz aktualnego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Sprawdź, czy użytkownik jest instancją UserDetails
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername(); // Zwróć nazwę użytkownika
            }
            // Jeśli nie, możesz zwrócić inne dane
            return principal.toString(); // To także może być nazwa użytkownika
        }
        return null; // Jeśli użytkownik nie jest zalogowany
    }

}
