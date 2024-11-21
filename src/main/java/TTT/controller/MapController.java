package TTT.controller;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.GPX;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Controller
public class MapController {

    TripDAO tripDAO = new TripDAO();
    MethodsHandler methodsHandler = new MethodsHandler();

    @PostMapping("/sendData")
    public String createTrip(@RequestParam("waypoints") String waypoints,
                             @RequestParam("allRouteDuration") String allRouteDuration,
                             @RequestParam("descriptionOfTrip") String description,
                             @RequestParam("driverCheck") String driverCheck,
                             @RequestParam("amountOfPeopleDriver") String amountOfPeopleDriver,
                             @RequestParam("isCheckedAnimals") String isCheckedAnimals,
                             @RequestParam("isCheckedGroup") String isCheckedGroup,
                             @RequestParam("amountOfPeopleInGroup") String amountOfPeopleInGroup,
                             @RequestParam("destination") String destination,
                             @RequestParam("distanceOfTrip") String distanceOfTrip,
                             @RequestParam("date") String date,
                             @RequestParam("jsonGeometryWaypoints") String jsonGeometryWaypoints,
                             @RequestParam("waypointsLength") String waypointsLength,
                             Model model) throws IOException {

        LocalDateTime dateTime = LocalDateTime.parse(date);
        String userEmail = methodsHandler.getLoggedInUserName();
        CustomUserDAO customUserDAO = new CustomUserDAO();
        CustomUser customUser = customUserDAO.findCustomUserByEmail(userEmail);
        String userID = String.valueOf(customUser.getId());
        int tripsCreated = customUser.getNumbersOfTrips() + 1; // get amount of trips created and add one to them
        customUserDAO.updateUserStats(tripsCreated, userEmail, "numberOfAnnouncements");

        int numberOfWaypoints = Integer.parseInt(waypointsLength);

        int amountOfPeople = 0;

        if (amountOfPeopleDriver.equals("")) {
            amountOfPeople = 0;
        } else {
            amountOfPeople = Integer.parseInt(amountOfPeopleDriver);
        }

        GPX gpx = new GPX();
        byte[] gpxFile = null;
        String filePath = "src/main/resources/routes/" + userID + "_" + date.replace(":", "_") + "_route.gpx";
        try {
            gpx.makeGPX(jsonGeometryWaypoints, numberOfWaypoints, filePath);
            gpxFile = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("duration: " + allRouteDuration);
        System.out.println("DISTANCE    : " + distanceOfTrip);

        Trip trip = new Trip.TripBuilder()
                .withTripDescription(description)
                .withDestination(destination)
                .withOwner(customUser)
                .withTripDuration(allRouteDuration)
                .withClosedGroup(Boolean.parseBoolean(isCheckedGroup))
                .withAmountOfClosedGroup(Integer.parseInt(amountOfPeopleInGroup))
                .withDriverPeople(Boolean.parseBoolean(driverCheck))
                .withAmountOfDriverPeople(amountOfPeople)
                .withAnimals(Boolean.parseBoolean(isCheckedAnimals))
                .withWaypoints(waypoints)
                .withTripDataTime(dateTime)
                .withDistanceOfTrip(distanceOfTrip)
                .withGpxFile(gpxFile)
                .build();

        tripDAO.addAnnouncement(trip);

        String nextPage = "/announcement";
        model.addAttribute("nextPage", nextPage);

        return "actionSuccess";
    }

    @GetMapping("/map")
    public String getMapPage() {
        return "map";
    }
}
