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
import ar.nex.entity.ItemUrl;
import ar.nex.entity.ItemUuid;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ItemUuidJpaController implements Serializable {

    public ItemUuidJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemUuid itemUuid) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemUrl url = itemUuid.getUrl();
            if (url != null) {
                url = em.getReference(url.getClass(), url.getUuid());
                itemUuid.setUrl(url);
            }
            em.persist(itemUuid);
            if (url != null) {
                url.getItemUuidCollection().add(itemUuid);
                url = em.merge(url);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItemUuid(itemUuid.getUuid()) != null) {
                throw new PreexistingEntityException("ItemUuid " + itemUuid + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemUuid itemUuid) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemUuid persistentItemUuid = em.find(ItemUuid.class, itemUuid.getUuid());
            ItemUrl urlOld = persistentItemUuid.getUrl();
            ItemUrl urlNew = itemUuid.getUrl();
            if (urlNew != null) {
                urlNew = em.getReference(urlNew.getClass(), urlNew.getUuid());
                itemUuid.setUrl(urlNew);
            }
            itemUuid = em.merge(itemUuid);
            if (urlOld != null && !urlOld.equals(urlNew)) {
                urlOld.getItemUuidCollection().remove(itemUuid);
                urlOld = em.merge(urlOld);
            }
            if (urlNew != null && !urlNew.equals(urlOld)) {
                urlNew.getItemUuidCollection().add(itemUuid);
                urlNew = em.merge(urlNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = itemUuid.getUuid();
                if (findItemUuid(id) == null) {
                    throw new NonexistentEntityException("The itemUuid with id " + id + " no longer exists.");
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
            ItemUuid itemUuid;
            try {
                itemUuid = em.getReference(ItemUuid.class, id);
                itemUuid.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemUuid with id " + id + " no longer exists.", enfe);
            }
            ItemUrl url = itemUuid.getUrl();
            if (url != null) {
                url.getItemUuidCollection().remove(itemUuid);
                url = em.merge(url);
            }
            em.remove(itemUuid);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemUuid> findItemUuidEntities() {
        return findItemUuidEntities(true, -1, -1);
    }

    public List<ItemUuid> findItemUuidEntities(int maxResults, int firstResult) {
        return findItemUuidEntities(false, maxResults, firstResult);
    }

    private List<ItemUuid> findItemUuidEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemUuid.class));
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

    public ItemUuid findItemUuid(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemUuid.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemUuidCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemUuid> rt = cq.from(ItemUuid.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
