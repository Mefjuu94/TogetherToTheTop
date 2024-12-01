import TTT.databaseUtils.CommentsDAO;
import TTT.trips.Comments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class CommentsDAOTests {
    private CommentsDAO testObject;
    private final Comments testComment = createTestComment();
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    @Container
    PostgreSQLContainer postgresqlContainer;

    public CommentsDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer) (new PostgreSQLContainer(this.postgres)).
                withDatabaseName("test_container").withUsername("test").withPassword("test").
                withReuse(true);
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        this.testObject = new CommentsDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    private Comments createTestComment() {
        return new Comments("new comment", 1, 1, "Alex");
    }

    @Test
    public void saveCommentTest() {
        Assertions.assertTrue(this.testObject.addComment(this.testComment));
    }

    @Test
    public void saveCommentTestFail() {
        Assertions.assertFalse(this.testObject.addComment(null));
    }

    @Test
    public void findCommentsByTripIDTest() {
        createTestComment();

        // Assertions.assertArrayEquals();
    }

    @Test
    public void deleteCommentTest() {
        createTestComment();
        Assertions.assertTrue(this.testObject.deleteComment(1));
    }

}
