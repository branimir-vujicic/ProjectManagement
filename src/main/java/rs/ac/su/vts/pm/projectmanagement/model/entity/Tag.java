package rs.ac.su.vts.pm.projectmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
@Entity(name = "tag")
public class Tag
        extends BaseEntity
{

    @Column(length = 64)
    private String name;

    private String color;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Project> projects;

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        final Tag other = (Tag) o;
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
        return new StringJoiner(", ", Tag.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("title='" + getName() + "'")
                .toString();
    }
    // endregion

    // region: support functions
    public Integer getNumberOfProject()
    {
        if (projects == null) {
            return 0;
        }
        return projects.size();
    }
    // endregion
}
