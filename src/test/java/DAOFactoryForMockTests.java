import TTT.databaseUtils.CommentsDAO;
import TTT.databaseUtils.CustomUserDAO;
import TTT.databaseUtils.TripDAO;
import TTT.databaseUtils.UserRatingDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DAOFactoryForMockTests {
    private static CustomUserDAO customUserDAO;
    private static UserRatingDAO userRatingDAO;
    private static TripDAO tripDAO;
    private static CommentsDAO commentsDAO;

    private static PostgreSQLContainer<?> postgresqlContainer;
    private static SessionFactory sessionFactory;

    static {
        postgresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("test_container")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
        postgresqlContainer.start();


        int mappedPort = postgresqlContainer.getMappedPort(5432);
        sessionFactory = TestSessionFactoryCreator.getCustomUserSessionFactory(mappedPort);

        customUserDAO = new CustomUserDAO(sessionFactory);
        userRatingDAO = new UserRatingDAO(sessionFactory);
        tripDAO = new TripDAO(sessionFactory);
        commentsDAO = new CommentsDAO(sessionFactory);
    }

    public static void clearDatabase() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM Comments").executeUpdate();
            session.createQuery("DELETE FROM UserRating").executeUpdate();
            session.createQuery("DELETE FROM Trip").executeUpdate();
            session.createQuery("DELETE FROM CustomUser").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public static CustomUserDAO getCustomUserDAO() {
        return customUserDAO;
    }

    public static UserRatingDAO getUserRatingDAO() {
        return userRatingDAO;
    }

    public static TripDAO getTripDAO() {
        return tripDAO;
    }

    public static CommentsDAO getCommentsDAO() {
        return commentsDAO;
    }
}
