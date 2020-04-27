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
import ar.nex.entity.equipo.EquipoCompraVenta;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoCompraVentaJpaController implements Serializable {

    public EquipoCompraVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoCompraVenta equipoCompraVenta) {
        if (equipoCompraVenta.getEquipoList() == null) {
            equipoCompraVenta.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoCompraVenta.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoCompraVenta.setEquipoList(attachedEquipoList);
            em.persist(equipoCompraVenta);
            for (Equipo equipoListEquipo : equipoCompraVenta.getEquipoList()) {
                EquipoCompraVenta oldCompraVentaOfEquipoListEquipo = equipoListEquipo.getCompraVenta();
                equipoListEquipo.setCompraVenta(equipoCompraVenta);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldCompraVentaOfEquipoListEquipo != null) {
                    oldCompraVentaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldCompraVentaOfEquipoListEquipo = em.merge(oldCompraVentaOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoCompraVenta equipoCompraVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCompraVenta persistentEquipoCompraVenta = em.find(EquipoCompraVenta.class, equipoCompraVenta.getIdCompraVenta());
            List<Equipo> equipoListOld = persistentEquipoCompraVenta.getEquipoList();
            List<Equipo> equipoListNew = equipoCompraVenta.getEquipoList();
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoCompraVenta.setEquipoList(equipoListNew);
            equipoCompraVenta = em.merge(equipoCompraVenta);
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setCompraVenta(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoCompraVenta oldCompraVentaOfEquipoListNewEquipo = equipoListNewEquipo.getCompraVenta();
                    equipoListNewEquipo.setCompraVenta(equipoCompraVenta);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldCompraVentaOfEquipoListNewEquipo != null && !oldCompraVentaOfEquipoListNewEquipo.equals(equipoCompraVenta)) {
                        oldCompraVentaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldCompraVentaOfEquipoListNewEquipo = em.merge(oldCompraVentaOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoCompraVenta.getIdCompraVenta();
                if (findEquipoCompraVenta(id) == null) {
                    throw new NonexistentEntityException("The equipoCompraVenta with id " + id + " no longer exists.");
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
            EquipoCompraVenta equipoCompraVenta;
            try {
                equipoCompraVenta = em.getReference(EquipoCompraVenta.class, id);
                equipoCompraVenta.getIdCompraVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoCompraVenta with id " + id + " no longer exists.", enfe);
            }
            List<Equipo> equipoList = equipoCompraVenta.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setCompraVenta(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            em.remove(equipoCompraVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoCompraVenta> findEquipoCompraVentaEntities() {
        return findEquipoCompraVentaEntities(true, -1, -1);
    }

    public List<EquipoCompraVenta> findEquipoCompraVentaEntities(int maxResults, int firstResult) {
        return findEquipoCompraVentaEntities(false, maxResults, firstResult);
    }

    private List<EquipoCompraVenta> findEquipoCompraVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoCompraVenta.class));
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

    public EquipoCompraVenta findEquipoCompraVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoCompraVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCompraVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoCompraVenta> rt = cq.from(EquipoCompraVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
