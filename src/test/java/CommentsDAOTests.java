import TTT.databaseUtils.CommentsDAO;
import TTT.trips.Comments;
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
        saveCommentTest();

        List<Comments> list = new ArrayList<>();
        testComment.setID(1L);
        list.add(testComment);

         Assertions.assertEquals(list,testObject.findCommentsByTripID(1L));
    }

    @Test
    public void findCommentsByTripIDTestFail() {
        saveCommentTest();
        Comments comment = testObject.findCommentID(1);
        Assertions.assertNotEquals(testComment,comment); // wrong Id
    }

    @Test
    public void deleteCommentTest() {
        saveCommentTest();
        Assertions.assertTrue(this.testObject.deleteComment(1));
    }
    @Test
    public void deleteCommentFail() {
        Assertions.assertFalse(this.testObject.deleteComment(1));
    }

    @Test
    public void editComment() {
        saveCommentTest();
        Assertions.assertTrue(this.testObject.editComment(1,"comment was edited"));
        Assertions.assertEquals("comment was edited",testObject.findCommentID(1).getComment());
    }

    @Test
    public void editCommentFail() {
        Assertions.assertFalse(this.testObject.editComment(1,"comment was edited"));
    }

}
