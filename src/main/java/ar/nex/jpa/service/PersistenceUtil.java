package ar.nex.jpa.service;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class PersistenceUtil {

    EntityManagerFactory managerFactory = null;
    Map<String, String> persistenceMap = new HashMap<>();

    public PersistenceUtil() {

        persistenceMap.put("javax.persistence.jdbc.url", "<url>");
        persistenceMap.put("javax.persistence.jdbc.user", "<username>");
        persistenceMap.put("javax.persistence.jdbc.password", "<password>");
        persistenceMap.put("javax.persistence.jdbc.driver", "<driver>");

        managerFactory = Persistence.createEntityManagerFactory("SaeFxPU", persistenceMap);
       // manager = managerFactory.createEntityManager();
    }

}
