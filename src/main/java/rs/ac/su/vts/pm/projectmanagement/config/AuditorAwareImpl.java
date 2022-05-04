package rs.ac.su.vts.pm.projectmanagement.config;

import org.springframework.data.domain.AuditorAware;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.auth.ContextService;

import java.util.Optional;

public class AuditorAwareImpl
        implements AuditorAware<String>
{

    @Override
    public Optional<String> getCurrentAuditor()
    {
        Optional<User> user = ContextService.getLoggedUser();
        return Optional.of(user.map(User::getEmail).orElse("not present"));
    }
}