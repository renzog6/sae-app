package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.jpa.service.JpaRemote;
import ar.nex.jpa.service.JpaService;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

    public List<Actividad> getPendientes() {
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

    public List<Actividad> getRemoteActividad() {
        try {
            EntityManager em = new JpaRemote().getFactory().createEntityManager();
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

    public void checkLocalActividad() {
        try {
            List<Actividad> pendientes = null;
            pendientes = getPendientes();
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
        //   new ActividadController().genActividad();
        //new SincronizarController().checkLocalActividad();

        if (new SincronizarController().hayConexion()) {
            System.out.println("SI SI SI::::");
            new SincronizarController().checkLocalActividad();
        } else {
            System.out.println("NO NO NO::::");
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
                new JpaRemote().getActividad().create(actividad);
                actividad.setSincronizacion(SincronizarEstado.SINCRONIZADO);
                jpaLocal.getActividad().edit(actividad);
            }
        } catch (Exception e) {
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
