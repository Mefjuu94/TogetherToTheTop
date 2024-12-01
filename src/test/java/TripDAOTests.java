import TTT.databaseUtils.CommentsDAO;
import TTT.databaseUtils.TripDAO;
import TTT.trips.Comments;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
public class TripDAOTests {
    private final Trip testTrip = createTestTrip();
    private final CustomUserDAOTests customUserDAOTests = new CustomUserDAOTests();
    private final CustomUser customUser = customUserDAOTests.createTestUser();
    TripDAO testObject = new TripDAO();
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    @Container
    PostgreSQLContainer postgresqlContainer;

    public TripDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer) (new PostgreSQLContainer(this.postgres)).
                withDatabaseName("test_container").withUsername("test").withPassword("test").
                withReuse(true);
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        TripDAO testObject = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    private Trip createTestTrip() {
        Trip trip = new Trip.TripBuilder()
                .withTripDescription("description")
                .withDestination("destination")
                .withOwner(customUser)
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
        return trip;
    }

    @Test
    public void addAnnouncementTest(){
        Assertions.assertTrue(testObject.addAnnouncement(testTrip));
    }

    @Test
    public void addAnnouncementTestFail(){
        Trip trip = null;
        Assertions.assertFalse(testObject.addAnnouncement(trip));
    }

    @Test
    public void findTripTest(){
        addAnnouncementTest();
        List<Trip> tripList = testObject.findTrip("destination");
        tripList.get(0).setId(0);

        List<Trip> list = new ArrayList<>();
        list.add(testTrip);
        testTrip.setTripDateTime(tripList.get(0).getTripDateTime()); // because of delay in time
        Assertions.assertEquals(testTrip.toString(),tripList.get(0).toString());
    }

    @Test
    public void findTripTestFail(){
        List<Trip> list = new ArrayList<>();
        Assertions.assertEquals(list,testObject.findTrip("dest"));
    }

}
