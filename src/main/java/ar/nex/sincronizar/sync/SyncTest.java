package ar.nex.sincronizar.sync;

import ar.nex.entity.Item;
import ar.nex.entity.Usuario;
import ar.nex.sincronizar.Actividad;
import ar.nex.sincronizar.ActividadController;
import ar.nex.sincronizar.SincronizarController;
import ar.nex.jpa.service.JpaService;
import ar.nex.sincronizar.ApiSync;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class SyncTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Item item = null;// new JpaService().getItem().findItem(1L);

        // new ActividadController().create(item,"Local");
//        System.out.println("Actividad::: " +act.toString());
        // new JpaService().getActividad().create(act);
//        Actividad a = new JpaService().getActividad().findActividad(1L);
//        System.out.println("Actividad::: " + a.toString());
        System.out.println("ar.nex.sincronizar.sync.SyncTest.main()" + item);
        new ApiSync().create(item);
        try {
            System.out.println("ar.nex.entity.sincronizar.sync.SyncTest.main()");
            //new SincronizarController().syncToRemote();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
