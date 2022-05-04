package rs.ac.su.vts.pm.projectmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntityAudit;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "project_member")
@Entity(name = "project_member")
public class ProjectMember
        extends BaseEntityAudit
{

    @EmbeddedId
    protected ProjectMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @Where(clause = "deleted = false")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @Where(clause = "deleted = false")
    private User user;

    public ProjectMember(Project project, User user)
    {
        this.project = project;
        this.user = user;
        this.id = new ProjectMemberId(this.project.getId(), this.user.getId());
    }

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectMember)) {
            return false;
        }
        final ProjectMember other = (ProjectMember) o;
        return Objects.equals(other.getId(), getId());
    }

    @Override
    public int hashCode()
    {
        return 1;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", ProjectMember.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .toString();
    }
    // endregion

    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ProjectMemberId
            implements Serializable
    {
        private Long projectId;
        private Long userId;
    }
}