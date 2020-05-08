package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.jpa.service.JpaRemote;
import ar.nex.jpa.service.JpaService;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.apache.commons.math3.analysis.function.Sin;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class SincronizarController {

    private JpaService jpaLocal;
    private JpaRemote jpaRemote;

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8000;
    public static final String BASE_URL = "http://" + HOSTNAME + ":" + PORT;

    public List<Actividad> getLocalPendientes() {
        try {
            EntityManager em = new JpaService().getFactory().createEntityManager();
            TypedQuery<Actividad> query
                    = em.createQuery("SELECT a FROM Actividad a"
                            + "  WHERE a.sincronizacion = :estado", Actividad.class)
                            .setParameter("estado", SincronizarEstado.PENDIENTE);
            List<Actividad> results = query.getResultList();
            if (!results.isEmpty()) {
                return results;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<Actividad> getRemotePendientes() {
        try {
            EntityManager em = new JpaService().getFactory().createEntityManager();

            Dispositivo dp = new JpaService().getDispositivo().findDispositivo("0");
            //System.out.println("dp::: " + dp.getNombre());
            Query query = em.createQuery(""
                    + "SELECT a "
                    + "FROM Actividad a "
                    + "WHERE :rdp NOT MEMBER OF a.dispositivoList")
                    .setParameter("rdp", dp);
            
            List<Actividad> results = new ArrayList<>();//query.getResultList();
            if (!results.isEmpty()) {
                return results;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Actividad> test() {

        List<Actividad> actividadList = null;
        List<Sincronizar> syncList = null;
        try {
            EntityManager em = new JpaService().getFactory().createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Sincronizar> cq = cb.createQuery(Sincronizar.class);

            if (cq != null) {
                Root<Sincronizar> player = cq.from(Sincronizar.class);
                Join<Sincronizar, Actividad> join = player.join("sincronizarList", JoinType.LEFT);
                cq.where(cb.equal(join.get("device"), "Server"));

                //cq.where(cb.equal(join.get("name"), "ordinary"));
                //Join<User, Role> join = from.join("role", JoinType.LEFT);
                // Get MetaModel from Root
                //EntityType<Sincronizar> Sincronizar_ = player.getModel();
                // set the where clause
                TypedQuery<Sincronizar> tq = em.createQuery(cq);

                syncList = tq.getResultList();
            }
//                try {
//        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
//        if (cq != null) {
//            Root<Player> player = cq.from(Player.class);
//            Join<Player, Team> team = player.join(Player_.teams);
//            Join<Team, League> league = team.join(Team_.league);
//
//            // Get MetaModel from Root
//            //EntityType<Player> Player_ = player.getModel();
//
//            // set the where clause
//            cq.where(cb.equal(league.get(League_.sport), sport));
//            cq.select(player).distinct(true);
//            TypedQuery<Player> q = em.createQuery(cq);
//            players = q.getResultList();
//        }
            return actividadList;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void checkLocalActividad() {
        try {
            List<Actividad> pendientes = null;
            pendientes = getLocalPendientes();
            if (pendientes != null) {
                System.out.println("SincronizarController.checkLocalActividad()::: HAY " + pendientes.size() + " para sync");
                if (hayConexion()) {
                    for (Actividad actividad : pendientes) {
                        remoteCreate(actividad);
                    }
                }
            } else {
                System.out.println("ar.nex.sincronizar.SincronizarController.checkLocalActividad()::: NADA para sync");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //new ActividadController().remoteList();
        //new ActividadController().genActividad();
        //new SincronizarController().checkLocalActividad();

//        if (new SincronizarController().hayConexion()) {
//            System.out.println("SI SI SI::::");
//            new SincronizarController().checkLocalActividad();
//        } else {
//            System.out.println("NO NO NO::::");
//        }
        // List<Actividad> lst = new SincronizarController().getRemotePendientes();
        List<Actividad> lst = new SincronizarController().getRemotePendientes();
        if (lst != null) {
            System.out.println("Cantidad::: " + lst.size());
            for (Actividad a : lst) {
                System.out.println("Actividad::: " + a.getUuid() + " - " + a.getDevice());
            }
        }

    }

    private void remoteCreate(Actividad actividad) {
        try {
            jpaLocal = new JpaService();
            jpaRemote = new JpaRemote();

            switch (actividad.getEntity()) {
                case "Item": {
                    syncItem(actividad.getEntityUuid(), false);
                    syncActividad(actividad, false);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return true si se conecta con la base remota
     */
    private boolean hayConexion() {
        Connection conn = null;
        boolean estado = false;
        try {
            String url = "jdbc:mysql://bxtvdeoxbvp3jskyzekv-mysql.services.clever-cloud.com:3306/bxtvdeoxbvp3jskyzekv?useSSL=false";
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, "uxqrbexixwavcqbz", "IOmd4SIEJKdV6cl7I8Tg");
            System.out.println("Database connection established");
            estado = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) {
                    /* ignore close errors */ }
            }
        }
        return estado;
    }

    private void syncActividad(Actividad actividad, boolean local) {
        try {
            if (local) {
                new JpaService().getActividad().create(actividad);
            } else {
                Sincronizar sync = new Sincronizar();
                new JpaService().getSincronizar().create(sync);
                new JpaRemote().getSincronizar().create(sync);
                actividad.getSincronizarList().add(sync);
                new JpaRemote().getActividad().create(actividad);
                actividad.setSincronizacion(SincronizarEstado.SINCRONIZADO);
                jpaLocal.getActividad().edit(actividad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncItem(String uuid, boolean local) {
        try {
            System.out.println("Item UUID:::" + uuid);
            Item i = new JpaService().getItem().findItem(uuid);
            new JpaRemote().getItem().create(i);
            System.out.println("jpa.getItem().getItemCount()::: " + new JpaRemote().getItem().getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
