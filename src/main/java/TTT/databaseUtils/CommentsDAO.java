package TTT.databaseUtils;

import TTT.trips.Comments;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CommentsDAO {

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public boolean addComment(Comments comment) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.merge(comment);
            transaction.commit();
            System.out.println("Comment added!");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // back transaction when is error
            e.printStackTrace();
            return false;
        }
    }

    public List<Comments> findByTripID(Long ID) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Comments> userQuery = cb.createQuery(Comments.class);
            Root<Comments> root = userQuery.from(Comments.class);
            userQuery.select(root).where(cb.equal(root.get("tripID"), ID));
            List<Comments> results = session.createQuery(userQuery).getResultList();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with id: " + ID);
        }
        return null;
    }

    public boolean deleteComment(long id) {

        Session session = null;
        Comments comment = findCommentID(id);

        if (comment == null){
            System.out.println("no comment with this ID!");
            return false;
        }

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Comments> delete = cb.createCriteriaDelete(Comments.class);

            Root<Comments> authorRoot = delete.from(Comments.class);
            delete.where(cb.equal(authorRoot.get("id"), id));

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

    public List<Comments> listAllComments() {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT a FROM Comments a", Comments.class).getResultList();
    }

    public Comments findCommentID(long id) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Comments> userQuery = cb.createQuery(Comments.class);
            Root<Comments> root = userQuery.from(Comments.class);
            userQuery.select(root).where(cb.equal(root.get("id"), id));
            Comments results = session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (PersistenceException | IllegalArgumentException e) {
            System.out.println("No entity found with destination: " + id);
        }
        return null;
    }

    public void editComment(long idOfComment, String commentString) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
                session.merge(commentString);
                transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
