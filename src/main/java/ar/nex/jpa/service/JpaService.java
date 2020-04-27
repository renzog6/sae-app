package ar.nex.jpa.service;

import ar.nex.jpa.ActividadJpaController;
import ar.nex.jpa.AdminEmpresaJpaController;
import ar.nex.jpa.ContactoJpaController;
import ar.nex.jpa.DireccionJpaController;
import ar.nex.jpa.EmpleadoCategoriaJpaController;
import ar.nex.jpa.EmpleadoJpaController;
import ar.nex.jpa.EmpleadoPuestoJpaController;
import ar.nex.jpa.EmpresaJpaController;
import ar.nex.jpa.EquipoCategoriaJpaController;
import ar.nex.jpa.EquipoCompraVentaJpaController;
import ar.nex.jpa.EquipoDocumentacionJpaController;
import ar.nex.jpa.EquipoJpaController;
import ar.nex.jpa.EquipoModeloJpaController;
import ar.nex.jpa.EquipoTipoJpaController;
import ar.nex.jpa.FamiliaJpaController;
import ar.nex.jpa.GasoilJpaController;
import ar.nex.jpa.GastoJpaController;
import ar.nex.jpa.ItemJpaController;
import ar.nex.jpa.ItemUuidJpaController;

import ar.nex.jpa.LocalidadJpaController;
import ar.nex.jpa.MarcaJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.PersonaJpaController;
import ar.nex.jpa.ProvinciaJpaController;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.jpa.RubroJpaController;
import ar.nex.jpa.SeguroJpaController;

import ar.nex.jpa.TransporteJpaController;
import ar.nex.jpa.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Renzo
 */
public final class JpaService {

    private final static Logger LOGGER = LogManager.getLogger(JpaService.class.getName());

    private EntityManagerFactory factory;

    public static void init() {
        LOGGER.info("JpaService init()");
    }

    /**
     *
     * Constructor de JPA Services.
     */
    public JpaService() {
        this.factory = getFactory();
    }

    /**
     *
     * @return factory = Persistence.createEntityManagerFactory("SaeFxPU");
     */
    public EntityManagerFactory getFactory() {
        if (factory == null) {
            this.factory = Persistence.createEntityManagerFactory("SaeFxPU");
        }
        return factory;
    }

    public void flushPersist(){
        //this.factory.getPersistenceUnitUtil().
    }
    
    
    public AdminEmpresaJpaController getAdminEmpresa() {
        return new AdminEmpresaJpaController(getFactory());
    }

    public EmpresaJpaController getEmpresa() {
        return new EmpresaJpaController(getFactory());
    }

    public MarcaJpaController getMarca() {
        return new MarcaJpaController(getFactory());
    }

    public EquipoJpaController getEquipo() {
        return new EquipoJpaController(getFactory());
    }

    public EquipoDocumentacionJpaController getEquipoDocumentacion() {
        return new EquipoDocumentacionJpaController(getFactory());
    }

    public EquipoCategoriaJpaController getEquipoCategoria() {
        return new EquipoCategoriaJpaController(getFactory());
    }

    public EquipoModeloJpaController getEquipoModelo() {
        return new EquipoModeloJpaController(getFactory());
    }

    public EquipoTipoJpaController getEquipoTipo() {
        return new EquipoTipoJpaController(getFactory());
    }

    public EquipoCompraVentaJpaController getEquipoCompraVenta() {
        return new EquipoCompraVentaJpaController(getFactory());
    }

    public GasoilJpaController getGasoil() {
        return new GasoilJpaController(getFactory());
    }

    public PedidoJpaController getPedido() {
        return new PedidoJpaController(getFactory());
    }

    public RepuestoJpaController getRepuesto() {
        return new RepuestoJpaController(getFactory());
    }

    public UsuarioJpaController getUsuario() {
        return new UsuarioJpaController(getFactory());
    }

    public EmpleadoJpaController getEmpleado() {
        return new EmpleadoJpaController(getFactory());
    }

    public EmpleadoCategoriaJpaController getEmpleadoCategoria() {
        return new EmpleadoCategoriaJpaController(getFactory());
    }

    public EmpleadoPuestoJpaController getEmpleadoPuesto() {
        return new EmpleadoPuestoJpaController(getFactory());
    }

    public PersonaJpaController getPersona() {
        return new PersonaJpaController(getFactory());
    }

    public FamiliaJpaController getFamilia() {
        return new FamiliaJpaController(getFactory());
    }

    public ProvinciaJpaController getProvincia() {
        return new ProvinciaJpaController(getFactory());
    }

    public LocalidadJpaController getLocalidad() {
        return new LocalidadJpaController(getFactory());
    }

    public DireccionJpaController getDireccion() {
        return new DireccionJpaController(getFactory());
    }

    public ContactoJpaController getContacto() {
        return new ContactoJpaController(getFactory());
    }

    public RubroJpaController getRubro() {
        return new RubroJpaController(getFactory());
    }

    public TransporteJpaController getTransporte() {
        return new TransporteJpaController(getFactory());
    }

    /**
     *
     * @return Gasto Jpa Controller
     */
    public GastoJpaController getGasto() {
        return new GastoJpaController(getFactory());
    }

    /**
     *
     * @return Seguro Jpa Controller
     */
    public SeguroJpaController getSeguro() {
        return new SeguroJpaController(getFactory());
    }

    /**
     *
     * @return ItemJpaController
     */
    public ItemJpaController getItem() {
        return new ItemJpaController(getFactory());
    }

    public ItemUuidJpaController getItemUuid() {
        return new ItemUuidJpaController(getFactory());
    }

    /**
     *
     * @return ActividadJpaController
     */
    public ActividadJpaController getActividad() {
        return new ActividadJpaController(getFactory());
    }

    /**
     *
     * @return SincronizarJpaController
     */
//    public SincronizarJpaController getSincronizar() {
//        return new SincronizarJpaController(getFactory());
//    }
}
