package ar.nex.empresa;

import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.empresa.Rubro;
import ar.nex.entity.ubicacion.Direccion;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Renzo
 */
public class EmpesaUtil {

    public EmpesaUtil() {
    }

    public String getListaRubrosString(List<Rubro> rubros) {
        if (!rubros.isEmpty() && rubros != null) {
            if (rubros.size() >= 2) {
                String lst = "";
                for (Rubro r : rubros) {
                    lst += r.getNombre() + " - ";
                }
                return lst.substring(0, lst.length() - 3);
            } else {
                return rubros.get(0).getNombre();
            }
        }
        return "Sin Rubro";
    }

    public ObservableList getListaDirecciones(List<Direccion> direcciones) {                
        if (!direcciones.isEmpty() && direcciones != null) {
                ObservableList lst = FXCollections.observableArrayList();
                for (Direccion r : direcciones) {                    
                    lst.add(r);
                }
                return lst;          
        }
        return null;
    }
    
    public ObservableList getListaContactos(List<Contacto> contactos) {                
        if (!contactos.isEmpty() && contactos != null) {
                ObservableList lst = FXCollections.observableArrayList();
                for (Contacto r : contactos) {                    
                    lst.add(r);
                }
                return lst;          
        }
        return null;
    }
}
