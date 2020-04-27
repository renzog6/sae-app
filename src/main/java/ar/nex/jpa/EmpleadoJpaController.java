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
import ar.nex.entity.empleado.EmpleadoCategoria;
import ar.nex.entity.empleado.EmpleadoPuesto;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.Seguro;
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.entity.ubicacion.Contacto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.empleado.Familia;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getContactoList() == null) {
            empleado.setContactoList(new ArrayList<Contacto>());
        }
        if (empleado.getFamiliaList() == null) {
            empleado.setFamiliaList(new ArrayList<Familia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoCategoria categoria = empleado.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getIdCategoria());
                empleado.setCategoria(categoria);
            }
            EmpleadoPuesto puesto = empleado.getPuesto();
            if (puesto != null) {
                puesto = em.getReference(puesto.getClass(), puesto.getIdPuesto());
                empleado.setPuesto(puesto);
            }
            Empresa empresa = empleado.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                empleado.setEmpresa(empresa);
            }
            Seguro seguro = empleado.getSeguro();
            if (seguro != null) {
                seguro = em.getReference(seguro.getClass(), seguro.getIdSeguro());
                empleado.setSeguro(seguro);
            }
            Direccion domicilio = empleado.getDomicilio();
            if (domicilio != null) {
                domicilio = em.getReference(domicilio.getClass(), domicilio.getIdDireccion());
                empleado.setDomicilio(domicilio);
            }
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : empleado.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            empleado.setContactoList(attachedContactoList);
            List<Familia> attachedFamiliaList = new ArrayList<Familia>();
            for (Familia familiaListFamiliaToAttach : empleado.getFamiliaList()) {
                familiaListFamiliaToAttach = em.getReference(familiaListFamiliaToAttach.getClass(), familiaListFamiliaToAttach.getIdPersona());
                attachedFamiliaList.add(familiaListFamiliaToAttach);
            }
            empleado.setFamiliaList(attachedFamiliaList);
            em.persist(empleado);
            if (categoria != null) {
                categoria.getEmpleadoList().add(empleado);
                categoria = em.merge(categoria);
            }
            if (puesto != null) {
                puesto.getEmpleadoList().add(empleado);
                puesto = em.merge(puesto);
            }
            if (empresa != null) {
                empresa.getEmpleadoList().add(empleado);
                empresa = em.merge(empresa);
            }
            if (seguro != null) {
                seguro.getEmpleadoList().add(empleado);
                seguro = em.merge(seguro);
            }
            if (domicilio != null) {
                domicilio.getPersonaList().add(empleado);
                domicilio = em.merge(domicilio);
            }
            for (Contacto contactoListContacto : empleado.getContactoList()) {
                contactoListContacto.getPersonaList().add(empleado);
                contactoListContacto = em.merge(contactoListContacto);
            }
            for (Familia familiaListFamilia : empleado.getFamiliaList()) {
                ar.nex.entity.empleado.Persona oldParienteOfFamiliaListFamilia = familiaListFamilia.getPariente();
                familiaListFamilia.setPariente(empleado);
                familiaListFamilia = em.merge(familiaListFamilia);
                if (oldParienteOfFamiliaListFamilia != null) {
                    oldParienteOfFamiliaListFamilia.getFamiliaList().remove(familiaListFamilia);
                    oldParienteOfFamiliaListFamilia = em.merge(oldParienteOfFamiliaListFamilia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdPersona());
            EmpleadoCategoria categoriaOld = persistentEmpleado.getCategoria();
            EmpleadoCategoria categoriaNew = empleado.getCategoria();
            EmpleadoPuesto puestoOld = persistentEmpleado.getPuesto();
            EmpleadoPuesto puestoNew = empleado.getPuesto();
            Empresa empresaOld = persistentEmpleado.getEmpresa();
            Empresa empresaNew = empleado.getEmpresa();
            Seguro seguroOld = persistentEmpleado.getSeguro();
            Seguro seguroNew = empleado.getSeguro();
            Direccion domicilioOld = persistentEmpleado.getDomicilio();
            Direccion domicilioNew = empleado.getDomicilio();
            List<Contacto> contactoListOld = persistentEmpleado.getContactoList();
            List<Contacto> contactoListNew = empleado.getContactoList();
            List<Familia> familiaListOld = persistentEmpleado.getFamiliaList();
            List<Familia> familiaListNew = empleado.getFamiliaList();
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getIdCategoria());
                empleado.setCategoria(categoriaNew);
            }
            if (puestoNew != null) {
                puestoNew = em.getReference(puestoNew.getClass(), puestoNew.getIdPuesto());
                empleado.setPuesto(puestoNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                empleado.setEmpresa(empresaNew);
            }
            if (seguroNew != null) {
                seguroNew = em.getReference(seguroNew.getClass(), seguroNew.getIdSeguro());
                empleado.setSeguro(seguroNew);
            }
            if (domicilioNew != null) {
                domicilioNew = em.getReference(domicilioNew.getClass(), domicilioNew.getIdDireccion());
                empleado.setDomicilio(domicilioNew);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            empleado.setContactoList(contactoListNew);
            List<Familia> attachedFamiliaListNew = new ArrayList<Familia>();
            for (Familia familiaListNewFamiliaToAttach : familiaListNew) {
                familiaListNewFamiliaToAttach = em.getReference(familiaListNewFamiliaToAttach.getClass(), familiaListNewFamiliaToAttach.getIdPersona());
                attachedFamiliaListNew.add(familiaListNewFamiliaToAttach);
            }
            familiaListNew = attachedFamiliaListNew;
            empleado.setFamiliaList(familiaListNew);
            empleado = em.merge(empleado);
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getEmpleadoList().remove(empleado);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getEmpleadoList().add(empleado);
                categoriaNew = em.merge(categoriaNew);
            }
            if (puestoOld != null && !puestoOld.equals(puestoNew)) {
                puestoOld.getEmpleadoList().remove(empleado);
                puestoOld = em.merge(puestoOld);
            }
            if (puestoNew != null && !puestoNew.equals(puestoOld)) {
                puestoNew.getEmpleadoList().add(empleado);
                puestoNew = em.merge(puestoNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getEmpleadoList().remove(empleado);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getEmpleadoList().add(empleado);
                empresaNew = em.merge(empresaNew);
            }
            if (seguroOld != null && !seguroOld.equals(seguroNew)) {
                seguroOld.getEmpleadoList().remove(empleado);
                seguroOld = em.merge(seguroOld);
            }
            if (seguroNew != null && !seguroNew.equals(seguroOld)) {
                seguroNew.getEmpleadoList().add(empleado);
                seguroNew = em.merge(seguroNew);
            }
            if (domicilioOld != null && !domicilioOld.equals(domicilioNew)) {
                domicilioOld.getPersonaList().remove(empleado);
                domicilioOld = em.merge(domicilioOld);
            }
            if (domicilioNew != null && !domicilioNew.equals(domicilioOld)) {
                domicilioNew.getPersonaList().add(empleado);
                domicilioNew = em.merge(domicilioNew);
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getPersonaList().remove(empleado);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getPersonaList().add(empleado);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }
            for (Familia familiaListOldFamilia : familiaListOld) {
                if (!familiaListNew.contains(familiaListOldFamilia)) {
                    familiaListOldFamilia.setPariente(null);
                    familiaListOldFamilia = em.merge(familiaListOldFamilia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empleado.getIdPersona();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            EmpleadoCategoria categoria = empleado.getCategoria();
            if (categoria != null) {
                categoria.getEmpleadoList().remove(empleado);
                categoria = em.merge(categoria);
            }
            EmpleadoPuesto puesto = empleado.getPuesto();
            if (puesto != null) {
                puesto.getEmpleadoList().remove(empleado);
                puesto = em.merge(puesto);
            }
            Empresa empresa = empleado.getEmpresa();
            if (empresa != null) {
                empresa.getEmpleadoList().remove(empleado);
                empresa = em.merge(empresa);
            }
            Seguro seguro = empleado.getSeguro();
            if (seguro != null) {
                seguro.getEmpleadoList().remove(empleado);
                seguro = em.merge(seguro);
            }
            Direccion domicilio = empleado.getDomicilio();
            if (domicilio != null) {
                domicilio.getPersonaList().remove(empleado);
                domicilio = em.merge(domicilio);
            }
            List<Contacto> contactoList = empleado.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getPersonaList().remove(empleado);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<Familia> familiaList = empleado.getFamiliaList();
            for (Familia familiaListFamilia : familiaList) {
                familiaListFamilia.setPariente(null);
                familiaListFamilia = em.merge(familiaListFamilia);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
