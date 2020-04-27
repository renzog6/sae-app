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
import ar.nex.entity.equipo.EquipoDocumentacion;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoDocumentacionJpaController implements Serializable {

    public EquipoDocumentacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoDocumentacion equipoDocumentacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipo = equipoDocumentacion.getEquipo();
            if (equipo != null) {
                equipo = em.getReference(equipo.getClass(), equipo.getIdEquipo());
                equipoDocumentacion.setEquipo(equipo);
            }
            em.persist(equipoDocumentacion);
            if (equipo != null) {
                EquipoDocumentacion oldDocumentacionOfEquipo = equipo.getDocumentacion();
                if (oldDocumentacionOfEquipo != null) {
                    oldDocumentacionOfEquipo.setEquipo(null);
                    oldDocumentacionOfEquipo = em.merge(oldDocumentacionOfEquipo);
                }
                equipo.setDocumentacion(equipoDocumentacion);
                equipo = em.merge(equipo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoDocumentacion equipoDocumentacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoDocumentacion persistentEquipoDocumentacion = em.find(EquipoDocumentacion.class, equipoDocumentacion.getIdDoc());
            Equipo equipoOld = persistentEquipoDocumentacion.getEquipo();
            Equipo equipoNew = equipoDocumentacion.getEquipo();
            if (equipoNew != null) {
                equipoNew = em.getReference(equipoNew.getClass(), equipoNew.getIdEquipo());
                equipoDocumentacion.setEquipo(equipoNew);
            }
            equipoDocumentacion = em.merge(equipoDocumentacion);
            if (equipoOld != null && !equipoOld.equals(equipoNew)) {
                equipoOld.setDocumentacion(null);
                equipoOld = em.merge(equipoOld);
            }
            if (equipoNew != null && !equipoNew.equals(equipoOld)) {
                EquipoDocumentacion oldDocumentacionOfEquipo = equipoNew.getDocumentacion();
                if (oldDocumentacionOfEquipo != null) {
                    oldDocumentacionOfEquipo.setEquipo(null);
                    oldDocumentacionOfEquipo = em.merge(oldDocumentacionOfEquipo);
                }
                equipoNew.setDocumentacion(equipoDocumentacion);
                equipoNew = em.merge(equipoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoDocumentacion.getIdDoc();
                if (findEquipoDocumentacion(id) == null) {
                    throw new NonexistentEntityException("The equipoDocumentacion with id " + id + " no longer exists.");
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
            EquipoDocumentacion equipoDocumentacion;
            try {
                equipoDocumentacion = em.getReference(EquipoDocumentacion.class, id);
                equipoDocumentacion.getIdDoc();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoDocumentacion with id " + id + " no longer exists.", enfe);
            }
            Equipo equipo = equipoDocumentacion.getEquipo();
            if (equipo != null) {
                equipo.setDocumentacion(null);
                equipo = em.merge(equipo);
            }
            em.remove(equipoDocumentacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoDocumentacion> findEquipoDocumentacionEntities() {
        return findEquipoDocumentacionEntities(true, -1, -1);
    }

    public List<EquipoDocumentacion> findEquipoDocumentacionEntities(int maxResults, int firstResult) {
        return findEquipoDocumentacionEntities(false, maxResults, firstResult);
    }

    private List<EquipoDocumentacion> findEquipoDocumentacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoDocumentacion.class));
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

    public EquipoDocumentacion findEquipoDocumentacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoDocumentacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoDocumentacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoDocumentacion> rt = cq.from(EquipoDocumentacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
