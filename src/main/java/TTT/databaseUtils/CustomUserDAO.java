package TTT.databaseUtils;

import TTT.trips.Trip;
import TTT.users.CustomUser;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class CustomUserDAO {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public CustomUserDAO() {}
    public CustomUserDAO(SessionFactory testSessionFactory) {
        this.sessionFactory = testSessionFactory;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close(); // Zamknięcie fabryki sesji
        }
    }

    public boolean saveUser(CustomUser customUser) {

        if (findCustomUserByEmail(customUser.getEmail()) != null) {
            return false;
        }
        if (customUser.getPassword() == null || customUser.getPassword().length() < 8) {
            return false;
        }
        String username = customUser.getEmail().substring(0, customUser.getEmail().indexOf("@"));

        customUser.setCustomUserName(username);

        customUser.setDistanceTraveled(0.00);

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            session.merge(customUser);
            transaction.commit();

            System.out.println("user saved");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }

    public boolean editUsersChanges(List<CustomUser> customUsers, double number) {
        if (customUsers == null || customUsers.isEmpty()) {
            return false;
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            for (CustomUser customUser : customUsers) {

                customUser.setDistanceTraveled(customUser.getDistanceTraveled() + number);
                customUser.setNumbersOfTrips(customUser.getNumbersOfTrips() + 1);

                session.merge(customUser);
            }

            transaction.commit();
            System.out.println("Users saved successfully");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<CustomUser> listAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> criteriaQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = criteriaQuery.from(CustomUser.class);
            criteriaQuery.select(root);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public CustomUser findCustomUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> userQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = userQuery.from(CustomUser.class);
            userQuery.select(root).where(cb.equal(root.get("email"), email));
            CustomUser results = session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with email: " + email);
        }
        return null;
    }

    public CustomUser findCustomUserByID(String ID) {
        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> userQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = userQuery.from(CustomUser.class);
            userQuery.select(root).where(cb.equal(root.get("id"), ID));
            CustomUser results = session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with email: " + ID);
        }
        return null;
    }

    public List<CustomUser> findCustomUserByName(String name) {
        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> userQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = userQuery.from(CustomUser.class);
            userQuery.select(root).where(cb.equal(cb.lower(root.get("customUserName")), name.toLowerCase()));
            List<CustomUser> results = session.createQuery(userQuery).getResultList();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with email: " + name);
        }
        return null;
    }


    public boolean deleteCustomUser(String email) {

        if (email == null) {
            return false;
        }
        email = email.trim();
        if (email.isEmpty()) {
            return false;
        }

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<CustomUser> delete = cb.createCriteriaDelete(CustomUser.class);

            Root<CustomUser> authorRoot = delete.from(CustomUser.class);
            delete.where(cb.equal(authorRoot.get("email"), email));

            session.createMutationQuery(delete).executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }

        }
    }

    public Boolean updateUserTrips(String email, List<Trip> trips) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomUser user = findCustomUserByEmail(email);
            if (user != null) {
                user.setTripsParticipated(trips);
                session.merge(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found with email: " + email);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }

    }

    public Boolean updateUserField(String value, String email, String field) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomUser user = findCustomUserByEmail(email);
            if (user != null && value != null) {
                switch (field) {
                    case "email":
                        user.setEmail(value);
                        break;
                    case "username":
                        user.setCustomUserName(value);
                        break;
                    case "city":
                        user.setCity(value);
                        break;
                    default:
                        System.out.println("Wrong field!");
                        return false;
                }
                session.merge(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateUserAge(String email, int age) {
        Transaction transaction = null;
        if (age > 120) {
            return false;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomUser user = findCustomUserByEmail(email);
            if (user != null) {
                user.setAge(age);
                session.merge(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found with email: " + email);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateUserStats(double value, String email, String field) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomUser user = findCustomUserByEmail(email);
            if (user != null && value > 0) {
                int numberOfAnnouncements = user.getTripsOwned().size();
                int numberOfTrips = user.getNumbersOfTrips();
                double numberOfDistance = user.getDistanceTraveled();

                switch (field) {
                    case "numbersOfTrips":
                        user.setNumbersOfTrips((numberOfTrips + 1));
                        System.out.println("NumbersOfTrips " + user.getCustomUserName() + " " + user.getNumbersOfTrips());
                        break;
                    case "numberOfAnnouncements":
                        user.setNumbersOfAnnouncements((int) (numberOfAnnouncements + value));
                        System.out.println("owner " + user.getCustomUserName() + " " + user.getNumbersOfAnnouncements());
                        break;
                    case "distanceTraveled":
                        user.setDistanceTraveled(numberOfDistance + value);
                        System.out.println("distanceTraveled " + user.getCustomUserName() + " " + user.getDistanceTraveled());
                        break;
                }
                session.merge(user);
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}