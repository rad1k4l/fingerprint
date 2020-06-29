package libs;

import boot.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
public class Transact {


    public static void begin(ITransactable transactable) {
        Transaction transaction = null;
        try (Session session = Hibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            transactable.commit(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
