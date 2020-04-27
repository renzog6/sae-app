/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.ubicacion.Coordenada;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.ubicacion.Direccion;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class CoordenadaJpaController implements Serializable {

    public CoordenadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Coordenada coordenada) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion = coordenada.getDireccion();
            if (direccion != null) {
                direccion = em.getReference(direccion.getClass(), direccion.getIdDireccion());
                coordenada.setDireccion(direccion);
            }
            em.persist(coordenada);
            if (direccion != null) {
                direccion.getCoordenadaList().add(coordenada);
                direccion = em.merge(direccion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCoordenada(coordenada.getIdCoordenada()) != null) {
                throw new PreexistingEntityException("Coordenada " + coordenada + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Coordenada coordenada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Coordenada persistentCoordenada = em.find(Coordenada.class, coordenada.getIdCoordenada());
            Direccion direccionOld = persistentCoordenada.getDireccion();
            Direccion direccionNew = coordenada.getDireccion();
            if (direccionNew != null) {
                direccionNew = em.getReference(direccionNew.getClass(), direccionNew.getIdDireccion());
                coordenada.setDireccion(direccionNew);
            }
            coordenada = em.merge(coordenada);
            if (direccionOld != null && !direccionOld.equals(direccionNew)) {
                direccionOld.getCoordenadaList().remove(coordenada);
                direccionOld = em.merge(direccionOld);
            }
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                direccionNew.getCoordenadaList().add(coordenada);
                direccionNew = em.merge(direccionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = coordenada.getIdCoordenada();
                if (findCoordenada(id) == null) {
                    throw new NonexistentEntityException("The coordenada with id " + id + " no longer exists.");
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
            Coordenada coordenada;
            try {
                coordenada = em.getReference(Coordenada.class, id);
                coordenada.getIdCoordenada();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The coordenada with id " + id + " no longer exists.", enfe);
            }
            Direccion direccion = coordenada.getDireccion();
            if (direccion != null) {
                direccion.getCoordenadaList().remove(coordenada);
                direccion = em.merge(direccion);
            }
            em.remove(coordenada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Coordenada> findCoordenadaEntities() {
        return findCoordenadaEntities(true, -1, -1);
    }

    public List<Coordenada> findCoordenadaEntities(int maxResults, int firstResult) {
        return findCoordenadaEntities(false, maxResults, firstResult);
    }

    private List<Coordenada> findCoordenadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Coordenada.class));
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

    public Coordenada findCoordenada(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Coordenada.class, id);
        } finally {
            em.close();
        }
    }

    public int getCoordenadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Coordenada> rt = cq.from(Coordenada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
