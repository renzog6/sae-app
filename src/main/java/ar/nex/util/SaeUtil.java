/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.util;

import ar.nex.empleado.EmpleadoController;
import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.ubicacion.ContactoTipo;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.record.PageBreakRecord;

/**
 *
 * @author Renzo
 */
public class SaeUtil {

    private static final Logger LOG = LogManager.getLogger(EmpleadoController.class.getName());

    /**
     *
     * @param lst lista de contactos a buscar
     * @param tipo de de contacto a buscar
     * @return el dato del contacto requerido por el tipo
     */
    public static String getContactoDato(List<Contacto> lst, ContactoTipo tipo) {
        try {
            String dato = "Sin datos";
            if (lst != null) {

                for (Contacto c : lst) {
                    if (c.getTipo() == tipo) {
                        dato = c.getDato();
                        break;
                    }
                }
            }
            return dato;
        } catch (Exception e) {
            LOG.log(Level.FATAL, e);
            return "Error";
        }
    }
}
