package rs.ac.su.vts.pm.projectmanagement.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.su.vts.pm.projectmanagement.exception.ForbiddenException;
import rs.ac.su.vts.pm.projectmanagement.exception.InvalidPasswordException;
import rs.ac.su.vts.pm.projectmanagement.exception.NotAuthorizedException;
import rs.ac.su.vts.pm.projectmanagement.exception.NotFoundException;
import rs.ac.su.vts.pm.projectmanagement.exception.ValidationException;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.repository.UserRepository;

@Component
@Slf4j
@Transactional
public class AuthService
        implements UserDetailsService
{

    private final PasswordEncoder passwordEncoder;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder,
            TokenAuthenticationService tokenAuthenticationService,
            UserRepository userRepository)
    {
        this.passwordEncoder = passwordEncoder;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
    }

    public User login(String email, String password)
            throws NotAuthorizedException
    {
        log.debug("login, [{}]", email);
        User user = getUserByUsername(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!user.isActive()) {
            throw new NotFoundException("User not found");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Bad password or username");
        }
        user.setAccessToken(tokenAuthenticationService.authenticationToken(user));
        return user;
    }

    public User getUser(String token)
    {
        User userFromToken = getUserFromToken(token);
        return getUserByUsername(userFromToken.getEmail());
    }

    public User refreshToken(String token)
            throws NotAuthorizedException
    {
        User user = getUserFromExpiredToken(token);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!user.isActive()) {
            throw new NotFoundException("User not found");
        }
        log.debug("refresh token, [{}];", user.getEmail());
        user.setAccessToken(tokenAuthenticationService.authenticationToken(user));
        return user;
    }

    public UserDetails loadUserByUsername(String username)
    {
        User user = userRepository.findOneByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.getAuthorities());
    }

    public User getUserByUsername(String username)
    {
        return userRepository.findOneByEmail(username);
    }

    public User getUserByUsernameRequired(String username)
    {
        User userByUsername = getUserByUsername(username);
        if (userByUsername == null) {
            throw new ValidationException("user does not exist");
        }
        return userByUsername;
    }

    private User getUserFromToken(String token)
    {
        if (token == null) {
            throw new ForbiddenException();
        }
        try {
            return tokenAuthenticationService.getUserFromToken(token);
        }
        catch (ExpiredJwtException e) {
            throw new NotAuthorizedException("Token has expired");
        }
        catch (UnsupportedJwtException e) {
            throw new NotAuthorizedException("Unsupported token format");
        }
        catch (MalformedJwtException e) {
            throw new NotAuthorizedException("Malformed token");
        }
        catch (Exception e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private User getUserFromExpiredToken(String token)
    {
        if (token == null) {
            throw new ForbiddenException();
        }
        try {
            return tokenAuthenticationService.getUserFromToken(token);
        }
        catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
//			throw new NotAuthorizedException("Token has expired {} ", claims.get());
            return userRepository.findOneByEmail(claims.getSubject());
        }
        catch (UnsupportedJwtException e) {
            throw new NotAuthorizedException("Unsupported token format");
        }
        catch (MalformedJwtException e) {
            throw new NotAuthorizedException("Malformed token");
        }
        catch (Exception e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }
}

