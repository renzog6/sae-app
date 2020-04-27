/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.equipo.gasto.GastoRepuesto;
import ar.nex.jpa.exceptions.NonexistentEntityException;
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
public class GastoRepuestoJpaController implements Serializable {

    public GastoRepuestoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GastoRepuesto gastoRepuesto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gastoRepuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GastoRepuesto gastoRepuesto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gastoRepuesto = em.merge(gastoRepuesto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gastoRepuesto.getIdGasto();
                if (findGastoRepuesto(id) == null) {
                    throw new NonexistentEntityException("The gastoRepuesto with id " + id + " no longer exists.");
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
            GastoRepuesto gastoRepuesto;
            try {
                gastoRepuesto = em.getReference(GastoRepuesto.class, id);
                gastoRepuesto.getIdGasto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gastoRepuesto with id " + id + " no longer exists.", enfe);
            }
            em.remove(gastoRepuesto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GastoRepuesto> findGastoRepuestoEntities() {
        return findGastoRepuestoEntities(true, -1, -1);
    }

    public List<GastoRepuesto> findGastoRepuestoEntities(int maxResults, int firstResult) {
        return findGastoRepuestoEntities(false, maxResults, firstResult);
    }

    private List<GastoRepuesto> findGastoRepuestoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GastoRepuesto.class));
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

    public GastoRepuesto findGastoRepuesto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GastoRepuesto.class, id);
        } finally {
            em.close();
        }
    }

    public int getGastoRepuestoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GastoRepuesto> rt = cq.from(GastoRepuesto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
