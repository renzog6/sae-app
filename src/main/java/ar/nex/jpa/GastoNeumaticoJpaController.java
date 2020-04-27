/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.nex.jpa;

import ar.nex.entity.equipo.gasto.GastoNeumatico;
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
public class GastoNeumaticoJpaController implements Serializable {

    public GastoNeumaticoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GastoNeumatico gastoNeumatico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gastoNeumatico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GastoNeumatico gastoNeumatico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gastoNeumatico = em.merge(gastoNeumatico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gastoNeumatico.getIdGasto();
                if (findGastoNeumatico(id) == null) {
                    throw new NonexistentEntityException("The gastoNeumatico with id " + id + " no longer exists.");
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
            GastoNeumatico gastoNeumatico;
            try {
                gastoNeumatico = em.getReference(GastoNeumatico.class, id);
                gastoNeumatico.getIdGasto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gastoNeumatico with id " + id + " no longer exists.", enfe);
            }
            em.remove(gastoNeumatico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GastoNeumatico> findGastoNeumaticoEntities() {
        return findGastoNeumaticoEntities(true, -1, -1);
    }

    public List<GastoNeumatico> findGastoNeumaticoEntities(int maxResults, int firstResult) {
        return findGastoNeumaticoEntities(false, maxResults, firstResult);
    }

    private List<GastoNeumatico> findGastoNeumaticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GastoNeumatico.class));
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

    public GastoNeumatico findGastoNeumatico(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GastoNeumatico.class, id);
        } finally {
            em.close();
        }
    }

    public int getGastoNeumaticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GastoNeumatico> rt = cq.from(GastoNeumatico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
