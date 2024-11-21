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

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

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


    public boolean deleteRate(Long id, UserRating rating) {

        if (rating == null || id == null) {
            return false;
        }

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<UserRating> delete = cb.createCriteriaDelete(UserRating.class);

            Root<UserRating> authorRoot = delete.from(UserRating.class);
            delete.where(cb.equal(authorRoot.get("UserRating"), UserRating.class));

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

    public List<UserRating> listAllARatings() {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT a FROM UserRating a", UserRating.class).getResultList();
    }
}
