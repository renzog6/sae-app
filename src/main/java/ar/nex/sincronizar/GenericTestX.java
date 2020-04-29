package ar.nex.sincronizar;

import ar.nex.entity.Item;
import ar.nex.jpa.service.JpaService;

/**
 *
 * @author Renzo
 */
public class GenericTestX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            JpaService service = new JpaService();
           // ItemUuid item = new ItemUuid();
           //Item item = new Item();
           Item item = new Item("Xds", "#dd");
            //service.getItem().edit(item);
           service.getItem().create(item);
            //service.getItemUuid().create(item);
            System.out.println("ar.nex.sincronizar.GenericTestX.main()" + item.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

//        item.setName("remame");
//        GenericJPA jpa = new GenericJPA();
//        jpa.setEntityManager(service.getFactory().createEntityManager());
//
//        jpa.update(item);
    }

}
