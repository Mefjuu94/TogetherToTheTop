import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
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
import java.util.Arrays;
import java.util.List;

@Testcontainers
public class TripDAOTests {
    CustomUserDAO customUserDAO;
    private Trip testTrip = createTestTrip();
    private final CustomUser customUser = createTestUser();
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
        testObject = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
    }

    private CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city",0);
        return customUser;
    }

    private Trip createTestTrip() {
        return new Trip.TripBuilder()
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
    }

    @Test
    public void addAnnouncementTest() {
        Assertions.assertTrue(testObject.addAnnouncement(testTrip));
    }

    @Test
    public void addAnnouncementTestFail() {
        Trip trip = null;
        Assertions.assertFalse(testObject.addAnnouncement(trip));
    }

    @Test
    public void findTripTest() {
        addAnnouncementTest();
        List<Trip> tripList = testObject.findTrip("destination");
        tripList.get(0).setId(0);

        List<Trip> list = new ArrayList<>();
        list.add(testTrip);
        testTrip.setTripDateTime(tripList.get(0).getTripDateTime()); // because of delay in time
        Assertions.assertEquals(testTrip.toString(), tripList.get(0).toString());
    }

    @Test
    public void findTripTestFail() {
        List<Trip> list = new ArrayList<>();
        Assertions.assertEquals(list, testObject.findTrip("dest"));
    }

    @Test
    public void deleteTripTest() {
        addAnnouncementTest();
        Assertions.assertTrue(testObject.deleteTrip(testTrip));
    }

    @Test
    public void deleteTripTestFail() {
        Assertions.assertFalse(testObject.deleteTrip(null));
    }

    @Test
    public void listAllAnnouncementsTest() {
        testObject.addAnnouncement(testTrip);
        List<Trip> tripList = testObject.findTrip(testTrip.getDestination());

        Assertions.assertEquals(testObject.listAllAnnouncements().toString(), tripList.toString());
    }


    @Test
    public void findTripIDTest() {
        testObject.addAnnouncement(testTrip);

        List<Trip> tripList = testObject.listAllAnnouncements();

        Trip trip = tripList.get(0);

        Assertions.assertEquals(trip.toString(), testObject.findTripID(trip.getId()).toString());
    }

    @Test
    public void findTripIDTestFail() {
        testObject.addAnnouncement(testTrip);

        Assertions.assertNull(testObject.findTripID(2));
    }

    @Test
    public void updateTripTest() {
        testObject.addAnnouncement(testTrip);
        List<Trip> tripList = testObject.listAllAnnouncements();

        Trip trip = tripList.get(0);
        trip.setDistanceOfTrip("13h");

        Assertions.assertTrue(testObject.updateTrip(trip));
    }

    @Test
    public void updateTripTestFail() {
        Trip trip = null;
        Assertions.assertFalse(testObject.updateTrip(trip));
    }


    @Test
    public void listAllAnnouncementsByUserIdTestFail() {
        Trip trip = null;

        Assertions.assertFalse(testObject.updateTrip(trip));
    }

    @Test
    public void listAllAnnouncementsByUserIdTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(createTestUser());
        testTrip.setOwner(customUser);
        testObject.addAnnouncement(testTrip);

        List<Trip> tripList = new ArrayList<>();
        testTrip.setId(1L);
        tripList.add(testTrip);

        List<Trip> result = testObject.listAllAnnouncementsByUserId(customUser.getId());
        result.get(0).setTripDateTime(testTrip.getTripDateTime());

        Assertions.assertEquals(tripList.toString(),result.toString()); //"contents are identical" -> to string works
    }

    @Test
    public void listAllTripParticipantIdsTest() {
        //first add custom users to database and to participants list
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));

        //new participants list
        List<CustomUser> customUserList = new ArrayList<>();
        //create participants
        CustomUser customUser1 = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city",0);
        CustomUser customUser2 = new CustomUser(2, "testemail@mail.com", "testUser123!",
                "testUser", 19, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "Test city",0);
        //add to list
        customUserList.add(customUser1);
        customUserList.add(customUser2);
        //add to database
        customUserDAO.saveUser(customUser1);
        customUserDAO.saveUser(customUser2);
        //set list
        testTrip.setParticipants(customUserList);
        // add trip with participants
        testObject.addAnnouncement(testTrip);

        //now read array of objects (trip and customUser classes)
        List<Object[]> objects = testObject.listAllTripParticipantIds();
        List<String> tripsAndIDs = new ArrayList<>();

        for (Object[] obj : objects) {
            String s1 = Arrays.toString(obj);
            String s = s1.replaceAll("[\\[\\]\\s]", "");
            String[] split = s.split(",");
            String tripID = split[0];
            String userID = split[1];
            tripsAndIDs.add(userID + "," + tripID);
        }

        List<String> results = new ArrayList<>();
        results.add("1,1"); // user id + "," + trip id
        results.add("2,1");

        Assertions.assertEquals(results, tripsAndIDs);
    }
}
