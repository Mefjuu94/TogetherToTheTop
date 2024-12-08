import TTT.databaseUtils.CustomUserDAO;
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
public class CustomUserDAOTests {
    private CustomUserDAO testObject;
    private final CustomUser testCustomUser = createTestUser();
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    @Container
    PostgreSQLContainer postgresqlContainer;

    public CustomUserDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer) (new PostgreSQLContainer(this.postgres)).
                withDatabaseName("test_container").withUsername("test").withPassword("test").
                withReuse(true);
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        this.testObject = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    protected CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        return customUser;
    }

    @Test
    public void saveCustomUserTest() {
        Assertions.assertTrue(this.testObject.saveUser(this.testCustomUser));
    }

    @Test
    public void saveCustomUserTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertFalse(this.testObject.saveUser(this.testCustomUser));
    }

    @Test
    public void saveCustomUserTestFailPassword() {
        CustomUser customUser = new CustomUser(9999, "test@mail.com", "123",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        Assertions.assertFalse(this.testObject.saveUser(customUser));
    }

    @Test
    public void saveCustomUserTestFailEmptyUser() {
        CustomUser customUser = new CustomUser();
        Assertions.assertFalse(this.testObject.saveUser(customUser));
    }

    @Test
    public void listAllUsersTest() {
        this.testObject.saveUser(this.testCustomUser);
        List<CustomUser> list = new ArrayList<>();
        list.add(testCustomUser);

        List<CustomUser> listFromDAO = testObject.listAllUsers();

        Assertions.assertEquals(list.toString(), listFromDAO.toString());
    }

    @Test
    public void listAllUsersTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        List<CustomUser> list = new ArrayList<>();
        list.add(testCustomUser);
        list.add(new CustomUser());

        List<CustomUser> listFromDAO = testObject.listAllUsers();

        Assertions.assertNotEquals(list, listFromDAO);
    }


    @Test
    public void findCustomUserByEmailTest() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertEquals(this.testCustomUser.getEmail(), this.testObject.findCustomUserByEmail("test@mail.com").getEmail());
    }

    @Test
    public void findCustomUserByEmailTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertNull(this.testObject.findCustomUserByEmail(" "));
    }

    @Test
    public void findCustomUserByIDTest() {
        this.testObject.saveUser(this.testCustomUser);
        CustomUser customUserId = testObject.findCustomUserByID("1");
        Assertions.assertEquals(testCustomUser.getId(), customUserId.getId());
    }

    @Test
    public void findCustomUserByIDTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertNull(testObject.findCustomUserByID("999"));
    }

    @Test
    public void findCustomUserByNameTest() {
        testObject.saveUser(testCustomUser);
        List<CustomUser> list = new ArrayList<>();
        list.add(testCustomUser);

        Assertions.assertEquals(list.toString(), testObject.findCustomUserByName("testUser").toString());
    }

    @Test
    public void findCustomUserByNameTestFail() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        this.testObject.saveUser(customUser);
        List<CustomUser> result = testObject.findCustomUserByName("testUser");

        List<CustomUser> notExpectedList = new ArrayList<>();

        Assertions.assertEquals(notExpectedList, result);
    }


    @Test
    public void deleteCustomUserTest() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertTrue(this.testObject.deleteCustomUser("test@mail.com"));
    }

    @Test
    public void deleteCustomUserTestFail() {
        Assertions.assertFalse(this.testObject.deleteCustomUser((String) null));
        Assertions.assertFalse(this.testObject.deleteCustomUser("  "));
    }

    @Test
    public void updateUserTripsTest() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        Trip trip = new Trip.TripBuilder()
                .withTripDescription("description")
                .withDestination("destination")
                .withOwner(customUser)
                .withTripDuration("5h")
                .withClosedGroup(false)
                .withAmountOfClosedGroup(0)
                .withDriverPeople(false)
                .withAmountOfDriverPeople(0)
                .withAnimals(false)
                .withWaypoints("ok")
                .withTripDataTime(LocalDateTime.now())
                .withDistanceOfTrip("distance of trip")
                .withGpxFile(null)
                .build();
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        customUser.setTripsOwned(tripList);
        this.testObject.saveUser(customUser);

        Assertions.assertTrue(testObject.updateUserTrips("test@mail.com", tripList));
    }

    @Test
    public void updateUserTripsTestFailInvalidEmail() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        Trip trip = new Trip.TripBuilder()
                .withTripDescription("description")
                .withDestination("destination")
                .withOwner(customUser)
                .withTripDuration("5h")
                .withClosedGroup(false)
                .withAmountOfClosedGroup(0)
                .withDriverPeople(false)
                .withAmountOfDriverPeople(0)
                .withAnimals(false)
                .withWaypoints("ok")
                .withTripDataTime(LocalDateTime.now())
                .withDistanceOfTrip("distance of trip")
                .withGpxFile(null)
                .build();
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        customUser.setTripsOwned(tripList);
        this.testObject.saveUser(customUser);

        Assertions.assertFalse(testObject.updateUserTrips("mail.com", tripList));
    }


    @Test
    public void updateUserAgeTest() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        Assertions.assertTrue(this.testObject.updateUserAge("test@mail.com", 90));
        Assertions.assertEquals(testObject.findCustomUserByEmail("test@mail.com").getAge(), 90);
    }

    @Test
    public void updateUserAgeTestFail() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        Assertions.assertFalse(this.testObject.updateUserAge("mail.com", 90)); //invalid email
        Assertions.assertFalse(this.testObject.updateUserAge("test@mail.com", 150)); //invalid Age > 120
        Assertions.assertNotEquals(testObject.findCustomUserByEmail("test@mail.com").getAge(), 90);
    }


    @Test
    public void updateUserStatsTest() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        Assertions.assertTrue(testObject.updateUserStats(2, "test@mail.com","numberOfAnnouncements"));
        Assertions.assertEquals(testObject.findCustomUserByEmail("test@mail.com").getNumbersOfAnnoucements(),2);
    }

    @Test
    public void updateUserStatsTestFail() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        Assertions.assertFalse(testObject.updateUserStats(2, "mail.com","numberOfAnnouncements")); //no user = null
        Assertions.assertFalse(testObject.updateUserStats(-2, "test@mail.com","numberOfAnnouncements"));
        Assertions.assertFalse(testObject.updateUserStats(-2, "test@mail.com","something"));
    }

    @Test
    public void updateUserFieldEmailTest(){
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        //email
        Assertions.assertTrue(testObject.updateUserField("mail@mail.com","test@mail.com","email"));
    }

    @Test
    public void updateUserFieldUsernameTest(){
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        //username
        Assertions.assertTrue(testObject.updateUserField("MyNewName","test@mail.com","username"));
    }

    @Test
    public void updateUserFieldCityTest(){
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        //city
        Assertions.assertTrue(testObject.updateUserField("Warsaw","test@mail.com","city"));
    }

    @Test
    public void updateUserFieldTestFail(){
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "Adam", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        testObject.saveUser(customUser);
        //email
        Assertions.assertFalse(testObject.updateUserField("mail.test@mail.com","mail.com","email")); //invalid email
        //username
        Assertions.assertFalse(testObject.updateUserField("MyNewName","test@mail.com","user")); // invalid field
        //city
        Assertions.assertFalse(testObject.updateUserField(null,"test@mail.com","city")); // null value
    }

}
