/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.sincronizar.Actividad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.sincronizar.Dispositivo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) throws PreexistingEntityException, Exception {
        if (actividad.getDispositivoList() == null) {
            actividad.setDispositivoList(new ArrayList<Dispositivo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Dispositivo> attachedDispositivoList = new ArrayList<Dispositivo>();
            for (Dispositivo dispositivoListDispositivoToAttach : actividad.getDispositivoList()) {
                dispositivoListDispositivoToAttach = em.getReference(dispositivoListDispositivoToAttach.getClass(), dispositivoListDispositivoToAttach.getUuid());
                attachedDispositivoList.add(dispositivoListDispositivoToAttach);
            }
            actividad.setDispositivoList(attachedDispositivoList);
            em.persist(actividad);
            for (Dispositivo dispositivoListDispositivo : actividad.getDispositivoList()) {
                dispositivoListDispositivo.getActividadList().add(actividad);
                dispositivoListDispositivo = em.merge(dispositivoListDispositivo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActividad(actividad.getUuid()) != null) {
                throw new PreexistingEntityException("Actividad " + actividad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getUuid());
            List<Dispositivo> dispositivoListOld = persistentActividad.getDispositivoList();
            List<Dispositivo> dispositivoListNew = actividad.getDispositivoList();
            List<Dispositivo> attachedDispositivoListNew = new ArrayList<Dispositivo>();
            for (Dispositivo dispositivoListNewDispositivoToAttach : dispositivoListNew) {
                dispositivoListNewDispositivoToAttach = em.getReference(dispositivoListNewDispositivoToAttach.getClass(), dispositivoListNewDispositivoToAttach.getUuid());
                attachedDispositivoListNew.add(dispositivoListNewDispositivoToAttach);
            }
            dispositivoListNew = attachedDispositivoListNew;
            actividad.setDispositivoList(dispositivoListNew);
            actividad = em.merge(actividad);
            for (Dispositivo dispositivoListOldDispositivo : dispositivoListOld) {
                if (!dispositivoListNew.contains(dispositivoListOldDispositivo)) {
                    dispositivoListOldDispositivo.getActividadList().remove(actividad);
                    dispositivoListOldDispositivo = em.merge(dispositivoListOldDispositivo);
                }
            }
            for (Dispositivo dispositivoListNewDispositivo : dispositivoListNew) {
                if (!dispositivoListOld.contains(dispositivoListNewDispositivo)) {
                    dispositivoListNewDispositivo.getActividadList().add(actividad);
                    dispositivoListNewDispositivo = em.merge(dispositivoListNewDispositivo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = actividad.getUuid();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            List<Dispositivo> dispositivoList = actividad.getDispositivoList();
            for (Dispositivo dispositivoListDispositivo : dispositivoList) {
                dispositivoListDispositivo.getActividadList().remove(actividad);
                dispositivoListDispositivo = em.merge(dispositivoListDispositivo);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
