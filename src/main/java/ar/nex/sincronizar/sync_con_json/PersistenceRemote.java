package ar.nex.sincronizar.sync_con_json;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class PersistenceRemote {

    private EntityManagerFactory managerFactory;
    private EntityManager em;
    private Map<String, String> persistenceMap = new HashMap<>();

    /**
     * MYSQL_ADDON_HOST= bxtvdeoxbvp3jskyzekv-mysql.services.clever-cloud.com
     * MYSQL_ADDON_DB= bxtvdeoxbvp3jskyzekv 
     * MYSQL_ADDON_USER= uxqrbexixwavcqbz
     * MYSQL_ADDON_PORT= 3306 
     * MYSQL_ADDON_PASSWORD= IOmd4SIEJKdV6cl7I8Tg
     * MYSQL_ADDON_URI= mysql://uxqrbexixwavcqbz:IOmd4SIEJKdV6cl7I8Tg@bxtvdeoxbvp3jskyzekv-mysql.services.clever-cloud.com:3306/bxtvdeoxbvp3jskyzekv
     */
    
    public PersistenceRemote() {

        this.managerFactory = null;
        this.em = null;

        persistenceMap.put("javax.persistence.jdbc.url", "jdbc:mysql://uxqrbexixwavcqbz:IOmd4SIEJKdV6cl7I8Tg@bxtvdeoxbvp3jskyzekv-mysql.services.clever-cloud.com:3306/bxtvdeoxbvp3jskyzekv");
        persistenceMap.put("javax.persistence.jdbc.user", "uxqrbexixwavcqbz");
        persistenceMap.put("javax.persistence.jdbc.password", "IOmd4SIEJKdV6cl7I8Tg");
        persistenceMap.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");

        //manager = managerFactory.createEntityManager();
    }

    private EntityManagerFactory getEMF() {
        return this.managerFactory = Persistence.createEntityManagerFactory("SaeRemote");
    }

    public EntityManager getEM() {
        return this.em = getEMF().createEntityManager();
    }
}
