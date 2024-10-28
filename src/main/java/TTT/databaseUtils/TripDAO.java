package TTT.databaseUtils;

import TTT.trips.Trip;
import TTT.users.CustomUser;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public boolean addAnnouncement(Trip trip) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(trip);
            transaction.commit();
            System.out.println("The trip has been created!");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }

    public Trip findTrip(String destination) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Trip> userQuery = cb.createQuery(Trip.class);
            Root<Trip> root = userQuery.from(Trip.class);
            userQuery.select(root).where(cb.equal(root.get("destination"), destination));
            Trip results = session.createQuery(userQuery).getSingleResultOrNull();
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

            Root<Trip> authorRoot = delete.from(Trip.class);
            delete.where(cb.equal(authorRoot.get("Trip"), Trip.class));

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

    public List<Trip> listAllAnnouncements() {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT a FROM Trip a", Trip.class).getResultList();
    }

    public List<Trip> ListTripWhereParticipated(long id) {
        Session session = sessionFactory.openSession();
        List<Trip> trips = session.createQuery("SELECT a FROM Trip a", Trip.class).getResultList();

        List<Trip> results = new ArrayList<Trip>();
        for (Trip trip : trips) {
            for (long userId : trip.getParticipantsId())
                if (id == userId) {
                    results.add(trip);
                }
        }

        return results;
    }

    public List<Trip> listAllAnnouncementsByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT a FROM Trip a WHERE a.owner.id = :userId", Trip.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    public List<Trip> listAllAnnouncementsByDestination(String destination) {
        Session session = sessionFactory.openSession();
        //search ignoring Upper/lowercase [ILIKE]!
        return session.createQuery("SELECT a FROM Trip a WHERE a.destination ILIKE:destination ", Trip.class)
                .setParameter("destination", destination)
                .getResultList();
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

    public void updateTripParticipants(long idTrip, Trip trip) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (trip != null) {
                session.merge(trip);
                transaction.commit();
            } else {
                System.out.println("Trip id did not exist");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


}
