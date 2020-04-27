/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "item_uuid")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemUuid.findAll", query = "SELECT i FROM ItemUuid i"),
    @NamedQuery(name = "ItemUuid.findByUuid", query = "SELECT i FROM ItemUuid i WHERE i.uuid = :uuid"),
    @NamedQuery(name = "ItemUuid.findByName", query = "SELECT i FROM ItemUuid i WHERE i.name = :name"),
    @NamedQuery(name = "ItemUuid.findByInfo", query = "SELECT i FROM ItemUuid i WHERE i.info = :info"),
    @NamedQuery(name = "ItemUuid.findByCreatedAt", query = "SELECT i FROM ItemUuid i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "ItemUuid.findByUpdatedAt", query = "SELECT i FROM ItemUuid i WHERE i.updatedAt = :updatedAt")})
public class ItemUuid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "uuid")
    private String uuid;
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

    @JoinColumn(name = "url", referencedColumnName = "uuid")
    @ManyToOne
    private ItemUrl url;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = new Date();
    }

    public ItemUuid() {
        this.uuid = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        
        this.url = new ItemUrl();
    }

    public ItemUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public ItemUrl getUrl() {
        return url;
    }

    public void setUrl(ItemUrl url) {
        this.url = url;
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
        if (!(object instanceof ItemUuid)) {
            return false;
        }
        ItemUuid other = (ItemUuid) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.entity.ItemUuid[ uuid=" + uuid + " ]";
    }

}
