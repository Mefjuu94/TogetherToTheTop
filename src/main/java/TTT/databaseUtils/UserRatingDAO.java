package TTT.databaseUtils;

import TTT.users.UserRating;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserRatingDAO {

    private SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public UserRatingDAO(){}
    public UserRatingDAO(SessionFactory testSessionFactory){this.sessionFactory = testSessionFactory;}


    public boolean addRate(UserRating rate) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(rate);
            transaction.commit();
            System.out.println("The rate has been created!");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }


    public boolean editRate(Long id, UserRating rating) {

        if (rating == null || id == null) {
            return false;
        }

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(rating);
            transaction.commit();
            System.out.println("The rate has been edited!");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }

    public List<UserRating> listAllARatings() {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT a FROM UserRating a", UserRating.class).getResultList();
    }
}
