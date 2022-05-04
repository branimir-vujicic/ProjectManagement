package rs.ac.su.vts.pm.projectmanagement.services.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.exception.NotAuthorizedException;
import rs.ac.su.vts.pm.projectmanagement.model.entity.Project;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.util.Objects;
import java.util.Optional;

@Service
public class ContextService
{

    public static Optional<User> getLoggedUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof User)) {
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static boolean hasRole(String... roles)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication != null) && (authentication.getPrincipal() instanceof User)) {
            for (String role : roles) {
                if (role == null) {
                    continue;
                }
                boolean match = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).anyMatch(role::equalsIgnoreCase);
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void requiresRole(String role)
    {
        if (!hasRole(role)) {
            throw new NotAuthorizedException();
        }
    }

    public static void isOrganizer(Project project)
    {
        User user = getLoggedUser().orElseThrow(NotAuthorizedException::new);
        boolean isOrganizer = project.getOrganizers().stream().anyMatch(organizer -> Objects.equals(organizer, user));
        if (!isOrganizer) {
            throw new NotAuthorizedException();
        }
    }

    public static void isMember(Project project)
    {
        User user = getLoggedUser().orElseThrow(NotAuthorizedException::new);
        boolean isMember = project.getMembers().stream().anyMatch(member -> Objects.equals(member, user));
        if (!isMember) {
            throw new NotAuthorizedException();
        }
    }

    public static void isProjectTeamMember(Project project)
    {
        isOrganizer(project);
        isMember(project);
    }
}
