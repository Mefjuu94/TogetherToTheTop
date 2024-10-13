//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import TTT.databaseUtils.CustomUserDAO;
import TTT.users.CustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class CustomUserDAOTests {
    private CustomUserDAO testObject;
    private CustomUser testCustomUser = new CustomUser("test@mail.com", "12345678");
    DockerImageName postgres = DockerImageName.parse("postgres:16");
    @Container
    PostgreSQLContainer postgresqlContainer;

    public CustomUserDAOTests() {
        this.postgresqlContainer = (PostgreSQLContainer)(new PostgreSQLContainer(this.postgres)).withDatabaseName("test_container").withUsername("test").withPassword("test").withReuse(true);
    }

    @BeforeEach
    public void emptyTest() {
        int mappedPort = this.postgresqlContainer.getMappedPort(5432);
        this.testObject = new CustomUserDAO(TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort));
        System.out.println(mappedPort);
    }

    @Test
    public void saveCustomUserTest() {
        Assertions.assertTrue(this.testObject.saveUser(this.testCustomUser));
        this.testObject.deleteCustomUser(this.testCustomUser.getEmail());
    }

    @Test
    public void saveCustomUserTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertFalse(this.testObject.saveUser(this.testCustomUser));
    }

    @Test
    public void findCustomUserByEmailTest() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertEquals(this.testCustomUser.getEmail(), this.testObject.findCustomUserByEmail("test@mail.com").getEmail());
    }

    @Test
    public void findCustomUserByEmailTestFail() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertNull(this.testObject.findCustomUserByEmail("testCustomUser@mail.com"));
        this.testObject.deleteCustomUser("test@mail.com");
    }

    @Test
    public void deleteCustomUserTest() {
        this.testObject.saveUser(this.testCustomUser);
        Assertions.assertTrue(this.testObject.deleteCustomUser("test@mail.com"));
    }

    @Test
    public void deleteCustomUserTestFail() {
        Assertions.assertFalse(this.testObject.deleteCustomUser((String)null));
        Assertions.assertFalse(this.testObject.deleteCustomUser("  "));
    }
}
