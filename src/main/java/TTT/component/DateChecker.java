package TTT.component;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class DateChecker {
    TripDAO tripDAO = new TripDAO();
    CustomUserDAO customUserDAO = new CustomUserDAO();

    @Scheduled(fixedRate = 60000 * 10) // 60000 = 1 minute // Co 6 godzin 6 * 60 * 60 * 1000
    public void checkDatesToRateUsersAndVisible() {
        System.out.println("checking dates!");
        List<Trip> entities = tripDAO.listAllAnnouncements();

        for (Trip entity : entities) {

            if (entity.isTripVisible()) {
                //load all participants of trip
                List<CustomUser> participants = tripDAO.findTripID(entity.getId()).getParticipants();
                System.out.println(participants.size());
                double number = Double.parseDouble(entity.getDistanceOfTrip().substring(0, entity.getDistanceOfTrip().length() - 3));

                if (entity.getTripDateTime().isBefore(LocalDateTime.now())) {
                    //change number of trips of participants
                    for (CustomUser participant : participants) {

                        participant.setDistanceTraveled(participant.getDistanceTraveled() + number);
                        participant.setNumbersOfTrips(participant.getNumbersOfTrips() + 1);
                        customUserDAO.saveUser(participant);
                    }

                    CustomUser owner = entity.getOwner();
                    customUserDAO.updateUserStats(1, owner.getEmail(), "numberOfAnnouncements");
                    customUserDAO.updateUserStats(1, owner.getEmail(), "numbersOfTrips");
                    customUserDAO.updateUserStats(number, owner.getEmail(), "distanceTraveled");

                    entity.setTripVisible(false);
                    tripDAO.updateTrip(entity);
                }
            }
        }
    }
}
