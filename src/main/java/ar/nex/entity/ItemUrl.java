/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "item_url")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemUrl.findAll", query = "SELECT i FROM ItemUrl i"),
    @NamedQuery(name = "ItemUrl.findByUuid", query = "SELECT i FROM ItemUrl i WHERE i.uuid = :uuid"),
    @NamedQuery(name = "ItemUrl.findById", query = "SELECT i FROM ItemUrl i WHERE i.id = :id"),
    @NamedQuery(name = "ItemUrl.findByName", query = "SELECT i FROM ItemUrl i WHERE i.name = :name"),
    @NamedQuery(name = "ItemUrl.findByInfo", query = "SELECT i FROM ItemUrl i WHERE i.info = :info"),
    @NamedQuery(name = "ItemUrl.findByCreatedAt", query = "SELECT i FROM ItemUrl i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "ItemUrl.findByUpdatedAt", query = "SELECT i FROM ItemUrl i WHERE i.updatedAt = :updatedAt")})
public class ItemUrl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "info")
    private String info;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @OneToMany(mappedBy = "url")
    private Collection<ItemUuid> itemUuidCollection;

    public ItemUrl() {
        this.uuid = UUID.randomUUID().toString();
    }

    public ItemUrl(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    public Collection<ItemUuid> getItemUuidCollection() {
        return itemUuidCollection;
    }

    public void setItemUuidCollection(Collection<ItemUuid> itemUuidCollection) {
        this.itemUuidCollection = itemUuidCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemUrl)) {
            return false;
        }
        ItemUrl other = (ItemUrl) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.ItemUrl[ uuid=" + uuid + " ]";
    }
    
}
