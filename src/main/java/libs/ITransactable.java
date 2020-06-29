package libs;

import org.hibernate.Session;

public interface ITransactable {

    void commit(Session session);
}
