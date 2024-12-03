package TTT.component;

import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DateChecker {
    TripDAO tripDAO = new TripDAO();

    @Scheduled(fixedRate = 60000 * 15) // 60000 = 1 minute // Co 6 godzin 6 * 60 * 60 * 1000
    public void checkDatesToRateUsersAndVisible() {
        System.out.println("checking dates!");
        List<Trip> entities = tripDAO.listAllAnnouncements();

        for (Trip entity : entities) {
            if (entity.getTripDateTime() == null) {
                entity.setTripVisible(false);
            }
            if (entity.getTripDateTime().isBefore(LocalDateTime.now())) {
                entity.setTripVisible(false);
                tripDAO.updateTrip(entity);
                System.out.println("Trip id: " + entity.getId() + " will be no longer avaible because is after date");
            }
        }
    }
}
