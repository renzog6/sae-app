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
import ar.nex.entity.empleado.Empleado;
import ar.nex.entity.empleado.EmpleadoCategoria;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EmpleadoCategoriaJpaController implements Serializable {

    public EmpleadoCategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmpleadoCategoria empleadoCategoria) throws PreexistingEntityException, Exception {
        if (empleadoCategoria.getEmpleadoList() == null) {
            empleadoCategoria.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : empleadoCategoria.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdPersona());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            empleadoCategoria.setEmpleadoList(attachedEmpleadoList);
            em.persist(empleadoCategoria);
            for (Empleado empleadoListEmpleado : empleadoCategoria.getEmpleadoList()) {
                EmpleadoCategoria oldCategoriaOfEmpleadoListEmpleado = empleadoListEmpleado.getCategoria();
                empleadoListEmpleado.setCategoria(empleadoCategoria);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldCategoriaOfEmpleadoListEmpleado != null) {
                    oldCategoriaOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldCategoriaOfEmpleadoListEmpleado = em.merge(oldCategoriaOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleadoCategoria(empleadoCategoria.getIdCategoria()) != null) {
                throw new PreexistingEntityException("EmpleadoCategoria " + empleadoCategoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmpleadoCategoria empleadoCategoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpleadoCategoria persistentEmpleadoCategoria = em.find(EmpleadoCategoria.class, empleadoCategoria.getIdCategoria());
            List<Empleado> empleadoListOld = persistentEmpleadoCategoria.getEmpleadoList();
            List<Empleado> empleadoListNew = empleadoCategoria.getEmpleadoList();
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdPersona());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            empleadoCategoria.setEmpleadoList(empleadoListNew);
            empleadoCategoria = em.merge(empleadoCategoria);
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    empleadoListOldEmpleado.setCategoria(null);
                    empleadoListOldEmpleado = em.merge(empleadoListOldEmpleado);
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    EmpleadoCategoria oldCategoriaOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getCategoria();
                    empleadoListNewEmpleado.setCategoria(empleadoCategoria);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldCategoriaOfEmpleadoListNewEmpleado != null && !oldCategoriaOfEmpleadoListNewEmpleado.equals(empleadoCategoria)) {
                        oldCategoriaOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldCategoriaOfEmpleadoListNewEmpleado = em.merge(oldCategoriaOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empleadoCategoria.getIdCategoria();
                if (findEmpleadoCategoria(id) == null) {
                    throw new NonexistentEntityException("The empleadoCategoria with id " + id + " no longer exists.");
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
            EmpleadoCategoria empleadoCategoria;
            try {
                empleadoCategoria = em.getReference(EmpleadoCategoria.class, id);
                empleadoCategoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleadoCategoria with id " + id + " no longer exists.", enfe);
            }
            List<Empleado> empleadoList = empleadoCategoria.getEmpleadoList();
            for (Empleado empleadoListEmpleado : empleadoList) {
                empleadoListEmpleado.setCategoria(null);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
            }
            em.remove(empleadoCategoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmpleadoCategoria> findEmpleadoCategoriaEntities() {
        return findEmpleadoCategoriaEntities(true, -1, -1);
    }

    public List<EmpleadoCategoria> findEmpleadoCategoriaEntities(int maxResults, int firstResult) {
        return findEmpleadoCategoriaEntities(false, maxResults, firstResult);
    }

    private List<EmpleadoCategoria> findEmpleadoCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmpleadoCategoria.class));
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

    public EmpleadoCategoria findEmpleadoCategoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmpleadoCategoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmpleadoCategoria> rt = cq.from(EmpleadoCategoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
