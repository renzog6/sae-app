package ar.nex.jpa;

import ar.nex.entity.equipo.gasto.Gasoil;
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
public class GasoilJpaController implements Serializable {

    public GasoilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gasoil gasoil) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gasoil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gasoil gasoil) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gasoil = em.merge(gasoil);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gasoil.getIdGasto();
                if (findGasoil(id) == null) {
                    throw new NonexistentEntityException("The gasoil with id " + id + " no longer exists.");
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
            Gasoil gasoil;
            try {
                gasoil = em.getReference(Gasoil.class, id);
                gasoil.getIdGasto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gasoil with id " + id + " no longer exists.", enfe);
            }
            em.remove(gasoil);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gasoil> findGasoilEntities() {
        return findGasoilEntities(true, -1, -1);
    }

    public List<Gasoil> findGasoilEntities(int maxResults, int firstResult) {
        return findGasoilEntities(false, maxResults, firstResult);
    }

    private List<Gasoil> findGasoilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gasoil.class));
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

    public Gasoil findGasoil(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gasoil.class, id);
        } finally {
            em.close();
        }
    }

    public int getGasoilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gasoil> rt = cq.from(Gasoil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
