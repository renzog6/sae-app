/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.empresa.Rubro;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.ubicacion.Contacto;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getRubroList() == null) {
            empresa.setRubroList(new ArrayList<Rubro>());
        }
        if (empresa.getContactoList() == null) {
            empresa.setContactoList(new ArrayList<Contacto>());
        }

        if (empresa.getEmpleadoList() == null) {
            empresa.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (empresa.getPedidoList() == null) {
            empresa.setPedidoList(new ArrayList<Pedido>());
        }
        if (empresa.getEquipoList() == null) {
            empresa.setEquipoList(new ArrayList<Equipo>());
        }
        if (empresa.getDireccionList() == null) {
            empresa.setDireccionList(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rubro> attachedRubroList = new ArrayList<Rubro>();
            for (Rubro rubroListRubroToAttach : empresa.getRubroList()) {
                rubroListRubroToAttach = em.getReference(rubroListRubroToAttach.getClass(), rubroListRubroToAttach.getIdRubro());
                attachedRubroList.add(rubroListRubroToAttach);
            }
            empresa.setRubroList(attachedRubroList);
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : empresa.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            empresa.setContactoList(attachedContactoList);
            
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : empresa.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getIdPedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            empresa.setPedidoList(attachedPedidoList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : empresa.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            empresa.setEquipoList(attachedEquipoList);
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : empresa.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getIdDireccion());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            empresa.setDireccionList(attachedDireccionList);
            em.persist(empresa);
            for (Rubro rubroListRubro : empresa.getRubroList()) {
                rubroListRubro.getEmpresaList().add(empresa);
                rubroListRubro = em.merge(rubroListRubro);
            }
            for (Contacto contactoListContacto : empresa.getContactoList()) {
                contactoListContacto.getEmpresaList().add(empresa);
                contactoListContacto = em.merge(contactoListContacto);
            }

            for (Empleado empleadoListEmpleado : empresa.getEmpleadoList()) {
                Empresa oldEmpresaOfEmpleadoListEmpleado = empleadoListEmpleado.getEmpresa();
                empleadoListEmpleado.setEmpresa(empresa);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldEmpresaOfEmpleadoListEmpleado != null) {
                    oldEmpresaOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldEmpresaOfEmpleadoListEmpleado = em.merge(oldEmpresaOfEmpleadoListEmpleado);
                }
            }
            for (Pedido pedidoListPedido : empresa.getPedidoList()) {
                Empresa oldEmpresaOfPedidoListPedido = pedidoListPedido.getEmpresa();
                pedidoListPedido.setEmpresa(empresa);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldEmpresaOfPedidoListPedido != null) {
                    oldEmpresaOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldEmpresaOfPedidoListPedido = em.merge(oldEmpresaOfPedidoListPedido);
                }
            }
            for (Equipo equipoListEquipo : empresa.getEquipoList()) {
                Empresa oldEmpresaOfEquipoListEquipo = equipoListEquipo.getEmpresa();
                equipoListEquipo.setEmpresa(empresa);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldEmpresaOfEquipoListEquipo != null) {
                    oldEmpresaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldEmpresaOfEquipoListEquipo = em.merge(oldEmpresaOfEquipoListEquipo);
                }
            }
            for (Direccion direccionListDireccion : empresa.getDireccionList()) {
                direccionListDireccion.getEmpresaList().add(empresa);
                direccionListDireccion = em.merge(direccionListDireccion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            List<Rubro> rubroListOld = persistentEmpresa.getRubroList();
            List<Rubro> rubroListNew = empresa.getRubroList();
            List<Contacto> contactoListOld = persistentEmpresa.getContactoList();
            List<Contacto> contactoListNew = empresa.getContactoList();
            List<Empleado> empleadoListOld = persistentEmpresa.getEmpleadoList();
            List<Empleado> empleadoListNew = empresa.getEmpleadoList();
            List<Pedido> pedidoListOld = persistentEmpresa.getPedidoList();
            List<Pedido> pedidoListNew = empresa.getPedidoList();
            List<Equipo> equipoListOld = persistentEmpresa.getEquipoList();
            List<Equipo> equipoListNew = empresa.getEquipoList();
            List<Direccion> direccionListOld = persistentEmpresa.getDireccionList();
            List<Direccion> direccionListNew = empresa.getDireccionList();
            List<String> illegalOrphanMessages = null;
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Equipo " + equipoListOldEquipo + " since its empresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rubro> attachedRubroListNew = new ArrayList<Rubro>();
            for (Rubro rubroListNewRubroToAttach : rubroListNew) {
                rubroListNewRubroToAttach = em.getReference(rubroListNewRubroToAttach.getClass(), rubroListNewRubroToAttach.getIdRubro());
                attachedRubroListNew.add(rubroListNewRubroToAttach);
            }
            rubroListNew = attachedRubroListNew;
            empresa.setRubroList(rubroListNew);
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            empresa.setContactoList(contactoListNew);
            
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getIdPedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            empresa.setPedidoList(pedidoListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            empresa.setEquipoList(equipoListNew);
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getIdDireccion());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            empresa.setDireccionList(direccionListNew);
            empresa = em.merge(empresa);
            for (Rubro rubroListOldRubro : rubroListOld) {
                if (!rubroListNew.contains(rubroListOldRubro)) {
                    rubroListOldRubro.getEmpresaList().remove(empresa);
                    rubroListOldRubro = em.merge(rubroListOldRubro);
                }
            }
            for (Rubro rubroListNewRubro : rubroListNew) {
                if (!rubroListOld.contains(rubroListNewRubro)) {
                    rubroListNewRubro.getEmpresaList().add(empresa);
                    rubroListNewRubro = em.merge(rubroListNewRubro);
                }
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getEmpresaList().remove(empresa);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getEmpresaList().add(empresa);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }

            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    empleadoListOldEmpleado.setEmpresa(null);
                    empleadoListOldEmpleado = em.merge(empleadoListOldEmpleado);
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Empresa oldEmpresaOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getEmpresa();
                    empleadoListNewEmpleado.setEmpresa(empresa);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldEmpresaOfEmpleadoListNewEmpleado != null && !oldEmpresaOfEmpleadoListNewEmpleado.equals(empresa)) {
                        oldEmpresaOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldEmpresaOfEmpleadoListNewEmpleado = em.merge(oldEmpresaOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    pedidoListOldPedido.setEmpresa(null);
                    pedidoListOldPedido = em.merge(pedidoListOldPedido);
                }
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Empresa oldEmpresaOfPedidoListNewPedido = pedidoListNewPedido.getEmpresa();
                    pedidoListNewPedido.setEmpresa(empresa);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldEmpresaOfPedidoListNewPedido != null && !oldEmpresaOfPedidoListNewPedido.equals(empresa)) {
                        oldEmpresaOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldEmpresaOfPedidoListNewPedido = em.merge(oldEmpresaOfPedidoListNewPedido);
                    }
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Empresa oldEmpresaOfEquipoListNewEquipo = equipoListNewEquipo.getEmpresa();
                    equipoListNewEquipo.setEmpresa(empresa);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldEmpresaOfEquipoListNewEquipo != null && !oldEmpresaOfEquipoListNewEquipo.equals(empresa)) {
                        oldEmpresaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldEmpresaOfEquipoListNewEquipo = em.merge(oldEmpresaOfEquipoListNewEquipo);
                    }
                }
            }
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    direccionListOldDireccion.getEmpresaList().remove(empresa);
                    direccionListOldDireccion = em.merge(direccionListOldDireccion);
                }
            }
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    direccionListNewDireccion.getEmpresaList().add(empresa);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Equipo> equipoListOrphanCheck = empresa.getEquipoList();
            for (Equipo equipoListOrphanCheckEquipo : equipoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Equipo " + equipoListOrphanCheckEquipo + " in its equipoList field has a non-nullable empresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rubro> rubroList = empresa.getRubroList();
            for (Rubro rubroListRubro : rubroList) {
                rubroListRubro.getEmpresaList().remove(empresa);
                rubroListRubro = em.merge(rubroListRubro);
            }
            List<Contacto> contactoList = empresa.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getEmpresaList().remove(empresa);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<Empleado> empleadoList = empresa.getEmpleadoList();
            for (Empleado empleadoListEmpleado : empleadoList) {
                empleadoListEmpleado.setEmpresa(null);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
            }
            List<Pedido> pedidoList = empresa.getPedidoList();
            for (Pedido pedidoListPedido : pedidoList) {
                pedidoListPedido.setEmpresa(null);
                pedidoListPedido = em.merge(pedidoListPedido);
            }
            List<Direccion> direccionList = empresa.getDireccionList();
            for (Direccion direccionListDireccion : direccionList) {
                direccionListDireccion.getEmpresaList().remove(empresa);
                direccionListDireccion = em.merge(direccionListDireccion);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Empresa findEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
