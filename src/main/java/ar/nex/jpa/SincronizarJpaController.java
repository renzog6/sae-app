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
            Actividad acitvidad = sincronizar.getAcitvidad();
            if (acitvidad != null) {
                acitvidad = em.getReference(acitvidad.getClass(), acitvidad.getUuid());
                sincronizar.setAcitvidad(acitvidad);
            }
            em.persist(sincronizar);
            if (acitvidad != null) {
                acitvidad.getSincronizarCollection().add(sincronizar);
                acitvidad = em.merge(acitvidad);
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
            Actividad acitvidadOld = persistentSincronizar.getAcitvidad();
            Actividad acitvidadNew = sincronizar.getAcitvidad();
            if (acitvidadNew != null) {
                acitvidadNew = em.getReference(acitvidadNew.getClass(), acitvidadNew.getUuid());
                sincronizar.setAcitvidad(acitvidadNew);
            }
            sincronizar = em.merge(sincronizar);
            if (acitvidadOld != null && !acitvidadOld.equals(acitvidadNew)) {
                acitvidadOld.getSincronizarCollection().remove(sincronizar);
                acitvidadOld = em.merge(acitvidadOld);
            }
            if (acitvidadNew != null && !acitvidadNew.equals(acitvidadOld)) {
                acitvidadNew.getSincronizarCollection().add(sincronizar);
                acitvidadNew = em.merge(acitvidadNew);
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
            Actividad acitvidad = sincronizar.getAcitvidad();
            if (acitvidad != null) {
                acitvidad.getSincronizarCollection().remove(sincronizar);
                acitvidad = em.merge(acitvidad);
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
