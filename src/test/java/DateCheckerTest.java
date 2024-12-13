import TTT.component.DateChecker;

import TTT.databaseUtils.TripDAO;
import TTT.trips.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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

    @Test
    void testCheckDatesToRateUsersAndVisible() {
        Trip trip1 = new Trip();
        trip1.setTripDateTime(LocalDateTime.now().minusDays(1));  // Data w przeszłości
        trip1.setTripVisible(true);

        Trip trip2 = new Trip();
        trip2.setTripDateTime(LocalDateTime.now().plusDays(1));  // Data w przyszłości
        trip2.setTripVisible(true);

        List<Trip> trips = Arrays.asList(trip1, trip2);

        // mocking visible list
        when(tripDAO.listAllAnnouncements()).thenReturn(trips);

        testObject.checkDatesToRateUsersAndVisible();

        // check if trip1 had set tripVisible on false (past)
        verify(tripDAO).updateTrip(trip1);
        assert !trip1.isTripVisible();  // should be false

        // check if trip2 do not change visible status on false, (future)
        verify(tripDAO, never()).updateTrip(trip2);
        assert trip2.isTripVisible();  // should be true
    }


}
