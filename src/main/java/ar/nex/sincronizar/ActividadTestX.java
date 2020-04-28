package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.jpa.ItemxJpaController;
import ar.nex.jpa.service.JpaService;
import ar.nex.sincronizar.sync.Itemx;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

/**
 *
 * @author Renzo
 */
public class ActividadTestX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            JpaService jpa = new JpaService();

            ItemxJpaController jpaItemx = new ItemxJpaController(jpa.getFactory());
            
            Itemx ix = new Itemx();
            ix.setName("Item UUID 333");
            ix.setInfo("UUID X 333");
            System.out.println("::: " +ix.getUuid());
            jpaItemx.create(ix);

            //jpa.getItem().getEntityManager().setFlushMode(FlushModeType.AUTO);
            // Item i = new Item("Kote", "KKK");
            // jpa.getItem().create(i);
            //i = jpa.getItem().findItem(400L);
            //System.out.println("ar.nex.sincronizar.ActividadTestX.main() " + i.getId());
            // jpa.getFactory().close();
//            jpa = new JpaService();
//            Item i = jpa.getItem().findItem(400L);
//            i.setId(415L);
//            jpa.getItem().edit(i);
//            
//            i = jpa.getItem().findItem(400L);
//            jpa.getItem().destroy(i.getId());
//            System.out.println("ar.nex.sincronizar.ActividadTestX.main() " + i.getId());
//            List<Actividad> lst = jpa.getActividad().findActividadEntities();
//                       
//            
//            for (Actividad a : lst) {
//                System.out.println("ar.nex.sincronizar.ActividadTestX.main()::: " + a.getDevice());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
