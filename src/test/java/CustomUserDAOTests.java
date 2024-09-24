import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomUserDAOTests {

    CustomUserDAO testObject = new CustomUserDAO();
    CustomUser testCustomUser = new CustomUser("test@mail.com","123");

    @Test
    public void saveCustomUserTest(){
        Assertions.assertTrue(testObject.saveUser(testCustomUser));
        testObject.deleteCustomUser("test@mail.com");
    }

    @Test
    public void saveCustomUserTestFail(){
        testObject.saveUser(testCustomUser);
        Assertions.assertFalse(testObject.saveUser(testCustomUser));
    }

    @Test
    public void findCustomUserTest(){
        testObject.deleteCustomUser("test@mail.com"); // to avoid entityException
        testObject.saveUser(testCustomUser);
        Assertions.assertEquals(testCustomUser.getEmail(),testObject.findCustomUser("test@mail.com").getEmail());
    }

    @Test
    public void findCustomUserTestFail(){
        testObject.saveUser(testCustomUser);
        Assertions.assertNull(testObject.findCustomUser("testCustomUser@mail.com"));
        testObject.deleteCustomUser("test@mail.com");
    }

    @Test
    public void deleteCustomUserTest(){
        testObject.deleteCustomUser("test@mail.com"); // to avoid entityException
        testObject.saveUser(testCustomUser);
        Assertions.assertTrue(testObject.deleteCustomUser("test@mail.com"));
    }

    @Test
    public void deleteCustomUserTestFail(){
        Assertions.assertFalse(testObject.deleteCustomUser("noCustomert@mail.com"));
    }
}
