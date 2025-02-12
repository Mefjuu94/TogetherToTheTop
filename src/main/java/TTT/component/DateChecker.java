package TTT.component;

import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DateChecker {
    private final TripDAO tripDAO;
    private final CustomUserDAO customUserDAO;
    private final UserRatingDAO userRatingDAO;

    @Autowired
    public DateChecker(TripDAO tripDAO, CustomUserDAO customUserDAO, UserRatingDAO userRatingDAO) {
        this.tripDAO = tripDAO;
        this.customUserDAO = customUserDAO;
        this.userRatingDAO = userRatingDAO;
    }

    @Transactional
    @Scheduled(fixedRate = 60000 * 10) // 60000 = 1 minute // Co 6 godzin 6 * 60 * 60 * 1000
    public void checkDatesToCreateRatesAndUpdateTrips() {
        List<Trip> entities = tripDAO.listAllAnnouncements();

        for (Trip entity : entities) {

            if (entity.isTripVisible()) {
                //load all participants of trip
                List<CustomUser> participants = tripDAO.findTripID(entity.getId()).getParticipants();
                CustomUser owner = entity.getOwner();
                participants.add(owner); // add owner to list of people to change stats and create rate

                double number = Double.parseDouble(entity.getDistanceOfTrip().substring(0, entity.getDistanceOfTrip().indexOf("km")));

                if (entity.getTripDateTime().isBefore(LocalDateTime.now())) {
                    customUserDAO.updateUserAfterTrip(participants, number);
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
