package ar.nex.sincronizar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
@Entity
@Table(name = "actividad")
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "uuid")
    private String uuid;
    @Basic(optional = false)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "device")
    private String device;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "entity")
    private String entity;
    @Column(name = "entity_uuid")
    private String entityUuid;
    @Column(name = "entity_json")
    private String entityJson;
    @Column(name = "sincronizacion")
    private SincronizarEstado sincronizacion;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @PreUpdate
    public void setLastUpdate() {
        this.updated = new Date();
    }

    public Actividad() {
        try {
            this.uuid = UUID.randomUUID().toString();
            //Nombre del dispositivo
            this.device = InetAddress.getLocalHost().getHostName();
            this.sincronizacion = SincronizarEstado.PENDIENTE;
            this.created = new Date();
            this.updated = new Date();
        } catch (Exception e) {
            this.device = "Ni idea";
        }
    }

    public Actividad(String tipo, String usuario, Object object) {
        try {
            this.uuid = UUID.randomUUID().toString();
            //Nombre del dispositivo
            this.device = InetAddress.getLocalHost().getHostName();
            this.tipo = tipo;
            this.usuario = usuario;
            this.entity = object.getClass().getSimpleName();
            this.sincronizacion = SincronizarEstado.PENDIENTE;
            this.created = new Date();
            this.updated = new Date();
        } catch (Exception e) {
            this.device = "Ni idea";
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        this.entityUuid = entityUuid;
    }

    public String getEntityJson() {
        return entityJson;
    }

    public void setEntityJson(String entityJson) {
        this.entityJson = entityJson;
    }

    public SincronizarEstado getSincronizacion() {
        return sincronizacion;
    }

    public void setSincronizacion(SincronizarEstado sincronizacion) {
        this.sincronizacion = sincronizacion;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.tipo + " - " + this.device + " - " + this.entity;
    }

}
