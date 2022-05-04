package rs.ac.su.vts.pm.projectmanagement.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class BaseEntityAudit
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    @JsonIgnore
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    @JsonIgnore
    private ZonedDateTime updatedOn;

    @Column(name = "created_by")
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;

    @JsonIgnore
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean deleted = false;

    public ZonedDateTime getCreatedOn()
    {return this.createdOn;}

    public ZonedDateTime getUpdatedOn()
    {return this.updatedOn;}

    public String getCreatedBy()
    {return this.createdBy;}

    public String getModifiedBy()
    {return this.modifiedBy;}

    public boolean isDeleted()
    {return this.deleted;}

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }
}





