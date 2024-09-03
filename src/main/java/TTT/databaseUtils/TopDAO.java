package TTT.databaseUtils;

import TTT.peaksAndSummitsHandler.Top;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TopDAO {

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();


    public boolean addTop(Top top) {
        if (top.getHeight() > 8850 || top.getHeight() < 1) {
            System.out.println("Invalid height!");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(top);
        transaction.commit();
        session.close();
        return true;
    }

    public List<Top> getAllSummits() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> cq = cb.createQuery(Top.class);
        Root<Top> topRoot = cq.from(Top.class);
        cq.select(topRoot);
        return session.createQuery(cq).getResultList();
    }

    public List<Top> findSummitByName(String summit) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
        Root<Top> root = userQuery.from(Top.class);
        // getting all letters to small in name column, also with argument (cb.lower() , to lowerCase()
        userQuery.select(root).where(cb.like(cb.lower(root.get("name")), "%" + summit.toLowerCase() + "%"));
        List<Top> results = session.createQuery(userQuery).getResultList();
        if (results.isEmpty()) {
            System.out.println("there is no Summit with that name");
        }
        return results;
    }

    public List<Top> findSummitByHeight(int height) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
        Root<Top> root = userQuery.from(Top.class);
        userQuery.select(root).where(cb.equal(root.get("height"), height));
        List<Top> results = session.createQuery(userQuery).getResultList();
        if (results.isEmpty()) {
            System.out.println("there is no Summit with that name");
        }
        return results;
    }

    public List<Top> findSummitsByHeightGreaterThan(int height) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
        Root<Top> root = userQuery.from(Top.class);
        userQuery.select(root).where(cb.greaterThan(root.get("height"), height));
        List<Top> results = session.createQuery(userQuery).getResultList();
        if (results.isEmpty()) {
            System.out.println("there is no Summit with that height");
        }
        return results;
    }

    public List<Top> findSummitsByHeightLessThan(int height) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
        Root<Top> root = userQuery.from(Top.class);
        userQuery.select(root).where(cb.lessThan(root.get("height"), height));
        List<Top> results = session.createQuery(userQuery).getResultList();
        if (results.isEmpty()) {
            System.out.println("there is no Summit with that height");
        }
        return results;
    }

    public List<Top> findSummitsByHeightBetween(int height1, int height2) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
        Root<Top> root = userQuery.from(Top.class);
        userQuery.select(root).where(cb.between(root.get("height"), height1, height2));
        List<Top> results = session.createQuery(userQuery).getResultList();
        if (results.isEmpty()) {
            System.out.println("there is no Summit with that name");
        }
        return results;
    }

    public boolean deleteTop(String summitName) {
        if (!findSummitByName(summitName).isEmpty()) {

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Top> delete = cb.createCriteriaDelete(Top.class);
            Root<Top> authorRoot = delete.from(Top.class);

            delete.where(cb.equal(authorRoot.get("name"), summitName));

            session.createMutationQuery(delete).executeUpdate();
            session.getTransaction().commit();
            return true;
        } else {
            System.out.println("No summit with that name");
            return false;
        }
    }

}
