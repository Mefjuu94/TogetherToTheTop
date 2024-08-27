package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UserSessionFactory {
    public static SessionFactory getUserSessionFactory(){
        Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure("/hibernate.cfg.xml");
        return configuration.buildSessionFactory();
    }
}
