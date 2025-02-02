import TTT.component.DateChecker;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class DateCheckerTest {

    @Mock
    private TripDAO tripDAO;  // mock TripDAO

    @InjectMocks
    private DateChecker testObject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CustomUser testUser = createTestUser();

    @Test
    void testCheckDatesToRateUsersAndVisible() {
        List<CustomUser> participants = new ArrayList<>();
        participants.add(testUser);
        Trip trip1 = createTestTrip();
        trip1.setId(1L);
        trip1.setParticipants(participants);
        trip1.setTripDateTime(LocalDateTime.now().minusDays(1));  // Data w przeszłości
        trip1.setTripVisible(true);


        Trip trip2 = createTestTrip();
        trip2.setId(2L);
        trip2.setParticipants(participants);
        trip2.setTripDateTime(LocalDateTime.now().plusDays(1));  // Data w przyszłości
        trip2.setTripVisible(true);

        // Mockowanie listy podróży
        List<Trip> trips = Arrays.asList(trip1, trip2);
        when(tripDAO.listAllAnnouncements()).thenReturn(trips);

        // Mockowanie findTripID dla trip1 i trip2
        when(tripDAO.findTripID(1L)).thenReturn(trip1);
        when(tripDAO.findTripID(2L)).thenReturn(trip2);

        testObject.checkDatesToRateUsersAndVisible();

        // check if trip1 had set tripVisible on false (past)
        verify(tripDAO).updateTrip(trip1);
        assert !trip1.isTripVisible();  // should be false

        // check if trip2 do not change visible status on false, (future)
        verify(tripDAO, never()).updateTrip(trip2);
        assert trip2.isTripVisible();  // should be true
    }

    protected CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city", 0);
        return customUser;
    }

    private Trip createTestTrip() {
        return new Trip.TripBuilder()
                .withTripDescription("description")
                .withDestination("destination")
                .withOwner(testUser)
                .withTripDuration("5h")
                .withClosedGroup(false)
                .withAmountOfClosedGroup(1)
                .withDriverPeople(false)
                .withAmountOfDriverPeople(12)
                .withAnimals(true)
                .withWaypoints("")
                .withTripDataTime(LocalDateTime.now())
                .withDistanceOfTrip("5km")
                .withGpxFile(null)
                .build();
    }
}
