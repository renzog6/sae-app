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
import ar.nex.entity.Usuario;
import ar.nex.entity.UsuarioGrupo;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsuarioGrupoJpaController implements Serializable {

    public UsuarioGrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioGrupo usuarioGrupo) {
        if (usuarioGrupo.getUsuarioList() == null) {
            usuarioGrupo.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usuarioGrupo.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usuarioGrupo.setUsuarioList(attachedUsuarioList);
            em.persist(usuarioGrupo);
            for (Usuario usuarioListUsuario : usuarioGrupo.getUsuarioList()) {
                UsuarioGrupo oldGrupoOfUsuarioListUsuario = usuarioListUsuario.getGrupo();
                usuarioListUsuario.setGrupo(usuarioGrupo);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldGrupoOfUsuarioListUsuario != null) {
                    oldGrupoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldGrupoOfUsuarioListUsuario = em.merge(oldGrupoOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioGrupo usuarioGrupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioGrupo persistentUsuarioGrupo = em.find(UsuarioGrupo.class, usuarioGrupo.getIdGrupo());
            List<Usuario> usuarioListOld = persistentUsuarioGrupo.getUsuarioList();
            List<Usuario> usuarioListNew = usuarioGrupo.getUsuarioList();
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usuarioGrupo.setUsuarioList(usuarioListNew);
            usuarioGrupo = em.merge(usuarioGrupo);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setGrupo(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    UsuarioGrupo oldGrupoOfUsuarioListNewUsuario = usuarioListNewUsuario.getGrupo();
                    usuarioListNewUsuario.setGrupo(usuarioGrupo);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldGrupoOfUsuarioListNewUsuario != null && !oldGrupoOfUsuarioListNewUsuario.equals(usuarioGrupo)) {
                        oldGrupoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldGrupoOfUsuarioListNewUsuario = em.merge(oldGrupoOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuarioGrupo.getIdGrupo();
                if (findUsuarioGrupo(id) == null) {
                    throw new NonexistentEntityException("The usuarioGrupo with id " + id + " no longer exists.");
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
            UsuarioGrupo usuarioGrupo;
            try {
                usuarioGrupo = em.getReference(UsuarioGrupo.class, id);
                usuarioGrupo.getIdGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioGrupo with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarioList = usuarioGrupo.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setGrupo(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(usuarioGrupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioGrupo> findUsuarioGrupoEntities() {
        return findUsuarioGrupoEntities(true, -1, -1);
    }

    public List<UsuarioGrupo> findUsuarioGrupoEntities(int maxResults, int firstResult) {
        return findUsuarioGrupoEntities(false, maxResults, firstResult);
    }

    private List<UsuarioGrupo> findUsuarioGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioGrupo.class));
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

    public UsuarioGrupo findUsuarioGrupo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioGrupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioGrupo> rt = cq.from(UsuarioGrupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
