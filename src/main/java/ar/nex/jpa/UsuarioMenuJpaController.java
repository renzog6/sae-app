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
import ar.nex.entity.UsuarioMenu;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class UsuarioMenuJpaController implements Serializable {

    public UsuarioMenuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioMenu usuarioMenu) {
        if (usuarioMenu.getUsuarioList() == null) {
            usuarioMenu.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usuarioMenu.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usuarioMenu.setUsuarioList(attachedUsuarioList);
            em.persist(usuarioMenu);
            for (Usuario usuarioListUsuario : usuarioMenu.getUsuarioList()) {
                usuarioListUsuario.getUsrMenuList().add(usuarioMenu);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioMenu usuarioMenu) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioMenu persistentUsuarioMenu = em.find(UsuarioMenu.class, usuarioMenu.getIdMenu());
            List<Usuario> usuarioListOld = persistentUsuarioMenu.getUsuarioList();
            List<Usuario> usuarioListNew = usuarioMenu.getUsuarioList();
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usuarioMenu.setUsuarioList(usuarioListNew);
            usuarioMenu = em.merge(usuarioMenu);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.getUsrMenuList().remove(usuarioMenu);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    usuarioListNewUsuario.getUsrMenuList().add(usuarioMenu);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuarioMenu.getIdMenu();
                if (findUsuarioMenu(id) == null) {
                    throw new NonexistentEntityException("The usuarioMenu with id " + id + " no longer exists.");
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
            UsuarioMenu usuarioMenu;
            try {
                usuarioMenu = em.getReference(UsuarioMenu.class, id);
                usuarioMenu.getIdMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioMenu with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarioList = usuarioMenu.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.getUsrMenuList().remove(usuarioMenu);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(usuarioMenu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioMenu> findUsuarioMenuEntities() {
        return findUsuarioMenuEntities(true, -1, -1);
    }

    public List<UsuarioMenu> findUsuarioMenuEntities(int maxResults, int firstResult) {
        return findUsuarioMenuEntities(false, maxResults, firstResult);
    }

    private List<UsuarioMenu> findUsuarioMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioMenu.class));
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

    public UsuarioMenu findUsuarioMenu(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioMenu.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioMenu> rt = cq.from(UsuarioMenu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
