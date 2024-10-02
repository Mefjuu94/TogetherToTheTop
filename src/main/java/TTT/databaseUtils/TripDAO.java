package TTT.databaseUtils;

import TTT.trips.Trip;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TripDAO {

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public boolean addAnnouncement(Trip trip) {

        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(trip);
            transaction.commit();
            System.out.println("The trip has been created!");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        } finally {
            session.close(); // close session
        }
    }

//    public Trip findTrip(String email) {
//        try {
//            Session session = sessionFactory.openSession();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<Trip> userQuery = cb.createQuery(Trip.class);
//            Root<Trip> root = userQuery.from(Trip.class);
//            userQuery.select(root).where(cb.equal(root.get("email"), email));
//            Trip results = session.createQuery(userQuery).getSingleResultOrNull();
//            return results;
//        } catch (PersistenceException | IllegalArgumentException e) {
//            System.out.println("No entity found with email: " + email);
//        }
//        return null;
//    }

//    public boolean deleteTrip(String email) {
//
//        if (email == null) {
//            return false;
//        }
//        email = email.trim();
//        if (email.isEmpty()) {
//            return false;
//        }
//
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            session.beginTransaction();
//
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaDelete<CustomUser> delete = cb.createCriteriaDelete(CustomUser.class);
//
//            Root<CustomUser> authorRoot = delete.from(CustomUser.class);
//            delete.where(cb.equal(authorRoot.get("email"), email));
//
//            session.createMutationQuery(delete).executeUpdate();
//            session.getTransaction().commit();
//            return true;
//        } catch (Exception e) {
//            if (session != null && session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }
//
}
