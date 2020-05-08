/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.sincronizar.Dispositivo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class DispositivoJpaController implements Serializable {

    public DispositivoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dispositivo dispositivo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dispositivo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDispositivo(dispositivo.getUuid()) != null) {
                throw new PreexistingEntityException("Dispositivo " + dispositivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dispositivo dispositivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dispositivo = em.merge(dispositivo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = dispositivo.getUuid();
                if (findDispositivo(id) == null) {
                    throw new NonexistentEntityException("The dispositivo with id " + id + " no longer exists.");
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
            Dispositivo dispositivo;
            try {
                dispositivo = em.getReference(Dispositivo.class, id);
                dispositivo.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dispositivo with id " + id + " no longer exists.", enfe);
            }
            em.remove(dispositivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dispositivo> findDispositivoEntities() {
        return findDispositivoEntities(true, -1, -1);
    }

    public List<Dispositivo> findDispositivoEntities(int maxResults, int firstResult) {
        return findDispositivoEntities(false, maxResults, firstResult);
    }

    private List<Dispositivo> findDispositivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dispositivo.class));
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

    public Dispositivo findDispositivo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dispositivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getDispositivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dispositivo> rt = cq.from(Dispositivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
