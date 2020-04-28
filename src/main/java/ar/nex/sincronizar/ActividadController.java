package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.entity.Usuario;
import ar.nex.jpa.service.JpaService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.json.JSONObject;

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
    public void create(String entity) {
        try {

        } catch (Exception e) {
        }
    }

//    private Long getLastObjectID(Class clazz) {
//        try {
//            EntityManager em = ...;
//CriteriaBuilder cb =  jpa.getFactory().getPersistenceUnitUtil().getIdentifier()// em.getCriteriaBuilder();
//            CriteriaQuery<Entity class  
//
//            
//            > cq = cb.createQuery(Entity.class  
//
//            
//            );
//Root<Entity> from = cq.from(Entity.class);
//            cq.select(Entity);
//            TypedQuery<Entity> q = em.createQuery(cq);
//            List<Entity> allitems = q.getResultList();
//
//        } catch (Exception e) {
//        }
//    }
    public void create(Object object, String objectID, String desde) {
        try {
            Usuario usuario = new JpaService().getUsuario().findUsuario(1L);
            Actividad actividad = new Actividad("create - " + desde, usuario.getUsername(), object);
            actividad.setEntityUuid(objectID);
            if (desde.equals("Remote")) {
                actividad.setSincronizacion(SincronizarEstado.SINCRONIZADO);
            }
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
        new ActividadController().genActividad();

    }

    public void genActividad() {
        try {
            for (int i = 209; i < 219; i++) {
                Item item = new Item("name " + i, "info " + i);
                jpa.getItem().create(item);

                Object ob = jpa.getFactory().getPersistenceUnitUtil().getIdentifier(item);
                System.out.println("ar.nex.sincronizar.ActividadController.genActividad() ob::: " + ob.toString()
                );
                create(item, item.getId().toString(), "Local");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
