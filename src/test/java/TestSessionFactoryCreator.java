
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestSessionFactoryCreator {
    public TestSessionFactoryCreator() {
    }

    public static SessionFactory getCustomUserSessionFactory(int port) {
        // Tworzymy konfigurację Hibernate
        Configuration config = new Configuration();
        // Ładujemy podstawową konfigurację z pliku hibernate.cfg.xml
        config.configure("hibernate.cfg.xml");
        // Ustawiamy dynamicznie URL bazy danych z portem uzyskanym z Testcontainers
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:" + port + "/test_container?loggerLevel=OFF");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.username", "test");
        config.setProperty("hibernate.connection.password", "test");
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");

        // Tworzymy i zwracamy SessionFactory
        return config.buildSessionFactory();
    }
}
