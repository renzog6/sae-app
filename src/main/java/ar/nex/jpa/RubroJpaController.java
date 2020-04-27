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
import ar.nex.entity.empresa.SubRubro;
import java.util.ArrayList;
import java.util.List;
import ar.nex.entity.empresa.Empresa;
import ar.nex.entity.empresa.Rubro;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class RubroJpaController implements Serializable {

    public RubroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rubro rubro) {
        if (rubro.getSubRubroList() == null) {
            rubro.setSubRubroList(new ArrayList<SubRubro>());
        }
        if (rubro.getEmpresaList() == null) {
            rubro.setEmpresaList(new ArrayList<Empresa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SubRubro> attachedSubRubroList = new ArrayList<SubRubro>();
            for (SubRubro subRubroListSubRubroToAttach : rubro.getSubRubroList()) {
                subRubroListSubRubroToAttach = em.getReference(subRubroListSubRubroToAttach.getClass(), subRubroListSubRubroToAttach.getIdSub());
                attachedSubRubroList.add(subRubroListSubRubroToAttach);
            }
            rubro.setSubRubroList(attachedSubRubroList);
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : rubro.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getIdEmpresa());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            rubro.setEmpresaList(attachedEmpresaList);
            em.persist(rubro);
            for (SubRubro subRubroListSubRubro : rubro.getSubRubroList()) {
                Rubro oldRubroOfSubRubroListSubRubro = subRubroListSubRubro.getRubro();
                subRubroListSubRubro.setRubro(rubro);
                subRubroListSubRubro = em.merge(subRubroListSubRubro);
                if (oldRubroOfSubRubroListSubRubro != null) {
                    oldRubroOfSubRubroListSubRubro.getSubRubroList().remove(subRubroListSubRubro);
                    oldRubroOfSubRubroListSubRubro = em.merge(oldRubroOfSubRubroListSubRubro);
                }
            }
            for (Empresa empresaListEmpresa : rubro.getEmpresaList()) {
                empresaListEmpresa.getRubroList().add(rubro);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rubro rubro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rubro persistentRubro = em.find(Rubro.class, rubro.getIdRubro());
            List<SubRubro> subRubroListOld = persistentRubro.getSubRubroList();
            List<SubRubro> subRubroListNew = rubro.getSubRubroList();
            List<Empresa> empresaListOld = persistentRubro.getEmpresaList();
            List<Empresa> empresaListNew = rubro.getEmpresaList();
            List<SubRubro> attachedSubRubroListNew = new ArrayList<SubRubro>();
            for (SubRubro subRubroListNewSubRubroToAttach : subRubroListNew) {
                subRubroListNewSubRubroToAttach = em.getReference(subRubroListNewSubRubroToAttach.getClass(), subRubroListNewSubRubroToAttach.getIdSub());
                attachedSubRubroListNew.add(subRubroListNewSubRubroToAttach);
            }
            subRubroListNew = attachedSubRubroListNew;
            rubro.setSubRubroList(subRubroListNew);
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            rubro.setEmpresaList(empresaListNew);
            rubro = em.merge(rubro);
            for (SubRubro subRubroListOldSubRubro : subRubroListOld) {
                if (!subRubroListNew.contains(subRubroListOldSubRubro)) {
                    subRubroListOldSubRubro.setRubro(null);
                    subRubroListOldSubRubro = em.merge(subRubroListOldSubRubro);
                }
            }
            for (SubRubro subRubroListNewSubRubro : subRubroListNew) {
                if (!subRubroListOld.contains(subRubroListNewSubRubro)) {
                    Rubro oldRubroOfSubRubroListNewSubRubro = subRubroListNewSubRubro.getRubro();
                    subRubroListNewSubRubro.setRubro(rubro);
                    subRubroListNewSubRubro = em.merge(subRubroListNewSubRubro);
                    if (oldRubroOfSubRubroListNewSubRubro != null && !oldRubroOfSubRubroListNewSubRubro.equals(rubro)) {
                        oldRubroOfSubRubroListNewSubRubro.getSubRubroList().remove(subRubroListNewSubRubro);
                        oldRubroOfSubRubroListNewSubRubro = em.merge(oldRubroOfSubRubroListNewSubRubro);
                    }
                }
            }
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    empresaListOldEmpresa.getRubroList().remove(rubro);
                    empresaListOldEmpresa = em.merge(empresaListOldEmpresa);
                }
            }
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    empresaListNewEmpresa.getRubroList().add(rubro);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = rubro.getIdRubro();
                if (findRubro(id) == null) {
                    throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.");
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
            Rubro rubro;
            try {
                rubro = em.getReference(Rubro.class, id);
                rubro.getIdRubro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rubro with id " + id + " no longer exists.", enfe);
            }
            List<SubRubro> subRubroList = rubro.getSubRubroList();
            for (SubRubro subRubroListSubRubro : subRubroList) {
                subRubroListSubRubro.setRubro(null);
                subRubroListSubRubro = em.merge(subRubroListSubRubro);
            }
            List<Empresa> empresaList = rubro.getEmpresaList();
            for (Empresa empresaListEmpresa : empresaList) {
                empresaListEmpresa.getRubroList().remove(rubro);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            em.remove(rubro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rubro> findRubroEntities() {
        return findRubroEntities(true, -1, -1);
    }

    public List<Rubro> findRubroEntities(int maxResults, int firstResult) {
        return findRubroEntities(false, maxResults, firstResult);
    }

    private List<Rubro> findRubroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rubro.class));
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

    public Rubro findRubro(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rubro.class, id);
        } finally {
            em.close();
        }
    }

    public int getRubroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rubro> rt = cq.from(Rubro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
