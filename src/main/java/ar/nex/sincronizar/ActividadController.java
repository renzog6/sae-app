package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.entity.Usuario;
import ar.nex.jpa.service.JpaRemote;
import ar.nex.jpa.service.JpaService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class ActividadController {

    private JpaService jpa;

    public ActividadController() {
        jpa = new JpaService();
    }

    /**
     *
     * @param object
     * @param objectID
     * @param desde
     */
    public void create(Object object, String objectID, String desde) {
        try {
            Usuario usuario = new JpaService().getUsuario().findUsuario(1L);
            Actividad actividad = new Actividad("create - " + desde, usuario.getUsername(), object);
            actividad.setEntityUuid(objectID);
            if (desde.equals("Remote")) {
                actividad.setSincronizacion(SincronizarEstado.SINCRONIZADO);
            }

            // actividad.getSincronizarCollection().add(new Sincronizar());
            new JpaService().getActividad().create(actividad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Actividad actividad) {
        try {
            new JpaService().getActividad().edit(actividad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Actividad actividad) {
        try {
            new JpaService().getActividad().destroy(actividad.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //new ActividadController().remoteList();
        // new ActividadController().remoteCreate();
        //new ActividadController().genActividad();

        // new ActividadController().genRemoteTest();
        new ActividadController().genConDispositivo();

    }

    public void genActividad() {
        try {
            for (int i = 1; i < 6; i++) {
                Item item = new Item("name " + i, "info " + i);
                jpa.getItem().create(item);
                create(item, item.getUuid(), "Local");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genRemoteTest() {
        try {
            JpaRemote jpaRemote = new JpaRemote();

            for (int i = 0; i < 5; i++) {

                Item item = new Item("remote " + i, "info " + i);
                jpaRemote.getItem().create(item);

                Actividad a = new Actividad("create - " + "genRemote", "Lalo", item);
                a.setEntityUuid(item.getUuid());
                a.setDevice("Gen X");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genConDispositivo() {
        try {
            List<Dispositivo> lstDP = new JpaService().getDispositivo().findDispositivoEntities();
            lstDP.forEach(e -> System.out.println(e.toString()));
            
            List<Actividad> lstAC = new JpaService().getActividad().findActividadEntities();
            for (Actividad a : lstAC) {
                System.out.println("Actividad::: "+a.toString());
                lstDP = a.getDispositivoList();
                for (Dispositivo d : lstDP) {
                    System.out.println(d.toString());
                }
            }
            //Actividad a = lstAC.get(3);
           // a.initDO();
            //a.getDispositivoList().add(lstDP.get(2));
           // new JpaService().getActividad().edit(a);
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
