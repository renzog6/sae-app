/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.sincronizar.Actividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.sincronizar.Sincronizar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) throws PreexistingEntityException, Exception {
        if (actividad.getSincronizarCollection() == null) {
            actividad.setSincronizarCollection(new ArrayList<Sincronizar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Sincronizar> attachedSincronizarCollection = new ArrayList<Sincronizar>();
            for (Sincronizar sincronizarCollectionSincronizarToAttach : actividad.getSincronizarCollection()) {
                sincronizarCollectionSincronizarToAttach = em.getReference(sincronizarCollectionSincronizarToAttach.getClass(), sincronizarCollectionSincronizarToAttach.getUuid());
                attachedSincronizarCollection.add(sincronizarCollectionSincronizarToAttach);
            }
            actividad.setSincronizarCollection(attachedSincronizarCollection);
            em.persist(actividad);
            for (Sincronizar sincronizarCollectionSincronizar : actividad.getSincronizarCollection()) {
                Actividad oldAcitvidadOfSincronizarCollectionSincronizar = sincronizarCollectionSincronizar.getAcitvidad();
                sincronizarCollectionSincronizar.setAcitvidad(actividad);
                sincronizarCollectionSincronizar = em.merge(sincronizarCollectionSincronizar);
                if (oldAcitvidadOfSincronizarCollectionSincronizar != null) {
                    oldAcitvidadOfSincronizarCollectionSincronizar.getSincronizarCollection().remove(sincronizarCollectionSincronizar);
                    oldAcitvidadOfSincronizarCollectionSincronizar = em.merge(oldAcitvidadOfSincronizarCollectionSincronizar);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActividad(actividad.getUuid()) != null) {
                throw new PreexistingEntityException("Actividad " + actividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getUuid());
            Collection<Sincronizar> sincronizarCollectionOld = persistentActividad.getSincronizarCollection();
            Collection<Sincronizar> sincronizarCollectionNew = actividad.getSincronizarCollection();
            Collection<Sincronizar> attachedSincronizarCollectionNew = new ArrayList<Sincronizar>();
            for (Sincronizar sincronizarCollectionNewSincronizarToAttach : sincronizarCollectionNew) {
                sincronizarCollectionNewSincronizarToAttach = em.getReference(sincronizarCollectionNewSincronizarToAttach.getClass(), sincronizarCollectionNewSincronizarToAttach.getUuid());
                attachedSincronizarCollectionNew.add(sincronizarCollectionNewSincronizarToAttach);
            }
            sincronizarCollectionNew = attachedSincronizarCollectionNew;
            actividad.setSincronizarCollection(sincronizarCollectionNew);
            actividad = em.merge(actividad);
            for (Sincronizar sincronizarCollectionOldSincronizar : sincronizarCollectionOld) {
                if (!sincronizarCollectionNew.contains(sincronizarCollectionOldSincronizar)) {
                    sincronizarCollectionOldSincronizar.setAcitvidad(null);
                    sincronizarCollectionOldSincronizar = em.merge(sincronizarCollectionOldSincronizar);
                }
            }
            for (Sincronizar sincronizarCollectionNewSincronizar : sincronizarCollectionNew) {
                if (!sincronizarCollectionOld.contains(sincronizarCollectionNewSincronizar)) {
                    Actividad oldAcitvidadOfSincronizarCollectionNewSincronizar = sincronizarCollectionNewSincronizar.getAcitvidad();
                    sincronizarCollectionNewSincronizar.setAcitvidad(actividad);
                    sincronizarCollectionNewSincronizar = em.merge(sincronizarCollectionNewSincronizar);
                    if (oldAcitvidadOfSincronizarCollectionNewSincronizar != null && !oldAcitvidadOfSincronizarCollectionNewSincronizar.equals(actividad)) {
                        oldAcitvidadOfSincronizarCollectionNewSincronizar.getSincronizarCollection().remove(sincronizarCollectionNewSincronizar);
                        oldAcitvidadOfSincronizarCollectionNewSincronizar = em.merge(oldAcitvidadOfSincronizarCollectionNewSincronizar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = actividad.getUuid();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            Collection<Sincronizar> sincronizarCollection = actividad.getSincronizarCollection();
            for (Sincronizar sincronizarCollectionSincronizar : sincronizarCollection) {
                sincronizarCollectionSincronizar.setAcitvidad(null);
                sincronizarCollectionSincronizar = em.merge(sincronizarCollectionSincronizar);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
