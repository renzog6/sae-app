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
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.entity.ubicacion.Contacto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.empleado.Familia;
import ar.nex.entity.empleado.Persona;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) {
        if (persona.getContactoList() == null) {
            persona.setContactoList(new ArrayList<Contacto>());
        }
        if (persona.getFamiliaList() == null) {
            persona.setFamiliaList(new ArrayList<Familia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion domicilio = persona.getDomicilio();
            if (domicilio != null) {
                domicilio = em.getReference(domicilio.getClass(), domicilio.getIdDireccion());
                persona.setDomicilio(domicilio);
            }
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : persona.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            persona.setContactoList(attachedContactoList);
            List<Familia> attachedFamiliaList = new ArrayList<Familia>();
            for (Familia familiaListFamiliaToAttach : persona.getFamiliaList()) {
                familiaListFamiliaToAttach = em.getReference(familiaListFamiliaToAttach.getClass(), familiaListFamiliaToAttach.getIdPersona());
                attachedFamiliaList.add(familiaListFamiliaToAttach);
            }
            persona.setFamiliaList(attachedFamiliaList);
            em.persist(persona);
            if (domicilio != null) {
                domicilio.getPersonaList().add(persona);
                domicilio = em.merge(domicilio);
            }
            for (Contacto contactoListContacto : persona.getContactoList()) {
                contactoListContacto.getPersonaList().add(persona);
                contactoListContacto = em.merge(contactoListContacto);
            }
            for (Familia familiaListFamilia : persona.getFamiliaList()) {
                Persona oldParienteOfFamiliaListFamilia = familiaListFamilia.getPariente();
                familiaListFamilia.setPariente(persona);
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

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getIdPersona());
            Direccion domicilioOld = persistentPersona.getDomicilio();
            Direccion domicilioNew = persona.getDomicilio();
            List<Contacto> contactoListOld = persistentPersona.getContactoList();
            List<Contacto> contactoListNew = persona.getContactoList();
            List<Familia> familiaListOld = persistentPersona.getFamiliaList();
            List<Familia> familiaListNew = persona.getFamiliaList();
            if (domicilioNew != null) {
                domicilioNew = em.getReference(domicilioNew.getClass(), domicilioNew.getIdDireccion());
                persona.setDomicilio(domicilioNew);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            persona.setContactoList(contactoListNew);
            List<Familia> attachedFamiliaListNew = new ArrayList<Familia>();
            for (Familia familiaListNewFamiliaToAttach : familiaListNew) {
                familiaListNewFamiliaToAttach = em.getReference(familiaListNewFamiliaToAttach.getClass(), familiaListNewFamiliaToAttach.getIdPersona());
                attachedFamiliaListNew.add(familiaListNewFamiliaToAttach);
            }
            familiaListNew = attachedFamiliaListNew;
            persona.setFamiliaList(familiaListNew);
            persona = em.merge(persona);
            if (domicilioOld != null && !domicilioOld.equals(domicilioNew)) {
                domicilioOld.getPersonaList().remove(persona);
                domicilioOld = em.merge(domicilioOld);
            }
            if (domicilioNew != null && !domicilioNew.equals(domicilioOld)) {
                domicilioNew.getPersonaList().add(persona);
                domicilioNew = em.merge(domicilioNew);
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.getPersonaList().remove(persona);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    contactoListNewContacto.getPersonaList().add(persona);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                }
            }
            for (Familia familiaListOldFamilia : familiaListOld) {
                if (!familiaListNew.contains(familiaListOldFamilia)) {
                    familiaListOldFamilia.setPariente(null);
                    familiaListOldFamilia = em.merge(familiaListOldFamilia);
                }
            }
            for (Familia familiaListNewFamilia : familiaListNew) {
                if (!familiaListOld.contains(familiaListNewFamilia)) {
                    Persona oldParienteOfFamiliaListNewFamilia = familiaListNewFamilia.getPariente();
                    familiaListNewFamilia.setPariente(persona);
                    familiaListNewFamilia = em.merge(familiaListNewFamilia);
                    if (oldParienteOfFamiliaListNewFamilia != null && !oldParienteOfFamiliaListNewFamilia.equals(persona)) {
                        oldParienteOfFamiliaListNewFamilia.getFamiliaList().remove(familiaListNewFamilia);
                        oldParienteOfFamiliaListNewFamilia = em.merge(oldParienteOfFamiliaListNewFamilia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = persona.getIdPersona();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getIdPersona();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            Direccion domicilio = persona.getDomicilio();
            if (domicilio != null) {
                domicilio.getPersonaList().remove(persona);
                domicilio = em.merge(domicilio);
            }
            List<Contacto> contactoList = persona.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.getPersonaList().remove(persona);
                contactoListContacto = em.merge(contactoListContacto);
            }
            List<Familia> familiaList = persona.getFamiliaList();
            for (Familia familiaListFamilia : familiaList) {
                familiaListFamilia.setPariente(null);
                familiaListFamilia = em.merge(familiaListFamilia);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
