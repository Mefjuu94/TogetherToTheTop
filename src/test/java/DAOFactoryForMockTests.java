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
    private static final CustomUserDAO customUserDAO;
    private static final UserRatingDAO userRatingDAO;
    private static final TripDAO tripDAO;
    private static final CommentsDAO commentsDAO;

    private static final PostgreSQLContainer<?> postgresqlContainer;
    private static final SessionFactory sessionFactory;

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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
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
