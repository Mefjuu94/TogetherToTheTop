//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestSessionFactoryCreator {
    public TestSessionFactoryCreator() {
    }

    public static SessionFactory getCustomUserSessionFactory(int port) {
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:" + port + "/test_container?loggerLevel=OFF");
        return config.buildSessionFactory();
    }
}
