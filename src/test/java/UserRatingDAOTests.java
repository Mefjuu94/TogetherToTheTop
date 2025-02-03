import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.junit.jupiter.api.AfterEach;
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
public class UserRatingDAOTests {

    private CustomUser reviewer = new CustomUser(2, "reviewer@mail.com", "testUser123!",
            "reviewerTestUser", 99, 0, 0, new ArrayList<>(),
            new ArrayList<>(), "reviewerCity",0);
    ///////////////////
    CustomUserDAO customUserDAO;
    private final CustomUser customUser = createTestUser();
    UserRating rate = createRate();
    private UserRatingDAO testObject;
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    private Trip testTrip = createTestTrip();
    private TripDAO tripDAO = new TripDAO();
    @Container
    PostgreSQLContainer postgresqlContainer;

    public UserRatingDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer) (new PostgreSQLContainer(this.postgres)).
                withDatabaseName("test_container").withUsername("test").withPassword("test").
                withReuse(true).withEnv("POSTGRES_MAX_CONNECTIONS", "300");
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        testObject = new UserRatingDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    @AfterEach
    public void tearDown() {
        if (testObject != null) {
            testObject.close();
        }
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

    private Trip setTestTripID() {
        testTrip.setId(1L);
        return testTrip;
    }

    private CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city",0);
        customUser.setRatings(new ArrayList<>());
        reviewer.setRatings(new ArrayList<>());
        return customUser;
    }

    protected UserRating createRate() {
        UserRating rate = new UserRating(5,testTrip,false,"comment",customUser,reviewer);
        return rate;
    }

    @Test
    public void addRateTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        setTestTripID();
        tripDAO.addAnnouncement(testTrip);

        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setId(1L);
        rate1.setTrip(testTrip);

        Assertions.assertTrue(testObject.addRate(rate1));
    }

    @Test
    public void addRateTestFail(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        setTestTripID();
        tripDAO.addAnnouncement(testTrip);
        rate.setId(1L);
        rate.setTrip(null);
        Assertions.assertThrows(IllegalStateException.class,()->testObject.addRate(rate));
        rate = null;
        Assertions.assertThrows(IllegalStateException.class,()->testObject.addRate(rate));
    }

    @Test
    public void editRateTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        setTestTripID();
        tripDAO.addAnnouncement(testTrip);

        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setTrip(testTrip);
        rate1.setId(1L);

        testObject.addRate(rate1);

        Assertions.assertTrue(testObject.editRate(1L,rate1.getRating(),testTrip,"ok"));
    }

    @Test
    public void editRateTestFail() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setTrip(testTrip);
        rate1.setId(1L);
        testObject.addRate(rate1);
        Assertions.assertThrows(IllegalStateException.class, () ->
                testObject.editRate(null, rate.getRating(), testTrip, "ok")
        );
    }


    @Test
    public void listAllRatingsTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        Trip trip1 = testTrip;

        trip1.setId(1L);
        trip1.setTripDateTime(LocalDateTime.now());
        tripDAO.addAnnouncement(trip1);

        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setId(1L);
        rate1.setTrip(trip1);

        testObject.addRate(rate1);

        List<UserRating> resultExpected = new ArrayList<>();
        resultExpected.add(rate1);

        Assertions.assertEquals(resultExpected.toString(),testObject.listAllRatings().toString());
    }

    @Test
    public void listAllARatingsTestEmptyList(){
        Assertions.assertEquals(new ArrayList<UserRating>(),testObject.listAllRates(customUser.getId()));
    }

    @Test
    public void listUsersToRateByMeTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        Trip trip1 = testTrip;

        trip1.setId(1L);
        trip1.setTripDateTime(LocalDateTime.now());
        tripDAO.addAnnouncement(trip1);

        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setId(1L);
        rate1.setTrip(trip1);

        testObject.addRate(rate1);

        List<UserRating> resultExpected = new ArrayList<>();
        resultExpected.add(rate1);

        Assertions.assertEquals(resultExpected.toString(),testObject.listUsersToRateByMe(reviewer.getId()).toString());
    }

    @Test
    public void listUsersToRateByMeTestFail(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        customUserDAO.saveUser(reviewer);
        tripDAO = new TripDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        Trip trip1 = testTrip;

        trip1.setId(1L);
        trip1.setTripDateTime(LocalDateTime.now());
        tripDAO.addAnnouncement(trip1);

        UserRating rate1 = new UserRating();
        rate1 = rate;
        rate1.setId(1L);
        rate1.setTrip(trip1);

        testObject.addRate(rate1);

        List<UserRating> resultExpected = new ArrayList<>();
        resultExpected.add(rate1);

        Assertions.assertNotEquals(resultExpected.toString(),testObject.listUsersToRateByMe(customUser.getId()).toString());
        resultExpected = new ArrayList<>();
        Assertions.assertEquals(resultExpected.toString(),testObject.listUsersToRateByMe(customUser.getId()).toString());
    }

}
