package rs.ac.su.vts.pm.projectmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
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

public class RefreshTokenFilter
        extends AbstractAuthenticationProcessingFilter
{

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public RefreshTokenFilter(String url, AuthService authService, ObjectMapper objectMapper)
    {
        super(new AntPathRequestMatcher(url));
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException
    {
        try {
            String authorization = req.getHeader("Authorization");
            String token = authorization.replace("Bearer ", "").strip();
            User user = authService.refreshToken(token);
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
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
            throws IOException
    {
        res.setStatus(200);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        User principal = (User) auth.getPrincipal();
        UserLoginDto userDto = UserMapper.INSTANCE.toLoginDto(principal);
        res.getWriter().write(objectMapper.writeValueAsString(userDto));
        res.getWriter().flush();
    }
}
