package ar.nex.jpa.service;

import ar.nex.jpa.ActividadJpaController;
import ar.nex.jpa.AdminEmpresaJpaController;
import ar.nex.jpa.ContactoJpaController;
import ar.nex.jpa.DireccionJpaController;
import ar.nex.jpa.DispositivoJpaController;
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

import ar.nex.jpa.LocalidadJpaController;
import ar.nex.jpa.MarcaJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.PersonaJpaController;
import ar.nex.jpa.ProvinciaJpaController;
import ar.nex.jpa.RepuestoJpaController;
import ar.nex.jpa.RubroJpaController;
import ar.nex.jpa.SeguroJpaController;
import ar.nex.jpa.SincronizarJpaController;

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

    /**
     *
     * @return factory = Persistence.createEntityManagerFactory("SaeRemote");
     */
    public EntityManagerFactory getFactoryRemote() {
        if (factory == null) {
            this.factory = Persistence.createEntityManagerFactory("SaeRemote");
        }
        return factory;
    }

    public AdminEmpresaJpaController getAdminEmpresa() {
        return new AdminEmpresaJpaController(factory);
    }

    public EmpresaJpaController getEmpresa() {
        return new EmpresaJpaController(factory);
    }

    public MarcaJpaController getMarca() {
        return new MarcaJpaController(factory);
    }

    public EquipoJpaController getEquipo() {
        return new EquipoJpaController(factory);
    }

    public EquipoDocumentacionJpaController getEquipoDocumentacion() {
        return new EquipoDocumentacionJpaController(factory);
    }

    public EquipoCategoriaJpaController getEquipoCategoria() {
        return new EquipoCategoriaJpaController(factory);
    }

    public EquipoModeloJpaController getEquipoModelo() {
        return new EquipoModeloJpaController(factory);
    }

    public EquipoTipoJpaController getEquipoTipo() {
        return new EquipoTipoJpaController(factory);
    }

    public EquipoCompraVentaJpaController getEquipoCompraVenta() {
        return new EquipoCompraVentaJpaController(factory);
    }

    public GasoilJpaController getGasoil() {
        return new GasoilJpaController(factory);
    }

    public PedidoJpaController getPedido() {
        return new PedidoJpaController(factory);
    }

    public RepuestoJpaController getRepuesto() {
        return new RepuestoJpaController(factory);
    }

    public UsuarioJpaController getUsuario() {
        return new UsuarioJpaController(factory);
    }

    public EmpleadoJpaController getEmpleado() {
        return new EmpleadoJpaController(factory);
    }

    public EmpleadoCategoriaJpaController getEmpleadoCategoria() {
        return new EmpleadoCategoriaJpaController(factory);
    }

    public EmpleadoPuestoJpaController getEmpleadoPuesto() {
        return new EmpleadoPuestoJpaController(factory);
    }

    public PersonaJpaController getPersona() {
        return new PersonaJpaController(factory);
    }

    public FamiliaJpaController getFamilia() {
        return new FamiliaJpaController(factory);
    }

    public ProvinciaJpaController getProvincia() {
        return new ProvinciaJpaController(factory);
    }

    public LocalidadJpaController getLocalidad() {
        return new LocalidadJpaController(factory);
    }

    public DireccionJpaController getDireccion() {
        return new DireccionJpaController(factory);
    }

    public ContactoJpaController getContacto() {
        return new ContactoJpaController(factory);
    }

    public RubroJpaController getRubro() {
        return new RubroJpaController(factory);
    }

    public TransporteJpaController getTransporte() {
        return new TransporteJpaController(factory);
    }

    /**
     *
     * @return Gasto Jpa Controller
     */
    public GastoJpaController getGasto() {
        return new GastoJpaController(factory);
    }

    /**
     *
     * @return Seguro Jpa Controller
     */
    public SeguroJpaController getSeguro() {
        return new SeguroJpaController(factory);
    }

    /**
     *
     * @return ItemJpaController
     */
    public ItemJpaController getItem() {
        return new ItemJpaController(factory);
    }

    /**
     *
     * @return ActividadJpaController
     */
    public ActividadJpaController getActividad() {
        return new ActividadJpaController(factory);
    }

    /**
     *
     * @return SincronizarJpaController
     */
    public SincronizarJpaController getSincronizar() {
        return new SincronizarJpaController(factory);
    }
    
    /**
     *
     * @return DispositivoJpaController
     */
    public DispositivoJpaController getDispositivo() {
        return new DispositivoJpaController(factory);
    }
}
