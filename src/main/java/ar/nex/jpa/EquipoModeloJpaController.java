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
import ar.nex.entity.equipo.EquipoTipo;
import ar.nex.entity.equipo.Repuesto;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.EquipoModelo;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class EquipoModeloJpaController implements Serializable {

    public EquipoModeloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoModelo equipoModelo) {
        if (equipoModelo.getRepuestoList() == null) {
            equipoModelo.setRepuestoList(new ArrayList<Repuesto>());
        }
        if (equipoModelo.getEquipoList() == null) {
            equipoModelo.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoTipo tipo = equipoModelo.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getIdTipo());
                equipoModelo.setTipo(tipo);
            }
            List<Repuesto> attachedRepuestoList = new ArrayList<Repuesto>();
            for (Repuesto repuestoListRepuestoToAttach : equipoModelo.getRepuestoList()) {
                repuestoListRepuestoToAttach = em.getReference(repuestoListRepuestoToAttach.getClass(), repuestoListRepuestoToAttach.getIdRepuesto());
                attachedRepuestoList.add(repuestoListRepuestoToAttach);
            }
            equipoModelo.setRepuestoList(attachedRepuestoList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : equipoModelo.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getIdEquipo());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            equipoModelo.setEquipoList(attachedEquipoList);
            em.persist(equipoModelo);
            if (tipo != null) {
                tipo.getModeloList().add(equipoModelo);
                tipo = em.merge(tipo);
            }
            for (Repuesto repuestoListRepuesto : equipoModelo.getRepuestoList()) {
                repuestoListRepuesto.getModeloList().add(equipoModelo);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            for (Equipo equipoListEquipo : equipoModelo.getEquipoList()) {
                EquipoModelo oldModeloOfEquipoListEquipo = equipoListEquipo.getModelo();
                equipoListEquipo.setModelo(equipoModelo);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldModeloOfEquipoListEquipo != null) {
                    oldModeloOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldModeloOfEquipoListEquipo = em.merge(oldModeloOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoModelo equipoModelo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoModelo persistentEquipoModelo = em.find(EquipoModelo.class, equipoModelo.getIdModelo());
            EquipoTipo tipoOld = persistentEquipoModelo.getTipo();
            EquipoTipo tipoNew = equipoModelo.getTipo();
            List<Repuesto> repuestoListOld = persistentEquipoModelo.getRepuestoList();
            List<Repuesto> repuestoListNew = equipoModelo.getRepuestoList();
            List<Equipo> equipoListOld = persistentEquipoModelo.getEquipoList();
            List<Equipo> equipoListNew = equipoModelo.getEquipoList();
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getIdTipo());
                equipoModelo.setTipo(tipoNew);
            }
            List<Repuesto> attachedRepuestoListNew = new ArrayList<Repuesto>();
            for (Repuesto repuestoListNewRepuestoToAttach : repuestoListNew) {
                repuestoListNewRepuestoToAttach = em.getReference(repuestoListNewRepuestoToAttach.getClass(), repuestoListNewRepuestoToAttach.getIdRepuesto());
                attachedRepuestoListNew.add(repuestoListNewRepuestoToAttach);
            }
            repuestoListNew = attachedRepuestoListNew;
            equipoModelo.setRepuestoList(repuestoListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getIdEquipo());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            equipoModelo.setEquipoList(equipoListNew);
            equipoModelo = em.merge(equipoModelo);
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getModeloList().remove(equipoModelo);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getModeloList().add(equipoModelo);
                tipoNew = em.merge(tipoNew);
            }
            for (Repuesto repuestoListOldRepuesto : repuestoListOld) {
                if (!repuestoListNew.contains(repuestoListOldRepuesto)) {
                    repuestoListOldRepuesto.getModeloList().remove(equipoModelo);
                    repuestoListOldRepuesto = em.merge(repuestoListOldRepuesto);
                }
            }
            for (Repuesto repuestoListNewRepuesto : repuestoListNew) {
                if (!repuestoListOld.contains(repuestoListNewRepuesto)) {
                    repuestoListNewRepuesto.getModeloList().add(equipoModelo);
                    repuestoListNewRepuesto = em.merge(repuestoListNewRepuesto);
                }
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setModelo(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    EquipoModelo oldModeloOfEquipoListNewEquipo = equipoListNewEquipo.getModelo();
                    equipoListNewEquipo.setModelo(equipoModelo);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldModeloOfEquipoListNewEquipo != null && !oldModeloOfEquipoListNewEquipo.equals(equipoModelo)) {
                        oldModeloOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldModeloOfEquipoListNewEquipo = em.merge(oldModeloOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = equipoModelo.getIdModelo();
                if (findEquipoModelo(id) == null) {
                    throw new NonexistentEntityException("The equipoModelo with id " + id + " no longer exists.");
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
            EquipoModelo equipoModelo;
            try {
                equipoModelo = em.getReference(EquipoModelo.class, id);
                equipoModelo.getIdModelo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoModelo with id " + id + " no longer exists.", enfe);
            }
            EquipoTipo tipo = equipoModelo.getTipo();
            if (tipo != null) {
                tipo.getModeloList().remove(equipoModelo);
                tipo = em.merge(tipo);
            }
            List<Repuesto> repuestoList = equipoModelo.getRepuestoList();
            for (Repuesto repuestoListRepuesto : repuestoList) {
                repuestoListRepuesto.getModeloList().remove(equipoModelo);
                repuestoListRepuesto = em.merge(repuestoListRepuesto);
            }
            List<Equipo> equipoList = equipoModelo.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setModelo(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            em.remove(equipoModelo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoModelo> findEquipoModeloEntities() {
        return findEquipoModeloEntities(true, -1, -1);
    }

    public List<EquipoModelo> findEquipoModeloEntities(int maxResults, int firstResult) {
        return findEquipoModeloEntities(false, maxResults, firstResult);
    }

    private List<EquipoModelo> findEquipoModeloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoModelo.class));
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

    public EquipoModelo findEquipoModelo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoModelo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoModeloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoModelo> rt = cq.from(EquipoModelo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
