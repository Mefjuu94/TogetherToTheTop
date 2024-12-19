package TTT.databaseUtils;

import TTT.trips.Trip;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripDAO {

    private SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public TripDAO(SessionFactory testSessionFactory) {
        this.sessionFactory = testSessionFactory;
    }

    public TripDAO() {
    }

    public boolean addAnnouncement(Trip trip) {

        if (trip == null) {
            return false;
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(trip);
            transaction.commit();
            System.out.println("The trip has been created!");
            session.close();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }

    public List<Trip> findTrip(String destination) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trip> cq = cb.createQuery(Trip.class);
            Root<Trip> root = cq.from(Trip.class);
            cq.select(root);

            List<Trip> preResults = session.createQuery(cq).getResultList();
            List<Trip> results = new ArrayList<>();
            for (Trip preResult : preResults) {
                String[] tag = preResult.getDestination().split("[ ,]+");
                for (String s : tag) {
                    System.out.println(s);
                    if (Objects.equals(s.toLowerCase(), destination.toLowerCase())) {
                        results.add(preResult);
                        break;
                    }
                }
            }
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with destination: " + destination);
        }
        return null;
    }


    public boolean deleteTrip(Trip trip) {

        if (trip == null) {
            return false;
        }

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Trip> delete = cb.createCriteriaDelete(Trip.class);

            Root<Trip> root = delete.from(Trip.class);
            delete.where(cb.equal(root.get("id"), trip.getId()));

            session.createQuery(delete).executeUpdate();
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

    public List<Trip> listAllAnnouncements() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trip> criteriaQuery = cb.createQuery(Trip.class);
            Root<Trip> root = criteriaQuery.from(Trip.class);
            criteriaQuery.select(root);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<Trip> listAllAnnouncementsByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trip> criteriaQuery = cb.createQuery(Trip.class);
            Root<Trip> root = criteriaQuery.from(Trip.class);
            criteriaQuery.select(root).where(cb.equal(root.get("owner").get("id"), userId));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }



    public Trip findTripID(long id) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trip> userQuery = cb.createQuery(Trip.class);
            Root<Trip> root = userQuery.from(Trip.class);
            userQuery.select(root).where(cb.equal(root.get("id"), id));
            Trip results = session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with destination: " + id);
        }
        return null;
    }

    public List<Object[]> listAllTripParticipantIds() {
        Session session = sessionFactory.openSession();
        return session.createNativeQuery("SELECT trip_id, user_id FROM trip_participants", Object[].class)
                .getResultList();

    }

    public Boolean updateTrip(Trip trip) {
        Transaction transaction = null;

        if (trip == null) {
            return false;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(trip);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }


}
