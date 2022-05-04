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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "comment")
@Entity(name = "comment")
public class Comment
        extends BaseEntity
{

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Setter
    private Set<Comment> children;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        final Comment other = (Comment) o;
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
        return new StringJoiner(", ", Comment.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .toString();
    }
    // endregion

    // region bidirectional methods

    // endregion
}
