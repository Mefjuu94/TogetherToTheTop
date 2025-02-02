package TTT.databaseUtils;

import TTT.trips.Trip;
import TTT.users.UserRating;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserRatingDAO {

    private SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public UserRatingDAO() {
    }

    public UserRatingDAO(SessionFactory testSessionFactory) {
        this.sessionFactory = testSessionFactory;
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close(); // Zamknięcie fabryki sesji
        }
    }

    public boolean addRate(UserRating rate) {
        Transaction transaction = null;
        if (rate == null || rate.getTrip() == null || rate.getId() == null){
            throw new IllegalStateException("rate, rate.Id or rate.Trip cannot be null!");
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Transaction started: " + transaction.isActive());

            session.merge(rate); // Synchronizacja obiektu z bazą danych
            transaction.commit(); // Zatwierdzenie transakcji
            System.out.println("Transaction committed.");
            return true; // Operacja zakończona sukcesem
        } catch (Exception e) {
            System.err.println("Error while adding rate: " + e.getMessage());
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback(); // Wycofanie transakcji w przypadku błędu
                System.out.println("Transaction rolled back.");
            }
            e.printStackTrace(); // Logowanie błędu
            return false; // Operacja zakończona niepowodzeniem
        }
    }


    public UserRating getRate(Long id) {
        Transaction transaction = null;
        UserRating rating = null; // Zmienna do przechowywania wyniku

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            rating = session.get(UserRating.class, id);

            if (rating != null) {
                System.out.println("The rate has been retrieved: ");
            } else {
                System.out.println("No rate found with ID: " + id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return rating;
    }

    public boolean editRate(Long id, int rate, Trip trip, String behavior) {
        UserRating rating = getRate(id);
        if (rating == null || id == null) {
            return false;
        }

        // Sprawdzenie, czy trip jest null
        if (trip == null) {
            throw new IllegalStateException("Trip cannot be null.");
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            rating.setId(id);
            rating.setRating(rate);
            rating.setTrip(trip);
            rating.setComment(behavior);
            rating.setFilled(true);

            session.merge(rating);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }


    public List<UserRating> listAllRatings() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserRating> criteriaQuery = criteriaBuilder.createQuery(UserRating.class);

            Root<UserRating> root = criteriaQuery.from(UserRating.class);
            criteriaQuery.select(root);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    public List<UserRating> listAllRates(long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.clear();
            String sql = "SELECT * FROM userrating WHERE user_id = :userId AND isFilled = true";
            return session.createNativeQuery(sql, UserRating.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }

    public List<UserRating> listUsersToRateByMe(long userId) {
        try (Session session = sessionFactory.openSession()) {
            session.clear();
            String sql = "SELECT * FROM userrating WHERE reviewer_id = :userId";
            return session.createNativeQuery(sql, UserRating.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}