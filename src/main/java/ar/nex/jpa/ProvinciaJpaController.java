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
import ar.nex.entity.ubicacion.Provincia;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ProvinciaJpaController implements Serializable {

    public ProvinciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provincia provincia) {
        if (provincia.getLocalidadList() == null) {
            provincia.setLocalidadList(new ArrayList<Localidad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Localidad> attachedLocalidadList = new ArrayList<Localidad>();
            for (Localidad localidadListLocalidadToAttach : provincia.getLocalidadList()) {
                localidadListLocalidadToAttach = em.getReference(localidadListLocalidadToAttach.getClass(), localidadListLocalidadToAttach.getIdLocalidad());
                attachedLocalidadList.add(localidadListLocalidadToAttach);
            }
            provincia.setLocalidadList(attachedLocalidadList);
            em.persist(provincia);
            for (Localidad localidadListLocalidad : provincia.getLocalidadList()) {
                Provincia oldProvinciaOfLocalidadListLocalidad = localidadListLocalidad.getProvincia();
                localidadListLocalidad.setProvincia(provincia);
                localidadListLocalidad = em.merge(localidadListLocalidad);
                if (oldProvinciaOfLocalidadListLocalidad != null) {
                    oldProvinciaOfLocalidadListLocalidad.getLocalidadList().remove(localidadListLocalidad);
                    oldProvinciaOfLocalidadListLocalidad = em.merge(oldProvinciaOfLocalidadListLocalidad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provincia provincia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia persistentProvincia = em.find(Provincia.class, provincia.getIdProvincia());
            List<Localidad> localidadListOld = persistentProvincia.getLocalidadList();
            List<Localidad> localidadListNew = provincia.getLocalidadList();
            List<Localidad> attachedLocalidadListNew = new ArrayList<Localidad>();
            for (Localidad localidadListNewLocalidadToAttach : localidadListNew) {
                localidadListNewLocalidadToAttach = em.getReference(localidadListNewLocalidadToAttach.getClass(), localidadListNewLocalidadToAttach.getIdLocalidad());
                attachedLocalidadListNew.add(localidadListNewLocalidadToAttach);
            }
            localidadListNew = attachedLocalidadListNew;
            provincia.setLocalidadList(localidadListNew);
            provincia = em.merge(provincia);
            for (Localidad localidadListOldLocalidad : localidadListOld) {
                if (!localidadListNew.contains(localidadListOldLocalidad)) {
                    localidadListOldLocalidad.setProvincia(null);
                    localidadListOldLocalidad = em.merge(localidadListOldLocalidad);
                }
            }
            for (Localidad localidadListNewLocalidad : localidadListNew) {
                if (!localidadListOld.contains(localidadListNewLocalidad)) {
                    Provincia oldProvinciaOfLocalidadListNewLocalidad = localidadListNewLocalidad.getProvincia();
                    localidadListNewLocalidad.setProvincia(provincia);
                    localidadListNewLocalidad = em.merge(localidadListNewLocalidad);
                    if (oldProvinciaOfLocalidadListNewLocalidad != null && !oldProvinciaOfLocalidadListNewLocalidad.equals(provincia)) {
                        oldProvinciaOfLocalidadListNewLocalidad.getLocalidadList().remove(localidadListNewLocalidad);
                        oldProvinciaOfLocalidadListNewLocalidad = em.merge(oldProvinciaOfLocalidadListNewLocalidad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = provincia.getIdProvincia();
                if (findProvincia(id) == null) {
                    throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.");
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
            Provincia provincia;
            try {
                provincia = em.getReference(Provincia.class, id);
                provincia.getIdProvincia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.", enfe);
            }
            List<Localidad> localidadList = provincia.getLocalidadList();
            for (Localidad localidadListLocalidad : localidadList) {
                localidadListLocalidad.setProvincia(null);
                localidadListLocalidad = em.merge(localidadListLocalidad);
            }
            em.remove(provincia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provincia> findProvinciaEntities() {
        return findProvinciaEntities(true, -1, -1);
    }

    public List<Provincia> findProvinciaEntities(int maxResults, int firstResult) {
        return findProvinciaEntities(false, maxResults, firstResult);
    }

    private List<Provincia> findProvinciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provincia.class));
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

    public Provincia findProvincia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provincia.class, id);
        } finally {
            em.close();
        }
    }

    public int getProvinciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provincia> rt = cq.from(Provincia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
