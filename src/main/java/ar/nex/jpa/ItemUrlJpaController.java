/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.entity.ItemUrl;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.entity.ItemUuid;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ItemUrlJpaController implements Serializable {

    public ItemUrlJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemUrl itemUrl) throws PreexistingEntityException, Exception {
        if (itemUrl.getItemUuidCollection() == null) {
            itemUrl.setItemUuidCollection(new ArrayList<ItemUuid>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ItemUuid> attachedItemUuidCollection = new ArrayList<ItemUuid>();
            for (ItemUuid itemUuidCollectionItemUuidToAttach : itemUrl.getItemUuidCollection()) {
                itemUuidCollectionItemUuidToAttach = em.getReference(itemUuidCollectionItemUuidToAttach.getClass(), itemUuidCollectionItemUuidToAttach.getUuid());
                attachedItemUuidCollection.add(itemUuidCollectionItemUuidToAttach);
            }
            itemUrl.setItemUuidCollection(attachedItemUuidCollection);
            em.persist(itemUrl);
            for (ItemUuid itemUuidCollectionItemUuid : itemUrl.getItemUuidCollection()) {
                ItemUrl oldUrlOfItemUuidCollectionItemUuid = itemUuidCollectionItemUuid.getUrl();
                itemUuidCollectionItemUuid.setUrl(itemUrl);
                itemUuidCollectionItemUuid = em.merge(itemUuidCollectionItemUuid);
                if (oldUrlOfItemUuidCollectionItemUuid != null) {
                    oldUrlOfItemUuidCollectionItemUuid.getItemUuidCollection().remove(itemUuidCollectionItemUuid);
                    oldUrlOfItemUuidCollectionItemUuid = em.merge(oldUrlOfItemUuidCollectionItemUuid);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItemUrl(itemUrl.getUuid()) != null) {
                throw new PreexistingEntityException("ItemUrl " + itemUrl + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemUrl itemUrl) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemUrl persistentItemUrl = em.find(ItemUrl.class, itemUrl.getUuid());
            Collection<ItemUuid> itemUuidCollectionOld = persistentItemUrl.getItemUuidCollection();
            Collection<ItemUuid> itemUuidCollectionNew = itemUrl.getItemUuidCollection();
            Collection<ItemUuid> attachedItemUuidCollectionNew = new ArrayList<ItemUuid>();
            for (ItemUuid itemUuidCollectionNewItemUuidToAttach : itemUuidCollectionNew) {
                itemUuidCollectionNewItemUuidToAttach = em.getReference(itemUuidCollectionNewItemUuidToAttach.getClass(), itemUuidCollectionNewItemUuidToAttach.getUuid());
                attachedItemUuidCollectionNew.add(itemUuidCollectionNewItemUuidToAttach);
            }
            itemUuidCollectionNew = attachedItemUuidCollectionNew;
            itemUrl.setItemUuidCollection(itemUuidCollectionNew);
            itemUrl = em.merge(itemUrl);
            for (ItemUuid itemUuidCollectionOldItemUuid : itemUuidCollectionOld) {
                if (!itemUuidCollectionNew.contains(itemUuidCollectionOldItemUuid)) {
                    itemUuidCollectionOldItemUuid.setUrl(null);
                    itemUuidCollectionOldItemUuid = em.merge(itemUuidCollectionOldItemUuid);
                }
            }
            for (ItemUuid itemUuidCollectionNewItemUuid : itemUuidCollectionNew) {
                if (!itemUuidCollectionOld.contains(itemUuidCollectionNewItemUuid)) {
                    ItemUrl oldUrlOfItemUuidCollectionNewItemUuid = itemUuidCollectionNewItemUuid.getUrl();
                    itemUuidCollectionNewItemUuid.setUrl(itemUrl);
                    itemUuidCollectionNewItemUuid = em.merge(itemUuidCollectionNewItemUuid);
                    if (oldUrlOfItemUuidCollectionNewItemUuid != null && !oldUrlOfItemUuidCollectionNewItemUuid.equals(itemUrl)) {
                        oldUrlOfItemUuidCollectionNewItemUuid.getItemUuidCollection().remove(itemUuidCollectionNewItemUuid);
                        oldUrlOfItemUuidCollectionNewItemUuid = em.merge(oldUrlOfItemUuidCollectionNewItemUuid);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = itemUrl.getUuid();
                if (findItemUrl(id) == null) {
                    throw new NonexistentEntityException("The itemUrl with id " + id + " no longer exists.");
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
            ItemUrl itemUrl;
            try {
                itemUrl = em.getReference(ItemUrl.class, id);
                itemUrl.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemUrl with id " + id + " no longer exists.", enfe);
            }
            Collection<ItemUuid> itemUuidCollection = itemUrl.getItemUuidCollection();
            for (ItemUuid itemUuidCollectionItemUuid : itemUuidCollection) {
                itemUuidCollectionItemUuid.setUrl(null);
                itemUuidCollectionItemUuid = em.merge(itemUuidCollectionItemUuid);
            }
            em.remove(itemUrl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemUrl> findItemUrlEntities() {
        return findItemUrlEntities(true, -1, -1);
    }

    public List<ItemUrl> findItemUrlEntities(int maxResults, int firstResult) {
        return findItemUrlEntities(false, maxResults, firstResult);
    }

    private List<ItemUrl> findItemUrlEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemUrl.class));
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

    public ItemUrl findItemUrl(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemUrl.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemUrlCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemUrl> rt = cq.from(ItemUrl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
