package rs.ac.su.vts.pm.projectmanagement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;
import rs.ac.su.vts.pm.projectmanagement.model.base.BaseEntity;
import rs.ac.su.vts.pm.projectmanagement.model.common.UserRole;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "user")
@Table(name = "user", uniqueConstraints = @UniqueConstraint(name = "email_idx", columnNames = {"email"}))
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id = ?")
public class User
        extends BaseEntity
{

    public static final String ROLE_ADMIN = UserRole.ADMIN.name();
    public static final String ROLE_USER = UserRole.USER.name();

    private String email;
    @JsonIgnore
    private String password;

    @Builder.Default
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    private String name;

    @JsonIgnore
    private String organization;

    @JsonIgnore
    private Date lastLoginTime;

    @JsonIgnore
    private String passwordRecoveryKey;

    @JsonIgnore
    private Long passwordRecoveryTime;

    @JsonIgnore
    private String emailVerificationKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(STRING)
    private Set<UserRole> roles;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> rights;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Where(clause = "deleted = false")
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Where(clause = "deleted = false")
    private List<ProjectOrganizer> projectOrganizers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Project> projectFavorites = new HashSet<>();

    @Builder.Default
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean setupUser = false;

    @Transient
    private String accessToken;

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if (roles == null) {
            return Collections.singleton(new SimpleGrantedAuthority("UNCONFIRMED"));
        }
        return roles.stream().map(Enum::name).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    // region hash code, equals, to string
    @Override
    public boolean equals(final Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User other = (User) o;
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
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("name='" + getName() + "'")
                .toString();
    }
    // endregion

    public void addProject(Project project)
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        projectFavorites.add(project);
    }

    public void addProjects(Collection<Project> projectCollection)
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        projectFavorites.addAll(projectCollection);
    }

    public void clearProjects()
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        projectFavorites.clear();
    }

    public void removeProject(Project project)
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        projectFavorites.remove(project);
    }

    public boolean isFavorite(Project project)
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        return projectFavorites.contains(project);
    }

    public boolean isFavorite(Long projectId)
    {
        if (projectFavorites == null) {
            projectFavorites = new HashSet<>();
        }
        return projectFavorites.stream().anyMatch(project -> Objects.equals(projectId, project.getId()));
    }

    public boolean hasRole(UserRole role)
    {
        if (!CollectionUtils.isEmpty(roles)) {
            return getRoles().contains(role);
        }
        return false;
    }
}
