/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.AdminEmpresa;
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
public class AdminEmpresaJpaController implements Serializable {

    public AdminEmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdminEmpresa adminEmpresa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(adminEmpresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdminEmpresa adminEmpresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            adminEmpresa = em.merge(adminEmpresa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = adminEmpresa.getIdAdmin();
                if (findAdminEmpresa(id) == null) {
                    throw new NonexistentEntityException("The adminEmpresa with id " + id + " no longer exists.");
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
            AdminEmpresa adminEmpresa;
            try {
                adminEmpresa = em.getReference(AdminEmpresa.class, id);
                adminEmpresa.getIdAdmin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adminEmpresa with id " + id + " no longer exists.", enfe);
            }
            em.remove(adminEmpresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdminEmpresa> findAdminEmpresaEntities() {
        return findAdminEmpresaEntities(true, -1, -1);
    }

    public List<AdminEmpresa> findAdminEmpresaEntities(int maxResults, int firstResult) {
        return findAdminEmpresaEntities(false, maxResults, firstResult);
    }

    private List<AdminEmpresa> findAdminEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdminEmpresa.class));
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

    public AdminEmpresa findAdminEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdminEmpresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdminEmpresa> rt = cq.from(AdminEmpresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
