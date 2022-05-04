package rs.ac.su.vts.pm.projectmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserLoginDto;
import rs.ac.su.vts.pm.projectmanagement.model.dto.user.UserMapper;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.auth.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginFilter
        extends AbstractAuthenticationProcessingFilter
{

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public LoginFilter(String url, AuthService authService, ObjectMapper objectMapper)
    {
        super(new AntPathRequestMatcher(url));
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (!user.isActive()) {
                throw new DisabledException("User is disabled and cannot login ! ");
            }
            return new UsernamePasswordAuthenticationToken(user, user.getEmail(), user.getAuthorities());
        }
        catch (Exception e) {
            throw new BadCredentialsException("", e);
        }
    }

    /**
     * Puts the authorization header to the http response.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authentication)
            throws IOException
    {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        User user = (User) authentication.getPrincipal();
        UserLoginDto userDto = UserMapper.INSTANCE.toLoginDto(user);
        response.getWriter().write(objectMapper.writeValueAsString(userDto));
        response.getWriter().flush();
    }
}
