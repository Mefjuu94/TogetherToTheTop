package TTT.PeakAndSummitsHandler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TopDAO {

    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();


    public boolean addTop(Top top) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.merge(top);
            transaction.commit();
            session.close();
            return true;
        } catch (EntityExistsException e) {
            System.out.println("EntityExistsException: entity alerady exist.");
            return false;
        }
    }

    public List<Top> getAllSummits() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Top> cq = cb.createQuery(Top.class);
        Root<Top> topRoot = cq.from(Top.class);
        cq.select(topRoot);
        return session.createQuery(cq).getResultList();
    }

    public List <Top> findSummitByName(String summit) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
            Root<Top> root = userQuery.from(Top.class);
            userQuery.select(root).where(cb.equal(root.get("name"), summit));
            List <Top> results = session.createQuery(userQuery).getResultList();
            return results;
        } catch (NoResultException e) {
            System.out.println("there is no Summit with that name");
            return null;
        }
    }

    public List <Top> findSummitByHeight(double height) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Top> userQuery = cb.createQuery(Top.class);
            Root<Top> root = userQuery.from(Top.class);
            userQuery.select(root).where(cb.equal(root.get("height"), height));
            List <Top> results = session.createQuery(userQuery).getResultList();
            return results;
        } catch (NoResultException e) {
            System.out.println("there is no Summit with that height");
            return null;
        }
    }

}
