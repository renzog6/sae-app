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
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.Repuesto;
import ar.nex.entity.equipo.RepuestoStockDetalle;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class RepuestoStockDetalleJpaController implements Serializable {

    public RepuestoStockDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RepuestoStockDetalle repuestoStockDetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = repuestoStockDetalle.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getIdEquipo());
                repuestoStockDetalle.setEquipo(equipo);
            }
            Repuesto repuesto = repuestoStockDetalle.getRepuesto();
            if (repuesto != null) {
                repuesto = em.getReference(repuesto.getClass(), repuesto.getIdRepuesto());
                repuestoStockDetalle.setRepuesto(repuesto);
            }
            em.persist(repuestoStockDetalle);
            if (equipo != null) {
                equipo.getRepuestoStockDetalleList().add(repuestoStockDetalle);
                equipo = em.merge(equipo);
            }
            if (repuesto != null) {
                repuesto.getRepuestoStockDetalleList().add(repuestoStockDetalle);
                repuesto = em.merge(repuesto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RepuestoStockDetalle repuestoStockDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RepuestoStockDetalle persistentRepuestoStockDetalle = em.find(RepuestoStockDetalle.class, repuestoStockDetalle.getIdStock());
            Equipo equipoOld = persistentRepuestoStockDetalle.getEquipo();
            Equipo equipoNew = repuestoStockDetalle.getEquipo();
            Repuesto repuestoOld = persistentRepuestoStockDetalle.getRepuesto();
            Repuesto repuestoNew = repuestoStockDetalle.getRepuesto();
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getIdEquipo());
                repuestoStockDetalle.setEquipo(equipoNew);
            }
            if (repuestoNew != null) {
                repuestoNew = em.getReference(repuestoNew.getClass(), repuestoNew.getIdRepuesto());
                repuestoStockDetalle.setRepuesto(repuestoNew);
            }
            repuestoStockDetalle = em.merge(repuestoStockDetalle);
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                equipoOld.getRepuestoStockDetalleList().remove(repuestoStockDetalle);
                equipoOld = em.merge(equipoOld);
            }
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                equipoNew.getRepuestoStockDetalleList().add(repuestoStockDetalle);
                equipoNew = em.merge(equipoNew);
            }
            if (repuestoOld != null && !repuestoOld.equals(repuestoNew)) {
                repuestoOld.getRepuestoStockDetalleList().remove(repuestoStockDetalle);
                repuestoOld = em.merge(repuestoOld);
            }
            if (repuestoNew != null && !repuestoNew.equals(repuestoOld)) {
                repuestoNew.getRepuestoStockDetalleList().add(repuestoStockDetalle);
                repuestoNew = em.merge(repuestoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = repuestoStockDetalle.getIdStock();
                if (findRepuestoStockDetalle(id) == null) {
                    throw new NonexistentEntityException("The repuestoStockDetalle with id " + id + " no longer exists.");
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
            RepuestoStockDetalle repuestoStockDetalle;
            try {
                repuestoStockDetalle = em.getReference(RepuestoStockDetalle.class, id);
                repuestoStockDetalle.getIdStock();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The repuestoStockDetalle with id " + id + " no longer exists.", enfe);
            }
            Equipo equipo = repuestoStockDetalle.getEquipo();
            if (equipo != null) {
                equipo.getRepuestoStockDetalleList().remove(repuestoStockDetalle);
                equipo = em.merge(equipo);
            }
            Repuesto repuesto = repuestoStockDetalle.getRepuesto();
            if (repuesto != null) {
                repuesto.getRepuestoStockDetalleList().remove(repuestoStockDetalle);
                repuesto = em.merge(repuesto);
            }
            em.remove(repuestoStockDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RepuestoStockDetalle> findRepuestoStockDetalleEntities() {
        return findRepuestoStockDetalleEntities(true, -1, -1);
    }

    public List<RepuestoStockDetalle> findRepuestoStockDetalleEntities(int maxResults, int firstResult) {
        return findRepuestoStockDetalleEntities(false, maxResults, firstResult);
    }

    private List<RepuestoStockDetalle> findRepuestoStockDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RepuestoStockDetalle.class));
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

    public RepuestoStockDetalle findRepuestoStockDetalle(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RepuestoStockDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getRepuestoStockDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RepuestoStockDetalle> rt = cq.from(RepuestoStockDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
