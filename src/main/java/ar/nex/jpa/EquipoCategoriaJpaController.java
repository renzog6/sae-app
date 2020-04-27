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
import ar.nex.entity.equipo.EquipoCategoria;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoCategoriaJpaController implements Serializable {

    public EquipoCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoCategoria equipoCategoria) {
        if (equipoCategoria.getEquipoList() == null) {
            equipoCategoria.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoCategoria.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoCategoria.setEquipoList(attachedEquipoList);
            em.persist(equipoCategoria);
            for (Equipo equipoListEquipo : equipoCategoria.getEquipoList()) {
                EquipoCategoria oldCategoriaOfEquipoListEquipo = equipoListEquipo.getCategoria();
                equipoListEquipo.setCategoria(equipoCategoria);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldCategoriaOfEquipoListEquipo != null) {
                    oldCategoriaOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldCategoriaOfEquipoListEquipo = em.merge(oldCategoriaOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoCategoria equipoCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCategoria persistentEquipoCategoria = em.find(EquipoCategoria.class, equipoCategoria.getIdCategoria());
            List<Equipo> equipoListOld = persistentEquipoCategoria.getEquipoList();
            List<Equipo> equipoListNew = equipoCategoria.getEquipoList();
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoCategoria.setEquipoList(equipoListNew);
            equipoCategoria = em.merge(equipoCategoria);
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setCategoria(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoCategoria oldCategoriaOfEquipoListNewEquipo = equipoListNewEquipo.getCategoria();
                    equipoListNewEquipo.setCategoria(equipoCategoria);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldCategoriaOfEquipoListNewEquipo != null && !oldCategoriaOfEquipoListNewEquipo.equals(equipoCategoria)) {
                        oldCategoriaOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldCategoriaOfEquipoListNewEquipo = em.merge(oldCategoriaOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoCategoria.getIdCategoria();
                if (findEquipoCategoria(id) == null) {
                    throw new NonexistentEntityException("The equipoCategoria with id " + id + " no longer exists.");
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
            EquipoCategoria equipoCategoria;
            try {
                equipoCategoria = em.getReference(EquipoCategoria.class, id);
                equipoCategoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoCategoria with id " + id + " no longer exists.", enfe);
            }
            List<Equipo> equipoList = equipoCategoria.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setCategoria(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            em.remove(equipoCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoCategoria> findEquipoCategoriaEntities() {
        return findEquipoCategoriaEntities(true, -1, -1);
    }

    public List<EquipoCategoria> findEquipoCategoriaEntities(int maxResults, int firstResult) {
        return findEquipoCategoriaEntities(false, maxResults, firstResult);
    }

    private List<EquipoCategoria> findEquipoCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoCategoria.class));
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

    public EquipoCategoria findEquipoCategoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoCategoria> rt = cq.from(EquipoCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
