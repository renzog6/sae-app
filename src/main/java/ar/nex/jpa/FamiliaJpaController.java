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
import ar.nex.entity.empleado.Persona;
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
public class FamiliaJpaController implements Serializable {

    public FamiliaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Familia familia) {
        if (familia.getContactoList() == null) {
            familia.setContactoList(new ArrayList<Contacto>());
        }
        if (familia.getFamiliaList() == null) {
            familia.setFamiliaList(new ArrayList<Familia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona pariente = familia.getPariente();
            if (pariente != null) {
                pariente = em.getReference(pariente.getClass(), pariente.getIdPersona());
                familia.setPariente(pariente);
            }
            Direccion domicilio = familia.getDomicilio();
            if (domicilio != null) {
                domicilio = em.getReference(domicilio.getClass(), domicilio.getIdDireccion());
                familia.setDomicilio(domicilio);
            }
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : familia.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            familia.setContactoList(attachedContactoList);
            List<Familia> attachedFamiliaList = new ArrayList<Familia>();
            for (Familia familiaListFamiliaToAttach : familia.getFamiliaList()) {
                familiaListFamiliaToAttach = em.getReference(familiaListFamiliaToAttach.getClass(), familiaListFamiliaToAttach.getIdPersona());
                attachedFamiliaList.add(familiaListFamiliaToAttach);
            }
            familia.setFamiliaList(attachedFamiliaList);
            em.persist(familia);
            if (pariente != null) {
                pariente.getFamiliaList().add(familia);
                pariente = em.merge(pariente);
            }
            if (domicilio != null) {
                domicilio.getPersonaList().add(familia);
                domicilio = em.merge(domicilio);
            }
            for (Contacto contactoListContacto : familia.getContactoList()) {
                contactoListContacto.getPersonaList().add(familia);
                contactoListContacto = em.merge(contactoListContacto);
            }
            for (Familia familiaListFamilia : familia.getFamiliaList()) {
                ar.nex.entity.empleado.Persona oldParienteOfFamiliaListFamilia = familiaListFamilia.getPariente();
                familiaListFamilia.setPariente(familia);
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

    public void edit(Familia familia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia persistentFamilia = em.find(Familia.class, familia.getIdPersona());
            Persona parienteOld = persistentFamilia.getPariente();
            Persona parienteNew = familia.getPariente();
            Direccion domicilioOld = persistentFamilia.getDomicilio();
            Direccion domicilioNew = familia.getDomicilio();
            List<Contacto> contactoListOld = persistentFamilia.getContactoList();
            List<Contacto> contactoListNew = familia.getContactoList();
            List<Familia> familiaListOld = persistentFamilia.getFamiliaList();
            List<Familia> familiaListNew = familia.getFamiliaList();
            if (parienteNew != null) {
                parienteNew = em.getReference(parienteNew.getClass(), parienteNew.getIdPersona());
                familia.setPariente(parienteNew);
            }
            if (domicilioNew != null) {
                domicilioNew = em.getReference(domicilioNew.getClass(), domicilioNew.getIdDireccion());
                familia.setDomicilio(domicilioNew);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            familia.setContactoList(contactoListNew);
            List<Familia> attachedFamiliaListNew = new ArrayList<Familia>();
            for (Familia familiaListNewFamiliaToAttach : familiaListNew) {
                familiaListNewFamiliaToAttach = em.getReference(familiaListNewFamiliaToAttach.getClass(), familiaListNewFamiliaToAttach.getIdPersona());
                attachedFamiliaListNew.add(familiaListNewFamiliaToAttach);
            }
            familiaListNew = attachedFamiliaListNew;
            familia.setFamiliaList(familiaListNew);
            familia = em.merge(familia);
            if (parienteOld != null && !parienteOld.equals(parienteNew)) {
                parienteOld.getFamiliaList().remove(familia);
                parienteOld = em.merge(parienteOld);
            }
            if (parienteNew != null && !parienteNew.equals(parienteOld)) {
                parienteNew.getFamiliaList().add(familia);
                parienteNew = em.merge(parienteNew);
            }
            if (domicilioOld != null && !domicilioOld.equals(domicilioNew)) {
                domicilioOld.getPersonaList().remove(familia);
                domicilioOld = em.merge(domicilioOld);
            }
            if (domicilioNew != null && !domicilioNew.equals(domicilioOld)) {
                domicilioNew.getPersonaList().add(familia);
                domicilioNew = em.merge(domicilioNew);
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getPersonaList().remove(familia);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getPersonaList().add(familia);
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
                Long id = familia.getIdPersona();
                if (findFamilia(id) == null) {
                    throw new NonexistentEntityException("The familia with id " + id + " no longer exists.");
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
            Familia familia;
            try {
                familia = em.getReference(Familia.class, id);
                familia.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The familia with id " + id + " no longer exists.", enfe);
            }
            Persona pariente = familia.getPariente();
            if (pariente != null) {
                pariente.getFamiliaList().remove(familia);
                pariente = em.merge(pariente);
            }
            Direccion domicilio = familia.getDomicilio();
            if (domicilio != null) {
                domicilio.getPersonaList().remove(familia);
                domicilio = em.merge(domicilio);
            }
            List<Contacto> contactoList = familia.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getPersonaList().remove(familia);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<Familia> familiaList = familia.getFamiliaList();
            for (Familia familiaListFamilia : familiaList) {
                familiaListFamilia.setPariente(null);
                familiaListFamilia = em.merge(familiaListFamilia);
            }
            em.remove(familia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Familia> findFamiliaEntities() {
        return findFamiliaEntities(true, -1, -1);
    }

    public List<Familia> findFamiliaEntities(int maxResults, int firstResult) {
        return findFamiliaEntities(false, maxResults, firstResult);
    }

    private List<Familia> findFamiliaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Familia.class));
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

    public Familia findFamilia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);
        } finally {
            em.close();
        }
    }

    public int getFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Familia> rt = cq.from(Familia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
