package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MapController {

    TripDAO tripDAO = new TripDAO();

    @PostMapping("/sendData")
    public String createTrip(@RequestParam("waypoints") String waypoints,
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

        Trip trip = new Trip.TripBuilder()
                .withTripDescription(description)
                .withDestination("Destination...")
                .withCustomUser(customUser)
                .withTripDuration(allRouteDuration)
                .withClosedGroup(Boolean.parseBoolean(isCheckedGroup))
                .withAmountOfClosedGroup(Integer.parseInt(amountOfPeopleInGroup))
                .withDriverPeople(Boolean.parseBoolean(driverCheck))
                .withAmountOfDriverPeople(Integer.parseInt(amountOfPeopleDriver))
                .build();

        tripDAO.addAnnouncement(trip);

        //todo add distance

        return "sendData";
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
