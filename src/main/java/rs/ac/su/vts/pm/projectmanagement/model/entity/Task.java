package rs.ac.su.vts.pm.projectmanagement.model.entity;

import lombok.*;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntity;
import rs.ac.su.vts.pm.projectmanagement.model.common.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "task")
@Entity(name = "task")
public class Task
        extends BaseEntity {

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    private Status status;

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        final Task other = (Task) o;
        return Objects.equals(other.getId(), getId());
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .toString();
    }
    // endregion

    // region bidirectional methods

    // endregion
}
