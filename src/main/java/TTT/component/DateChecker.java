package TTT.component;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DateChecker {
    TripDAO tripDAO = new TripDAO();
    CustomUserDAO customUserDAO = new CustomUserDAO();
    UserRatingDAO userRatingDAO = new UserRatingDAO();

    @Transactional
    @Scheduled(fixedRate = 60000 * 10) // 60000 = 1 minute // Co 6 godzin 6 * 60 * 60 * 1000
    public void checkDatesToRateUsersAndVisible() {
        List<Trip> entities = tripDAO.listAllAnnouncements();

        for (Trip entity : entities) {

            if (entity.isTripVisible()) {
                //load all participants of trip
                List<CustomUser> participants = tripDAO.findTripID(entity.getId()).getParticipants();
                CustomUser owner = entity.getOwner();
                participants.add(owner); // add owner to list of people to change stats and create rate

                double number = Double.parseDouble(entity.getDistanceOfTrip().substring(0, entity.getDistanceOfTrip().indexOf("km")));

                if (entity.getTripDateTime().isBefore(LocalDateTime.now())) {
                    customUserDAO.editUsersChanges(participants, number);
                    entity.setTripVisible(false);

                    for (int i = 0; i < participants.size(); i++) {
                        for (int j = 0; j < participants.size(); j++) {
                            if (participants.get(i) != participants.get(j)) {
                                userRatingDAO.addRate(new UserRating(0, entity, false, "", participants.get(i), participants.get(j)));
                            }
                        }
                    }
                    tripDAO.updateTrip(entity);
                }
            }
        }
    }
}
