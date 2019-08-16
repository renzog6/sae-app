/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.empleado;

import ar.nex.app.SaeUtils;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.EmpleadoPuesto;
import ar.nex.service.JpaService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renzo
 */
public class XTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            JpaService jpa = new JpaService();
            Empleado e = jpa.getEmpleado().findEmpleado(1L);
            System.out.println("ar.nex.empleado.XTest.main()>>>>> " + e.getNacimiento());
            System.out.println("ar.nex.empleado.XTest.main()>>>>> " + SaeUtils.getEdad(e.getNacimiento()));

            List<EmpleadoPuesto> lst = jpa.getEmpleadoPuesto().findEmpleadoPuestoEntities();
            System.out.println("ar.nex.empleado.XTest.main()::::::::: " + lst.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
