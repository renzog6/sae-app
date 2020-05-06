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
        if (actividad.getSincronizarList() == null) {
            actividad.setSincronizarList(new ArrayList<Sincronizar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Sincronizar> attachedSincronizarList = new ArrayList<Sincronizar>();
            for (Sincronizar sincronizarListSincronizarToAttach : actividad.getSincronizarList()) {
                sincronizarListSincronizarToAttach = em.getReference(sincronizarListSincronizarToAttach.getClass(), sincronizarListSincronizarToAttach.getUuid());
                attachedSincronizarList.add(sincronizarListSincronizarToAttach);
            }
            actividad.setSincronizarList(attachedSincronizarList);
            em.persist(actividad);
            for (Sincronizar sincronizarListSincronizar : actividad.getSincronizarList()) {
                Actividad oldActividadOfSincronizarListSincronizar = sincronizarListSincronizar.getActividad();
                sincronizarListSincronizar.setActividad(actividad);
                sincronizarListSincronizar = em.merge(sincronizarListSincronizar);
                if (oldActividadOfSincronizarListSincronizar != null) {
                    oldActividadOfSincronizarListSincronizar.getSincronizarList().remove(sincronizarListSincronizar);
                    oldActividadOfSincronizarListSincronizar = em.merge(oldActividadOfSincronizarListSincronizar);
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
            List<Sincronizar> sincronizarListOld = persistentActividad.getSincronizarList();
            List<Sincronizar> sincronizarListNew = actividad.getSincronizarList();
            List<Sincronizar> attachedSincronizarListNew = new ArrayList<Sincronizar>();
            for (Sincronizar sincronizarListNewSincronizarToAttach : sincronizarListNew) {
                sincronizarListNewSincronizarToAttach = em.getReference(sincronizarListNewSincronizarToAttach.getClass(), sincronizarListNewSincronizarToAttach.getUuid());
                attachedSincronizarListNew.add(sincronizarListNewSincronizarToAttach);
            }
            sincronizarListNew = attachedSincronizarListNew;
            actividad.setSincronizarList(sincronizarListNew);
            actividad = em.merge(actividad);
            for (Sincronizar sincronizarListOldSincronizar : sincronizarListOld) {
                if (!sincronizarListNew.contains(sincronizarListOldSincronizar)) {
                    sincronizarListOldSincronizar.setActividad(null);
                    sincronizarListOldSincronizar = em.merge(sincronizarListOldSincronizar);
                }
            }
            for (Sincronizar sincronizarListNewSincronizar : sincronizarListNew) {
                if (!sincronizarListOld.contains(sincronizarListNewSincronizar)) {
                    Actividad oldActividadOfSincronizarListNewSincronizar = sincronizarListNewSincronizar.getActividad();
                    sincronizarListNewSincronizar.setActividad(actividad);
                    sincronizarListNewSincronizar = em.merge(sincronizarListNewSincronizar);
                    if (oldActividadOfSincronizarListNewSincronizar != null && !oldActividadOfSincronizarListNewSincronizar.equals(actividad)) {
                        oldActividadOfSincronizarListNewSincronizar.getSincronizarList().remove(sincronizarListNewSincronizar);
                        oldActividadOfSincronizarListNewSincronizar = em.merge(oldActividadOfSincronizarListNewSincronizar);
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
            List<Sincronizar> sincronizarList = actividad.getSincronizarList();
            for (Sincronizar sincronizarListSincronizar : sincronizarList) {
                sincronizarListSincronizar.setActividad(null);
                sincronizarListSincronizar = em.merge(sincronizarListSincronizar);
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
