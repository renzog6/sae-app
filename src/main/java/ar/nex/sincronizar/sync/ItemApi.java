package ar.nex.sincronizar.sync;

import ar.nex.entity.Item;
import ar.nex.sincronizar.Actividad;
import ar.nex.sincronizar.ActividadController;
import ar.nex.sincronizar.SincronizarEstado;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.jpa.service.JpaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class ItemApi {

    private JpaService jpa;

    public void create(Item toCreate) throws Exception {
        try {
            jpa = new JpaService();
            Item item = jpa.getItem().findItem(toCreate.getId());

            if (item != null) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
                List<Actividad> actividadList = new ActividadController().getPendientes();
                for (Actividad a : actividadList) {
                    Item i = gson.fromJson(a.getEntityJson(), Item.class);
                    if (Objects.equals(i.getId(), item.getId())) {
                        System.out.println("ar.nex.entity.sincronizar.sync.ItemApi.create()::: " + a.getEntityJson());
                        new ActividadController().delete(a);
                    }
                }                
                item.setId(getLastId() + 1);
                jpa.getItem().create(item);                
              //  new ActividadController().create(item, "Local");
                jpa.getItem().edit(toCreate);
                //new ActividadController().create(toCreate, "Remote");
            } else {
                jpa.getItem().create(toCreate);
                //new ActividadController().create(toCreate, "Remote");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Long getLastId() {
        try {
            EntityManager em = new JpaService().getFactory().createEntityManager();
            TypedQuery<Item> query
                    = em.createQuery("SELECT obj FROM Item obj order by obj.id desc", Item.class);
            List<Item> results = query.setMaxResults(1).getResultList();            
            if (!results.isEmpty()) {
                return results.get(0).getId()+1;
            } else {
                return null;
            }
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }

    public void updateActividad() {
    }

    public void update() {
    }

    public void destroy() {
    }

}
