package rs.ac.su.vts.pm.projectmanagement.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.util.CollectionUtils;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "project")
@Entity(name = "project")
public class Project
        extends BaseEntity
{

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate date;

    private LocalTime timeFrom;
    private LocalTime timeTo;

    @Builder.Default
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean archived = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "deleted = false")
    @Builder.Default
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "deleted = false")
    @Builder.Default
    private List<ProjectOrganizer> projectOrganizers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "deleted = false")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "project_tags",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    private String directory;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "projectFavorites")
    @Builder.Default
    private Set<User> usersFavorites = new HashSet<>();

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        final Project other = (Project) o;
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
        return new StringJoiner(", ", Project.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("title='" + getTitle() + "'")
                .toString();
    }
    // endregion

    @Transient
    public Set<User> getMembers()
    {
        if (CollectionUtils.isEmpty(getProjectMembers())) {
            return Set.of();
        }
        return getProjectMembers().stream().map(ProjectMember::getUser).filter(u -> !u.isDeleted()).collect(Collectors.toSet());
    }

    @Transient
    public Set<User> getOrganizers()
    {
        if (CollectionUtils.isEmpty(getProjectOrganizers())) {
            return Set.of();
        }
        return getProjectOrganizers().stream().map(ProjectOrganizer::getUser).filter(u -> !u.isDeleted()).collect(Collectors.toSet());
    }

    // region bidirectional methods
    public void addProjectMember(ProjectMember projectMember)
    {
        projectMembers.add(projectMember);
        projectMember.setProject(this);
    }

    public void removeProjectMember(ProjectMember projectMember)
    {
        projectMembers.remove(projectMember);
        projectMember.setProject(null);
    }

    public void addProjectOrganizer(ProjectOrganizer projectOrganizer)
    {
        projectOrganizers.add(projectOrganizer);
        projectOrganizer.setProject(this);
    }

    public void removeProjectOrganizer(ProjectOrganizer projectOrganizer)
    {
        projectOrganizers.remove(projectOrganizer);
        projectOrganizer.setProject(null);
    }

    public void addComment(Comment comment)
    {
        comments.add(comment);
        comment.setProject(this);
    }

    public void removeComment(Comment comment)
    {
        comments.remove(comment);
        comment.setProject(null);
    }

    public void addUser(User user)
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        usersFavorites.add(user);
    }

    public void addUsers(Collection<User> userCollection)
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        usersFavorites.addAll(userCollection);
    }

    public void clearUsers()
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        usersFavorites.clear();
    }

    public void removeUser(User user)
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        usersFavorites.remove(user);
    }

    public boolean isFavorite(User user)
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        return usersFavorites.contains(user);
    }

    public boolean isFavorite(Long userId)
    {
        if (usersFavorites == null) {
            usersFavorites = new HashSet<>();
        }
        return usersFavorites.stream().anyMatch(user -> Objects.equals(userId, user.getId()));
    }

    public void addTag(Tag tag)
    {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.add(tag);
    }

    public void addTags(Collection<Tag> tagCollection)
    {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.addAll(tagCollection);
    }

    public void removeTag(Tag tag)
    {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.remove(tag);
    }

    public void clearTags()
    {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.clear();
    }

    // endregion
}
