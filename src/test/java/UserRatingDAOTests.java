import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import TTT.users.CustomUser;
import TTT.users.UserRating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
public class UserRatingDAOTests {

    private CustomUser reviewer = new CustomUser(1, "reviewer@mail.com", "testUser123!",
            "reviewerTestUser", 99, 0, 0, new ArrayList<>(),
            new ArrayList<>(), "reviewerCity");;
    ///////////////////
    CustomUserDAO customUserDAO;
    private final CustomUser customUser = createTestUser();
    UserRating rate = createRate();
    private UserRatingDAO testObject;
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    @Container
    PostgreSQLContainer postgresqlContainer;

    public UserRatingDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer) (new PostgreSQLContainer(this.postgres)).
                withDatabaseName("test_container").withUsername("test").withPassword("test").
                withReuse(true);
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        testObject = new UserRatingDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    private CustomUser createTestUser() {
        CustomUser customUser = new CustomUser(1, "test@mail.com", "testUser123!",
                "testUser", 99, 0, 0, new ArrayList<>(),
                new ArrayList<>(), "city");
        return customUser;
    }

    protected UserRating createRate() {
        UserRating rate = new UserRating(8,"comment",customUser,customUser);
        return rate;
    }

    @Test
    public void addRateTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);

        Assertions.assertTrue(testObject.addRate(createRate()));
    }

    @Test
    public void addRateTestFail(){
        Assertions.assertThrows(IllegalStateException.class,()->testObject.addRate(rate));
    }

    @Test
    public void editRateTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        customUserDAO.saveUser(customUser);
        rate.setComment("new rate here!");
        testObject.addRate(rate);

        Assertions.assertTrue(testObject.editRate(1L,rate));
    }

    @Test
    public void editRateTestFail(){
        Assertions.assertThrows(IllegalStateException.class,()->testObject.editRate(1L,rate));
    }


    @Test
    public void listAllARatingsTest(){
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        customUserDAO = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));

        customUserDAO.saveUser(customUser);

        testObject.addRate(rate);

        List<UserRating> resultExpected = new ArrayList<>();
        rate.setId(1L);
        resultExpected.add(rate);

        Assertions.assertEquals(resultExpected.toString(),testObject.listAllARatings().toString());
    }

    @Test
    public void listAllARatingsTestEmptyList(){
        Assertions.assertEquals(new ArrayList<UserRating>(),testObject.listAllARatings());
    }

}
