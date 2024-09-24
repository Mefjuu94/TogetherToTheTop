//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TTT.databaseUtils;

import TTT.users.CustomUser;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomUserDAO {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SessionFactory sessionFactory = UserSessionFactory.getUserSessionFactory();

    public CustomUserDAO() {
    }

    public boolean saveUser(CustomUser customUser) {
        if (this.findCustomUser(customUser.getEmail()) != null) {
            return false;
        } else if (customUser.getPassword().length() < 7) {
            return false;
        } else {
            Session session = null;
            Transaction transaction = null;

            boolean var5;
            try {
                session = this.sessionFactory.openSession();
                transaction = session.beginTransaction();
                customUser.setPassword(this.passwordEncoder.encode(customUser.getPassword()));
                session.merge(customUser);
                transaction.commit();
                boolean var11 = true;
                return var11;
            } catch (Exception var9) {
                Exception e = var9;
                if (transaction != null) {
                    transaction.rollback();
                }

                e.printStackTrace();
                var5 = false;
            } finally {
                session.close();
            }

            return var5;
        }
    }

    public CustomUser findCustomUser(String email) {
        try {
            Session session = this.sessionFactory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CustomUser> userQuery = cb.createQuery(CustomUser.class);
            Root<CustomUser> root = userQuery.from(CustomUser.class);
            userQuery.select(root).where(cb.equal(root.get("email"), email));
            CustomUser results = (CustomUser)session.createQuery(userQuery).getSingleResultOrNull();
            return results;
        } catch (IllegalArgumentException | PersistenceException var7) {
            System.out.println("No entity found with email: " + email);
            return null;
        }
    }

    public boolean deleteCustomUser(String email) {
        if (email == null) {
            return false;
        } else {
            email = email.trim();
            if (email.isEmpty()) {
                return false;
            } else {
                Session session = null;

                boolean var4;
                try {
                    session = this.sessionFactory.openSession();
                    session.beginTransaction();
                    CriteriaBuilder cb = session.getCriteriaBuilder();
                    CriteriaDelete<CustomUser> delete = cb.createCriteriaDelete(CustomUser.class);
                    Root<CustomUser> authorRoot = delete.from(CustomUser.class);
                    delete.where(cb.equal(authorRoot.get("email"), email));
                    session.createMutationQuery(delete).executeUpdate();
                    session.getTransaction().commit();
                    boolean var6 = true;
                    return var6;
                } catch (Exception var10) {
                    Exception e = var10;
                    if (session != null && session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }

                    e.printStackTrace();
                    var4 = false;
                } finally {
                    if (session != null) {
                        session.close();
                    }

                }

                return var4;
            }
        }
    }
}
