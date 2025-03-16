import TTT.component.DateChecker;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DateCheckerTest {

    private DateChecker testObject = new DateChecker(DAOFactoryForMockTests.getTripDAO(), DAOFactoryForMockTests.getCustomUserDAO(), DAOFactoryForMockTests.getUserRatingDAO());
    private final Trip testTrip = EntityFactoryForTests.createTestTrip();
    private final CustomUser reviewer = EntityFactoryForTests.createTestUser();
    private final CustomUser user = EntityFactoryForTests.createTestUser();

    @BeforeEach
    public void setUp() {
        DAOFactoryForMockTests.clearDatabase();
        //clear database and add object - 2 users and trip.
        reviewer.setEmail("reviewer@mail.com");
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(reviewer);
        user.setEmail("user@mail.com");
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(user);
        long id1 = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        long id2 = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        reviewer.setId(id1);
        user.setId(id2);
        testTrip.setOwner(reviewer);
        testTrip.setTripDateTime(LocalDateTime.now());
        DAOFactoryForMockTests.getTripDAO().addAnnouncement(testTrip);
    }

    @Test
    public void checkDatesAndVisibleOfTrip(){
        testObject.checkDatesToCreateRatesAndUpdateTrips();
        Trip trip = DAOFactoryForMockTests.getTripDAO().listAllAnnouncements().get(0);

        Assertions.assertFalse(trip.isTripVisible());
        Assertions.assertFalse(LocalDateTime.now().isBefore(trip.getTripDateTime()));
    }


}
