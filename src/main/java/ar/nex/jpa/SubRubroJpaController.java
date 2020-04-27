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
import ar.nex.entity.empresa.Rubro;
import ar.nex.entity.empresa.SubRubro;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class SubRubroJpaController implements Serializable {

    public SubRubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubRubro subRubro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro rubro = subRubro.getRubro();
            if (rubro != null) {
                rubro = em.getReference(rubro.getClass(), rubro.getIdRubro());
                subRubro.setRubro(rubro);
            }
            em.persist(subRubro);
            if (rubro != null) {
                rubro.getSubRubroList().add(subRubro);
                rubro = em.merge(rubro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubRubro subRubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubRubro persistentSubRubro = em.find(SubRubro.class, subRubro.getIdSub());
            Rubro rubroOld = persistentSubRubro.getRubro();
            Rubro rubroNew = subRubro.getRubro();
            if (rubroNew != null) {
                rubroNew = em.getReference(rubroNew.getClass(), rubroNew.getIdRubro());
                subRubro.setRubro(rubroNew);
            }
            subRubro = em.merge(subRubro);
            if (rubroOld != null && !rubroOld.equals(rubroNew)) {
                rubroOld.getSubRubroList().remove(subRubro);
                rubroOld = em.merge(rubroOld);
            }
            if (rubroNew != null && !rubroNew.equals(rubroOld)) {
                rubroNew.getSubRubroList().add(subRubro);
                rubroNew = em.merge(rubroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = subRubro.getIdSub();
                if (findSubRubro(id) == null) {
                    throw new NonexistentEntityException("The subRubro with id " + id + " no longer exists.");
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
            SubRubro subRubro;
            try {
                subRubro = em.getReference(SubRubro.class, id);
                subRubro.getIdSub();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subRubro with id " + id + " no longer exists.", enfe);
            }
            Rubro rubro = subRubro.getRubro();
            if (rubro != null) {
                rubro.getSubRubroList().remove(subRubro);
                rubro = em.merge(rubro);
            }
            em.remove(subRubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubRubro> findSubRubroEntities() {
        return findSubRubroEntities(true, -1, -1);
    }

    public List<SubRubro> findSubRubroEntities(int maxResults, int firstResult) {
        return findSubRubroEntities(false, maxResults, firstResult);
    }

    private List<SubRubro> findSubRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubRubro.class));
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

    public SubRubro findSubRubro(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubRubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubRubro> rt = cq.from(SubRubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
