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
import ar.nex.entity.equipo.EquipoCategoria;
import ar.nex.entity.equipo.Equipo;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.entity.equipo.EquipoTipo;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoTipoJpaController implements Serializable {

    public EquipoTipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoTipo equipoTipo) {
        if (equipoTipo.getEquipoList() == null) {
            equipoTipo.setEquipoList(new ArrayList<Equipo>());
        }
        if (equipoTipo.getModeloList() == null) {
            equipoTipo.setModeloList(new ArrayList<EquipoModelo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoCategoria categoria = equipoTipo.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getIdCategoria());
                equipoTipo.setCategoria(categoria);
            }
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoTipo.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoTipo.setEquipoList(attachedEquipoList);
            List<EquipoModelo> attachedModeloList = new ArrayList<EquipoModelo>();
            for (EquipoModelo modeloListEquipoModeloToAttach : equipoTipo.getModeloList()) {
                modeloListEquipoModeloToAttach = em.getReference(modeloListEquipoModeloToAttach.getClass(), modeloListEquipoModeloToAttach.getIdModelo());
                attachedModeloList.add(modeloListEquipoModeloToAttach);
            }
            equipoTipo.setModeloList(attachedModeloList);
            em.persist(equipoTipo);
            if (categoria != null) {
                categoria.getEquipoTipoList().add(equipoTipo);
                categoria = em.merge(categoria);
            }
            for (Equipo equipoListEquipo : equipoTipo.getEquipoList()) {
                EquipoTipo oldTipoOfEquipoListEquipo = equipoListEquipo.getTipo();
                equipoListEquipo.setTipo(equipoTipo);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldTipoOfEquipoListEquipo != null) {
                    oldTipoOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldTipoOfEquipoListEquipo = em.merge(oldTipoOfEquipoListEquipo);
                }
            }
            for (EquipoModelo modeloListEquipoModelo : equipoTipo.getModeloList()) {
                EquipoTipo oldTipoOfModeloListEquipoModelo = modeloListEquipoModelo.getTipo();
                modeloListEquipoModelo.setTipo(equipoTipo);
                modeloListEquipoModelo = em.merge(modeloListEquipoModelo);
                if (oldTipoOfModeloListEquipoModelo != null) {
                    oldTipoOfModeloListEquipoModelo.getModeloList().remove(modeloListEquipoModelo);
                    oldTipoOfModeloListEquipoModelo = em.merge(oldTipoOfModeloListEquipoModelo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoTipo equipoTipo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoTipo persistentEquipoTipo = em.find(EquipoTipo.class, equipoTipo.getIdTipo());
            EquipoCategoria categoriaOld = persistentEquipoTipo.getCategoria();
            EquipoCategoria categoriaNew = equipoTipo.getCategoria();
            List<Equipo> equipoListOld = persistentEquipoTipo.getEquipoList();
            List<Equipo> equipoListNew = equipoTipo.getEquipoList();
            List<EquipoModelo> modeloListOld = persistentEquipoTipo.getModeloList();
            List<EquipoModelo> modeloListNew = equipoTipo.getModeloList();
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getIdCategoria());
                equipoTipo.setCategoria(categoriaNew);
            }
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoTipo.setEquipoList(equipoListNew);
            List<EquipoModelo> attachedModeloListNew = new ArrayList<EquipoModelo>();
            for (EquipoModelo modeloListNewEquipoModeloToAttach : modeloListNew) {
                modeloListNewEquipoModeloToAttach = em.getReference(modeloListNewEquipoModeloToAttach.getClass(), modeloListNewEquipoModeloToAttach.getIdModelo());
                attachedModeloListNew.add(modeloListNewEquipoModeloToAttach);
            }
            modeloListNew = attachedModeloListNew;
            equipoTipo.setModeloList(modeloListNew);
            equipoTipo = em.merge(equipoTipo);
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getEquipoTipoList().remove(equipoTipo);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getEquipoTipoList().add(equipoTipo);
                categoriaNew = em.merge(categoriaNew);
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setTipo(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoTipo oldTipoOfEquipoListNewEquipo = equipoListNewEquipo.getTipo();
                    equipoListNewEquipo.setTipo(equipoTipo);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldTipoOfEquipoListNewEquipo != null && !oldTipoOfEquipoListNewEquipo.equals(equipoTipo)) {
                        oldTipoOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldTipoOfEquipoListNewEquipo = em.merge(oldTipoOfEquipoListNewEquipo);
                    }
                }
            }
            for (EquipoModelo modeloListOldEquipoModelo : modeloListOld) {
                if (!modeloListNew.contains(modeloListOldEquipoModelo)) {
                    modeloListOldEquipoModelo.setTipo(null);
                    modeloListOldEquipoModelo = em.merge(modeloListOldEquipoModelo);
                }
            }
            for (EquipoModelo modeloListNewEquipoModelo : modeloListNew) {
                if (!modeloListOld.contains(modeloListNewEquipoModelo)) {
                    EquipoTipo oldTipoOfModeloListNewEquipoModelo = modeloListNewEquipoModelo.getTipo();
                    modeloListNewEquipoModelo.setTipo(equipoTipo);
                    modeloListNewEquipoModelo = em.merge(modeloListNewEquipoModelo);
                    if (oldTipoOfModeloListNewEquipoModelo != null && !oldTipoOfModeloListNewEquipoModelo.equals(equipoTipo)) {
                        oldTipoOfModeloListNewEquipoModelo.getModeloList().remove(modeloListNewEquipoModelo);
                        oldTipoOfModeloListNewEquipoModelo = em.merge(oldTipoOfModeloListNewEquipoModelo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoTipo.getIdTipo();
                if (findEquipoTipo(id) == null) {
                    throw new NonexistentEntityException("The equipoTipo with id " + id + " no longer exists.");
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
            EquipoTipo equipoTipo;
            try {
                equipoTipo = em.getReference(EquipoTipo.class, id);
                equipoTipo.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoTipo with id " + id + " no longer exists.", enfe);
            }
            EquipoCategoria categoria = equipoTipo.getCategoria();
            if (categoria != null) {
                categoria.getEquipoTipoList().remove(equipoTipo);
                categoria = em.merge(categoria);
            }
            List<Equipo> equipoList = equipoTipo.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setTipo(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<EquipoModelo> modeloList = equipoTipo.getModeloList();
            for (EquipoModelo modeloListEquipoModelo : modeloList) {
                modeloListEquipoModelo.setTipo(null);
                modeloListEquipoModelo = em.merge(modeloListEquipoModelo);
            }
            em.remove(equipoTipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoTipo> findEquipoTipoEntities() {
        return findEquipoTipoEntities(true, -1, -1);
    }

    public List<EquipoTipo> findEquipoTipoEntities(int maxResults, int firstResult) {
        return findEquipoTipoEntities(false, maxResults, firstResult);
    }

    private List<EquipoTipo> findEquipoTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoTipo.class));
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

    public EquipoTipo findEquipoTipo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoTipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoTipo> rt = cq.from(EquipoTipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
