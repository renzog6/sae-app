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
import ar.nex.entity.ubicacion.Localidad;
import ar.nex.entity.empresa.Empresa;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.ubicacion.Coordenada;
import ar.nex.entity.empleado.Persona;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        if (direccion.getEmpresaList() == null) {
            direccion.setEmpresaList(new ArrayList<Empresa>());
        }
        if (direccion.getCoordenadaList() == null) {
            direccion.setCoordenadaList(new ArrayList<Coordenada>());
        }
        if (direccion.getPersonaList() == null) {
            direccion.setPersonaList(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidad = direccion.getLocalidad();
            if (localidad != null) {
                localidad = em.getReference(localidad.getClass(), localidad.getIdLocalidad());
                direccion.setLocalidad(localidad);
            }
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : direccion.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getIdEmpresa());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            direccion.setEmpresaList(attachedEmpresaList);
            List<Coordenada> attachedCoordenadaList = new ArrayList<Coordenada>();
            for (Coordenada coordenadaListCoordenadaToAttach : direccion.getCoordenadaList()) {
                coordenadaListCoordenadaToAttach = em.getReference(coordenadaListCoordenadaToAttach.getClass(), coordenadaListCoordenadaToAttach.getIdCoordenada());
                attachedCoordenadaList.add(coordenadaListCoordenadaToAttach);
            }
            direccion.setCoordenadaList(attachedCoordenadaList);
            List<Persona> attachedPersonaList = new ArrayList<Persona>();
            for (Persona personaListPersonaToAttach : direccion.getPersonaList()) {
                personaListPersonaToAttach = em.getReference(personaListPersonaToAttach.getClass(), personaListPersonaToAttach.getIdPersona());
                attachedPersonaList.add(personaListPersonaToAttach);
            }
            direccion.setPersonaList(attachedPersonaList);
            em.persist(direccion);
            if (localidad != null) {
                localidad.getDireccionList().add(direccion);
                localidad = em.merge(localidad);
            }
            for (Empresa empresaListEmpresa : direccion.getEmpresaList()) {
                empresaListEmpresa.getDireccionList().add(direccion);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            for (Coordenada coordenadaListCoordenada : direccion.getCoordenadaList()) {
                Direccion oldDireccionOfCoordenadaListCoordenada = coordenadaListCoordenada.getDireccion();
                coordenadaListCoordenada.setDireccion(direccion);
                coordenadaListCoordenada = em.merge(coordenadaListCoordenada);
                if (oldDireccionOfCoordenadaListCoordenada != null) {
                    oldDireccionOfCoordenadaListCoordenada.getCoordenadaList().remove(coordenadaListCoordenada);
                    oldDireccionOfCoordenadaListCoordenada = em.merge(oldDireccionOfCoordenadaListCoordenada);
                }
            }
            for (Persona personaListPersona : direccion.getPersonaList()) {
                Direccion oldDomicilioOfPersonaListPersona = personaListPersona.getDomicilio();
                personaListPersona.setDomicilio(direccion);
                personaListPersona = em.merge(personaListPersona);
                if (oldDomicilioOfPersonaListPersona != null) {
                    oldDomicilioOfPersonaListPersona.getPersonaList().remove(personaListPersona);
                    oldDomicilioOfPersonaListPersona = em.merge(oldDomicilioOfPersonaListPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getIdDireccion());
            Localidad localidadOld = persistentDireccion.getLocalidad();
            Localidad localidadNew = direccion.getLocalidad();
            List<Empresa> empresaListOld = persistentDireccion.getEmpresaList();
            List<Empresa> empresaListNew = direccion.getEmpresaList();
            List<Coordenada> coordenadaListOld = persistentDireccion.getCoordenadaList();
            List<Coordenada> coordenadaListNew = direccion.getCoordenadaList();
            List<Persona> personaListOld = persistentDireccion.getPersonaList();
            List<Persona> personaListNew = direccion.getPersonaList();
            if (localidadNew != null) {
                localidadNew = em.getReference(localidadNew.getClass(), localidadNew.getIdLocalidad());
                direccion.setLocalidad(localidadNew);
            }
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            direccion.setEmpresaList(empresaListNew);
            List<Coordenada> attachedCoordenadaListNew = new ArrayList<Coordenada>();
            for (Coordenada coordenadaListNewCoordenadaToAttach : coordenadaListNew) {
                coordenadaListNewCoordenadaToAttach = em.getReference(coordenadaListNewCoordenadaToAttach.getClass(), coordenadaListNewCoordenadaToAttach.getIdCoordenada());
                attachedCoordenadaListNew.add(coordenadaListNewCoordenadaToAttach);
            }
            coordenadaListNew = attachedCoordenadaListNew;
            direccion.setCoordenadaList(coordenadaListNew);
            List<Persona> attachedPersonaListNew = new ArrayList<Persona>();
            for (Persona personaListNewPersonaToAttach : personaListNew) {
                personaListNewPersonaToAttach = em.getReference(personaListNewPersonaToAttach.getClass(), personaListNewPersonaToAttach.getIdPersona());
                attachedPersonaListNew.add(personaListNewPersonaToAttach);
            }
            personaListNew = attachedPersonaListNew;
            direccion.setPersonaList(personaListNew);
            direccion = em.merge(direccion);
            if (localidadOld != null && !localidadOld.equals(localidadNew)) {
                localidadOld.getDireccionList().remove(direccion);
                localidadOld = em.merge(localidadOld);
            }
            if (localidadNew != null && !localidadNew.equals(localidadOld)) {
                localidadNew.getDireccionList().add(direccion);
                localidadNew = em.merge(localidadNew);
            }
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    empresaListOldEmpresa.getDireccionList().remove(direccion);
                    empresaListOldEmpresa = em.merge(empresaListOldEmpresa);
                }
            }
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    empresaListNewEmpresa.getDireccionList().add(direccion);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                }
            }
            for (Coordenada coordenadaListOldCoordenada : coordenadaListOld) {
                if (!coordenadaListNew.contains(coordenadaListOldCoordenada)) {
                    coordenadaListOldCoordenada.setDireccion(null);
                    coordenadaListOldCoordenada = em.merge(coordenadaListOldCoordenada);
                }
            }
            for (Coordenada coordenadaListNewCoordenada : coordenadaListNew) {
                if (!coordenadaListOld.contains(coordenadaListNewCoordenada)) {
                    Direccion oldDireccionOfCoordenadaListNewCoordenada = coordenadaListNewCoordenada.getDireccion();
                    coordenadaListNewCoordenada.setDireccion(direccion);
                    coordenadaListNewCoordenada = em.merge(coordenadaListNewCoordenada);
                    if (oldDireccionOfCoordenadaListNewCoordenada != null && !oldDireccionOfCoordenadaListNewCoordenada.equals(direccion)) {
                        oldDireccionOfCoordenadaListNewCoordenada.getCoordenadaList().remove(coordenadaListNewCoordenada);
                        oldDireccionOfCoordenadaListNewCoordenada = em.merge(oldDireccionOfCoordenadaListNewCoordenada);
                    }
                }
            }
            for (Persona personaListOldPersona : personaListOld) {
                if (!personaListNew.contains(personaListOldPersona)) {
                    personaListOldPersona.setDomicilio(null);
                    personaListOldPersona = em.merge(personaListOldPersona);
                }
            }
            for (Persona personaListNewPersona : personaListNew) {
                if (!personaListOld.contains(personaListNewPersona)) {
                    Direccion oldDomicilioOfPersonaListNewPersona = personaListNewPersona.getDomicilio();
                    personaListNewPersona.setDomicilio(direccion);
                    personaListNewPersona = em.merge(personaListNewPersona);
                    if (oldDomicilioOfPersonaListNewPersona != null && !oldDomicilioOfPersonaListNewPersona.equals(direccion)) {
                        oldDomicilioOfPersonaListNewPersona.getPersonaList().remove(personaListNewPersona);
                        oldDomicilioOfPersonaListNewPersona = em.merge(oldDomicilioOfPersonaListNewPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = direccion.getIdDireccion();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
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
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getIdDireccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            Localidad localidad = direccion.getLocalidad();
            if (localidad != null) {
                localidad.getDireccionList().remove(direccion);
                localidad = em.merge(localidad);
            }
            List<Empresa> empresaList = direccion.getEmpresaList();
            for (Empresa empresaListEmpresa : empresaList) {
                empresaListEmpresa.getDireccionList().remove(direccion);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            List<Coordenada> coordenadaList = direccion.getCoordenadaList();
            for (Coordenada coordenadaListCoordenada : coordenadaList) {
                coordenadaListCoordenada.setDireccion(null);
                coordenadaListCoordenada = em.merge(coordenadaListCoordenada);
            }
            List<Persona> personaList = direccion.getPersonaList();
            for (Persona personaListPersona : personaList) {
                personaListPersona.setDomicilio(null);
                personaListPersona = em.merge(personaListPersona);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
