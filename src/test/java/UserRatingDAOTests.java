import TTT.databaseUtils.UserRatingDAO;
import TTT.trips.Trip;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
public class UserRatingDAOTests {

    private UserRatingDAO testObject;
    private final Trip testTrip = EntityFactoryForTests.createTestTrip();
    private final CustomUser reviewer = EntityFactoryForTests.createTestUser();
    private final CustomUser user = EntityFactoryForTests.createTestUser();

    @BeforeEach
    public void setUp() {
        DAOFactoryForMockTests.clearDatabase();
        this.testObject = DAOFactoryForMockTests.getUserRatingDAO();
        reviewer.setEmail("reviewer@mail.com");
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(reviewer);
        user.setEmail("user@mail.com");
        DAOFactoryForMockTests.getCustomUserDAO().saveUser(user);
        long id1 = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        long id2 = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        reviewer.setId(id1);
        user.setId(id2);
        testTrip.setOwner(reviewer);
        DAOFactoryForMockTests.getTripDAO().addAnnouncement(testTrip);
    }

    private UserRating createRate(){
        UserRating userRating = EntityFactoryForTests.createRate();
        testTrip.setId(DAOFactoryForMockTests.getTripDAO().listAllAnnouncements().get(0).getId());
        userRating.setComment("");
        userRating.setRating(1);
        userRating.setTrip(testTrip);
        userRating.setUser(user);
        userRating.setReviewer(reviewer);
        return userRating;
    }

    @Test
    public void addRateTest(){
        Assertions.assertTrue(testObject.addRate(createRate()));
    }

    @Test
    public void addRateTestFail(){
        Assertions.assertFalse(testObject.addRate(null));
    }

    @Test
    public void editRateTest(){
        testObject.addRate(createRate());
        long rateId = testObject.listAllRatings().get(0).getId();

        Assertions.assertTrue(testObject.editRate(rateId,4,testTrip,"ok"));
    }

    @Test
    public void editRateTestFail() {
        testObject.addRate(createRate());
        Assertions.assertFalse(testObject.editRate(0L,4,testTrip,"ok"));
    }


    @Test
    public void listAllRatingsTest(){
        testObject.addRate(createRate());
        UserRating userRating = createRate();
        long rateId = testObject.listAllRatings().get(0).getId();
        userRating.setId(rateId);

        List<UserRating> expected = new ArrayList<>();
        expected.add(userRating);

        Assertions.assertEquals(expected.toString(),testObject.listAllRatings().toString());

    }

    @Test
    public void listAllARatingsTestEmptyList(){
        Assertions.assertEquals(new ArrayList<UserRating>(),testObject.listAllRatings());
    }

    @Test
    public void listUsersToRateByMeTest(){
        testObject.addRate(createRate());
        UserRating userRating = createRate();
        long rateId = testObject.listAllRatings().get(0).getId();
        userRating.setId(rateId);

        long myId = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();
        System.out.println(DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getCustomUserName());

        UserRating rating = testObject.listAllRatings().get(0);

        List<UserRating> expected = new ArrayList<>();
        expected.add(rating);

        Assertions.assertEquals(expected.toString(),testObject.listUsersToRateByMe(myId).toString());
    }

    @Test
    public void listUsersToRateByMeTestEmptyListFail(){
        testObject.addRate(createRate());

        Assertions.assertEquals(new ArrayList<>().toString(),testObject.listUsersToRateByMe(0).toString());
    }

    @Test
    public void listAllRatesTest(){
        testObject.addRate(createRate());
        UserRating userRating = createRate();
        long rateId = testObject.listAllRatings().get(0).getId();
        userRating.setId(rateId);

        long myId = DAOFactoryForMockTests.getCustomUserDAO().listAllUsers().get(0).getId();

        UserRating userRate = testObject.listAllRatings().get(0);
        userRate.setFilled(true);
        testObject.editRate(userRate.getId(),5, DAOFactoryForMockTests.getTripDAO().listAllAnnouncements().get(0),"ok");

        UserRating rating = testObject.listAllRatings().get(0);

        List<UserRating> expected = new ArrayList<>();
        expected.add(rating);

        Assertions.assertEquals(expected.toString(),testObject.listAllRates(myId).toString());
    }

    @Test
    public void listAllRatesTestFail(){
        testObject.addRate(createRate());
        UserRating userRating = createRate();
        long rateId = testObject.listAllRatings().get(0).getId();
        userRating.setId(rateId);

        UserRating userRate = testObject.listAllRatings().get(0);
        userRate.setFilled(true);
        testObject.editRate(userRate.getId(),5, DAOFactoryForMockTests.getTripDAO().listAllAnnouncements().get(0),"ok");

        UserRating rating = testObject.listAllRatings().get(0);

        List<UserRating> expected = new ArrayList<>();
        expected.add(rating);

        Assertions.assertNotEquals(expected.toString(),testObject.listAllRates(0).toString());
    }

}
