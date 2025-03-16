import TTT.databaseUtils.CommentsDAO;
import TTT.trips.Comments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Nested
@Testcontainers
class CommentsDAOTests {

    private CommentsDAO testObject;
    private final Comments testComment = EntityFactoryForTests.createTestComment();

    @BeforeEach
    public void setUp() {
        DAOFactoryForMockTests.clearDatabase();
        this.testObject = DAOFactoryForMockTests.getCommentsDAO();
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
        Comments comments = EntityFactoryForTests.createTestComment();
        testObject.addComment(comments);


        List<Comments> list = new ArrayList<>();
        long id = testObject.listAllComments().get(0).getID();
        comments.setID(id);

        list.add(comments);

        Assertions.assertEquals(list, testObject.findCommentsByTripID(1L));
    }

    @Test
    public void findCommentIDTest() {
        Comments comments = EntityFactoryForTests.createTestComment();

        testObject.addComment(comments);
        //because of automatically generated id:
        long id = testObject.listAllComments().get(0).getID();
        comments.setID(id);

        List<Comments> result = new ArrayList<>();
        result.add(comments);

        Assertions.assertEquals(result.toString(), testObject.findCommentsByTripID(1L).toString());
    }

    @Test
    public void findCommentsByTripIDTestFail() {
        saveCommentTest();
        Comments comment = testObject.findCommentID(1);
        Assertions.assertNotEquals(testComment, comment); // wrong Id
    }

    @Test
    public void deleteCommentTest() {
        saveCommentTest();
        long id = testObject.listAllComments().get(0).getID();
        Assertions.assertTrue(this.testObject.deleteComment(id));
    }

    @Test
    public void deleteCommentFail() {
        Assertions.assertFalse(this.testObject.deleteComment(1));
    }

    @Test
    public void editComment() {
        testObject.addComment(testComment);

        long id = testObject.listAllComments().get(0).getID();

        Assertions.assertTrue(testObject.editComment(id,"comment was edited"));
        Assertions.assertEquals("comment was edited", testObject.findCommentID(id).getComment());
    }

    @Test
    public void editCommentFail() {
        Assertions.assertFalse(this.testObject.editComment(1, "comment was edited"));
    }

    @Test
    public void listAllCommentsTest() {
        List<Comments> list = new ArrayList<>();

        testObject.addComment(testComment);
        Comments comment = new Comments("second comment", 2, 3, "Stefan");
        testObject.addComment(comment);

        testComment.setID(testObject.listAllComments().get(0).getID());
        comment.setID(testObject.listAllComments().get(1).getID());

        list.add(testComment);
        list.add(comment);

        Assertions.assertEquals(list, testObject.listAllComments());
    }

    @Test
    public void listAllCommentsTestFail() {
        List<Comments> list = new ArrayList<>();

        testObject.addComment(testComment);
        Comments comment = new Comments("second comment", 2, 3, "Stefan");
        testObject.addComment(comment);

        Assertions.assertNotEquals(list, testObject.listAllComments());
    }
}
