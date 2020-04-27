package ar.nex.entity.empleado;

import ar.nex.entity.AdminEmpresa;
import ar.nex.jpa.service.JpaService;
import java.util.List;

/**
 *
 * @author Renzo
 */
public class PersonaTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            JpaService jpa = new JpaService();
//        Persona per = jpa.getPersona().findPersona(2L);
//
//            Familia fa = new Familia();
//            fa.setNombre("Familia 2");
//            fa.setApellido("Apellido");
//            fa.setRelacion("Hijo/a");
//            fa.setPariente(per);
//            jpa.getFamilia().create(fa);
//
////            Familia fa = jpa.getFamilia().findFamilia(13L);
////            System.out.println("Familia::: " + fa.toString());
////            
//            
//            List<Familia> lista = per.getFamiliaList();
//            for (Familia familia : lista) {
//                System.out.println("Familia de " + per.getNombreCompleto() + " -> " + familia.toString());
 //           }
//            Persona per2 = jpa.getPersona().findPersona(4L);
//            
//            fa.setPariente(per);
//            fa.setPersona(per2);
//            jpa.getFamilia().create(fa);

            // per.setFamiliaList(new ArrayList<>());
//            per.getFamiliaList().add(per2);
//            jpa.getFamilia().edit(fa);
//            jpa.getPersona().edit(per);
//            System.out.println("Persona::: "+per.getFamiliaList().toString());
            //Familia fa = jpa.getFamilia().findFamilia(4l);
//            Empleado em = new Empleado();
//            em.setNombre("Rodrigo 2");
//            em.setApellido("TEst");
//            em.setSexo("Otro");                        
//            jpa.getEmpleado().create(em);
            Persona p = jpa.getPersona().findPersona(29L);
            p.setEstado(PersonaEstado.ACTIVO);
            p.setEstadoCivil(EstadoCivil.OTRO);
            jpa.getPersona().edit(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
