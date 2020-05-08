/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.sincronizar.Actividad;
import ar.nex.sincronizar.Sincronizar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class SincronizarJpaController implements Serializable {

    public SincronizarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sincronizar sincronizar) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad = sincronizar.getActividad();
            if (actividad != null) {
                actividad = em.getReference(actividad.getClass(), actividad.getUuid());
                sincronizar.setActividad(actividad);
            }
            em.persist(sincronizar);
            if (actividad != null) {
                actividad.getSincronizarList().add(sincronizar);
                actividad = em.merge(actividad);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSincronizar(sincronizar.getUuid()) != null) {
                throw new PreexistingEntityException("Sincronizar " + sincronizar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sincronizar sincronizar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sincronizar persistentSincronizar = em.find(Sincronizar.class, sincronizar.getUuid());
            Actividad actividadOld = persistentSincronizar.getActividad();
            Actividad actividadNew = sincronizar.getActividad();
            if (actividadNew != null) {
                actividadNew = em.getReference(actividadNew.getClass(), actividadNew.getUuid());
                sincronizar.setActividad(actividadNew);
            }
            sincronizar = em.merge(sincronizar);
            if (actividadOld != null && !actividadOld.equals(actividadNew)) {
                actividadOld.getSincronizarList().remove(sincronizar);
                actividadOld = em.merge(actividadOld);
            }
            if (actividadNew != null && !actividadNew.equals(actividadOld)) {
                actividadNew.getSincronizarList().add(sincronizar);
                actividadNew = em.merge(actividadNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sincronizar.getUuid();
                if (findSincronizar(id) == null) {
                    throw new NonexistentEntityException("The sincronizar with id " + id + " no longer exists.");
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
            Sincronizar sincronizar;
            try {
                sincronizar = em.getReference(Sincronizar.class, id);
                sincronizar.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sincronizar with id " + id + " no longer exists.", enfe);
            }
            Actividad actividad = sincronizar.getActividad();
            if (actividad != null) {
                actividad.getSincronizarList().remove(sincronizar);
                actividad = em.merge(actividad);
            }
            em.remove(sincronizar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sincronizar> findSincronizarEntities() {
        return findSincronizarEntities(true, -1, -1);
    }

    public List<Sincronizar> findSincronizarEntities(int maxResults, int firstResult) {
        return findSincronizarEntities(false, maxResults, firstResult);
    }

    private List<Sincronizar> findSincronizarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sincronizar.class));
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

    public Sincronizar findSincronizar(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sincronizar.class, id);
        } finally {
            em.close();
        }
    }

    public int getSincronizarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sincronizar> rt = cq.from(Sincronizar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
