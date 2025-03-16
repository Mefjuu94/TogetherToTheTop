import TTT.databaseUtils.CustomUserDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
public class CustomUserDAOTests {
    private CustomUserDAO testObject;
    private final CustomUser testCustomUser = EntityFactoryForTests.createTestUser();

    @BeforeEach
    public void setUp() {
        DAOFactoryForMockTests.clearDatabase();
        this.testObject = DAOFactoryForMockTests.getCustomUserDAO();
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
                new ArrayList<>(), "city",0);
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
        long id = testObject.listAllUsers().get(0).getId();
        testCustomUser.setId(id);
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
        long id = testObject.listAllUsers().get(0).getId();
        CustomUser customUserId = testObject.findCustomUserByID(String.valueOf(id));
        Assertions.assertEquals(id, customUserId.getId());
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
        long id = testObject.listAllUsers().get(0).getId();
        testCustomUser.setId(id);
        list.add(testCustomUser);

        Assertions.assertEquals(list.toString(), testObject.findCustomUserByName("test").toString());
    }

    @Test
    public void findCustomUserByNameTestFail() {
        this.testObject.saveUser(testCustomUser);
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

        Trip trip = EntityFactoryForTests.createTestTrip();
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        testObject.saveUser(testCustomUser);
        testCustomUser.setTripsOwned(tripList);

        Assertions.assertTrue(testObject.updateUserTrips("test@mail.com", tripList));
    }

    @Test
    public void updateUserTripsTestFailInvalidEmail() {

        Trip trip = EntityFactoryForTests.createTestTrip();
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        this.testObject.saveUser(testCustomUser);

        Assertions.assertFalse(testObject.updateUserTrips("testUser@mail.com", tripList));
    }


    @Test
    public void updateUserAgeTest() {
        testObject.saveUser(testCustomUser);
        Assertions.assertTrue(this.testObject.updateUserAge("test@mail.com", 90));
        Assertions.assertEquals(testObject.findCustomUserByEmail("test@mail.com").getAge(), 90);
    }

    @Test
    public void updateUserAgeTestFail() {
        testObject.saveUser(testCustomUser);
        Assertions.assertFalse(this.testObject.updateUserAge("mail.com", 90)); //invalid email
        Assertions.assertFalse(this.testObject.updateUserAge("test@mail.com", 150)); //invalid Age > 120
        Assertions.assertFalse(this.testObject.updateUserAge("test@mail.com", -10)); //invalid Age < 1
        Assertions.assertNotEquals(testObject.findCustomUserByEmail("test@mail.com").getAge(), 90);
    }


    @Test
    public void updateUserStatsTest() {
        testObject.saveUser(testCustomUser);
        Assertions.assertTrue(testObject.updateUserStats(2, "test@mail.com","numberOfAnnouncements"));
        System.out.println(testObject.findCustomUserByEmail("test@mail.com").getNumbersOfAnnouncements());
        Assertions.assertEquals(2,testObject.findCustomUserByEmail("test@mail.com").getNumbersOfAnnouncements());
    }

    @Test
    public void updateUserStatsTestFail() {
        testObject.saveUser(testCustomUser);
        Assertions.assertFalse(testObject.updateUserStats(2, "mail.com","numberOfAnnouncements")); //no user = null
        Assertions.assertFalse(testObject.updateUserStats(-2, "test@mail.com","numberOfAnnouncements"));
        Assertions.assertFalse(testObject.updateUserStats(-2, "test@mail.com","something"));
        //TODO add password
    }

    @Test
    public void updateUserFieldEmailTest(){
        testObject.saveUser(testCustomUser);
        //email
        Assertions.assertTrue(testObject.updateUserField("mail@mail.com","test@mail.com","email"));
    }

    @Test
    public void updateUserFieldUsernameTest(){
        testObject.saveUser(testCustomUser);
        //username
        Assertions.assertTrue(testObject.updateUserField("MyNewName","test@mail.com","username"));
    }

    @Test
    public void updateUserFieldCityTest(){
        testObject.saveUser(testCustomUser);
        //city
        Assertions.assertTrue(testObject.updateUserField("Warsaw","test@mail.com","city"));
    }

    @Test
    public void updateUserFieldPasswordTest(){
        testObject.saveUser(testCustomUser);
        //password
        Assertions.assertTrue(testObject.updateUserField("TestUser1234","test@mail.com","password"));
    }

    @Test
    public void updateUserFieldTestFail(){
        testObject.saveUser(testCustomUser);
        //email
        Assertions.assertFalse(testObject.updateUserField("mail.test@mail.com","mail.com","email")); //invalid email
        //username
        Assertions.assertFalse(testObject.updateUserField("MyNewName","test@mail.com","user")); // invalid field
        //city
        Assertions.assertFalse(testObject.updateUserField(null,"test@mail.com","city")); // null value
        //password
        Assertions.assertFalse(testObject.updateUserField(null,"test@mail.com","password")); // null value
        Assertions.assertFalse(testObject.updateUserField("123abc","test@mail.com","password")); //too short

    }

    @Test
    public void updateUserAfterTripTest(){
        testCustomUser.setRatings(new ArrayList<>());
        testObject.saveUser(testCustomUser);
        List<CustomUser> list = new ArrayList<>();
        list.add(testCustomUser);

        Assertions.assertTrue(testObject.updateUserAfterTrip(list,20));
        list.clear();
        Assertions.assertFalse(testObject.updateUserAfterTrip(list,20));
    }

}
