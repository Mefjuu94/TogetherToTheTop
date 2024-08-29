package TTT.databaseUtils;

import TTT.users.CustomUser;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomUserDAO {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public boolean saveUser(CustomUser customUser){
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));
            session.merge(customUser);
            transaction.commit();
            session.close();
            return true;
        } catch (EntityExistsException e) {
            System.out.println("EntityExistsException: entity alerady exist.");
            return false;
        }
    }


    public CustomUser findCustomUser(String email) {
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> userQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = userQuery.from(CustomUser.class);
            userQuery.select(root).where(cb.equal(root.get("email"), email));
            CustomUser results = session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (NoResultException e) {
            System.out.println("there is no Customer User with that email");
            return null;
        }
    }

}
