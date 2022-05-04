package rs.ac.su.vts.pm.projectmanagement.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity
        extends BaseEntityAudit
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @JsonIgnore
    private Long version;

    public Long getId()
    {
        return this.id;
    }

    public Long getVersion()
    {
        return this.version;
    }

    public String toString()
    {
        return "BaseEntity(id=" + this.getId() + ")";
    }
}





