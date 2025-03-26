package TTT.databaseUtils;

import TTT.trips.Trip;
import TTT.users.UserRating;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserRatingDAO {

    private SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public UserRatingDAO() {
    }

    public UserRatingDAO(SessionFactory testSessionFactory) {
        this.sessionFactory = testSessionFactory;
    }

    public boolean addRate(UserRating rate) {
        Transaction transaction = null;
        if (rate == null || rate.getTrip() == null){
            System.out.println(("rate or rate.Trip cannot be null!"));
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.merge(rate);
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error while adding rate: " + e.getMessage());
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
                System.out.println("Transaction rolled back.");
            }
            e.printStackTrace();
            return false;
        }
    }


    public UserRating getRate(Long id) {
        Transaction transaction = null;
        UserRating rating = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            rating = session.get(UserRating.class, id);

            if (rating != null) {
                System.out.println("The rate has been retrieved: " + rating.getRating() + " to user: " + rating.getUser());
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

        if (trip == null) {
            System.out.println(("Trip cannot be null."));
            return false;
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
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserRating> cr = cb.createQuery(UserRating.class);
            Root<UserRating> root = cr.from(UserRating.class);

            Predicate userIdPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate isFilledPredicate = cb.isTrue(root.get("isFilled"));

            cr.select(root).where(cb.and(userIdPredicate, isFilledPredicate));

            return session.createQuery(cr).getResultList();
        }
    }

    public List<UserRating> listUsersToRateByMe(long userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserRating> cr = cb.createQuery(UserRating.class);
            Root<UserRating> root = cr.from(UserRating.class);

            Predicate reviewerIdPredicate = cb.equal(root.get("reviewer").get("id"), userId);

            cr.select(root).where(reviewerIdPredicate);

            return session.createQuery(cr).getResultList();
        }
    }
}