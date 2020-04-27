/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.Seguro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.equipo.Equipo;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.empleado.Empleado;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class SeguroJpaController implements Serializable {

    public SeguroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seguro seguro) {
        if (seguro.getEquipoList() == null) {
            seguro.setEquipoList(new ArrayList<Equipo>());
        }
        if (seguro.getEmpleadoList() == null) {
            seguro.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : seguro.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            seguro.setEquipoList(attachedEquipoList);
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : seguro.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdPersona());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            seguro.setEmpleadoList(attachedEmpleadoList);
            em.persist(seguro);
            for (Equipo equipoListEquipo : seguro.getEquipoList()) {
                Seguro oldSeguroOfEquipoListEquipo = equipoListEquipo.getSeguro();
                equipoListEquipo.setSeguro(seguro);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldSeguroOfEquipoListEquipo != null) {
                    oldSeguroOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldSeguroOfEquipoListEquipo = em.merge(oldSeguroOfEquipoListEquipo);
                }
            }
            for (Empleado empleadoListEmpleado : seguro.getEmpleadoList()) {
                Seguro oldSeguroOfEmpleadoListEmpleado = empleadoListEmpleado.getSeguro();
                empleadoListEmpleado.setSeguro(seguro);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldSeguroOfEmpleadoListEmpleado != null) {
                    oldSeguroOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldSeguroOfEmpleadoListEmpleado = em.merge(oldSeguroOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seguro seguro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seguro persistentSeguro = em.find(Seguro.class, seguro.getIdSeguro());
            List<Equipo> equipoListOld = persistentSeguro.getEquipoList();
            List<Equipo> equipoListNew = seguro.getEquipoList();
            List<Empleado> empleadoListOld = persistentSeguro.getEmpleadoList();
            List<Empleado> empleadoListNew = seguro.getEmpleadoList();
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            seguro.setEquipoList(equipoListNew);
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdPersona());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            seguro.setEmpleadoList(empleadoListNew);
            seguro = em.merge(seguro);
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setSeguro(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Seguro oldSeguroOfEquipoListNewEquipo = equipoListNewEquipo.getSeguro();
                    equipoListNewEquipo.setSeguro(seguro);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldSeguroOfEquipoListNewEquipo != null && !oldSeguroOfEquipoListNewEquipo.equals(seguro)) {
                        oldSeguroOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldSeguroOfEquipoListNewEquipo = em.merge(oldSeguroOfEquipoListNewEquipo);
                    }
                }
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    empleadoListOldEmpleado.setSeguro(null);
                    empleadoListOldEmpleado = em.merge(empleadoListOldEmpleado);
                }
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Seguro oldSeguroOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getSeguro();
                    empleadoListNewEmpleado.setSeguro(seguro);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldSeguroOfEmpleadoListNewEmpleado != null && !oldSeguroOfEmpleadoListNewEmpleado.equals(seguro)) {
                        oldSeguroOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldSeguroOfEmpleadoListNewEmpleado = em.merge(oldSeguroOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = seguro.getIdSeguro();
                if (findSeguro(id) == null) {
                    throw new NonexistentEntityException("The seguro with id " + id + " no longer exists.");
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
            Seguro seguro;
            try {
                seguro = em.getReference(Seguro.class, id);
                seguro.getIdSeguro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seguro with id " + id + " no longer exists.", enfe);
            }
            List<Equipo> equipoList = seguro.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setSeguro(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<Empleado> empleadoList = seguro.getEmpleadoList();
            for (Empleado empleadoListEmpleado : empleadoList) {
                empleadoListEmpleado.setSeguro(null);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
            }
            em.remove(seguro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seguro> findSeguroEntities() {
        return findSeguroEntities(true, -1, -1);
    }

    public List<Seguro> findSeguroEntities(int maxResults, int firstResult) {
        return findSeguroEntities(false, maxResults, firstResult);
    }

    private List<Seguro> findSeguroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seguro.class));
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

    public Seguro findSeguro(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seguro.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeguroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seguro> rt = cq.from(Seguro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
