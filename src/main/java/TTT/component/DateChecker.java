package TTT.component;

import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DateChecker {
    TripDAO tripDAO = new TripDAO();

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // Co 6 godzin
    public void checkDatesToRateUsersAndVisible() {
        List<Trip> entities = tripDAO.listAllAnnouncements();

        for (Trip entity : entities) {
            if (entity.getTripDateTime().isBefore(LocalDateTime.now())) {
                entity.setTripVisible(false);
                System.out.println("Trip id: " + entity.getId() +  " will be no longer avaible because is after date");

                List <CustomUser> participants = entity.getParticipants();

                for (CustomUser participant : participants){
                    
                }

            }
        }


    }
}
