package rs.ac.su.vts.pm.projectmanagement.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

import java.util.Date;

@Service
public class TokenAuthenticationService
{

    static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours

    //Secret used to sign tokens.
    static final char[] SECRET = {'S', 'e', 'c', 'r', 'e', 't', 's', ' ', 'a', 'r', 'e', ' ', 't', 'r', 'i', 'c', 'k', 'y', ' ', 't', 'o', ' ', 'r', 'e', 'm', 'e', 'm', 'b', 'e',
            'r'};

    //The http authentication header name
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    String authenticationToken(User user)
    {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getRoles())
                .claim("rights", user.getRights())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, String.valueOf(SECRET))
                .compact();
    }

    public String refreshToken(User user)
    {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getRoles())
                .claim("rights", user.getRights())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, String.valueOf(SECRET))
                .compact();
    }

    /**
     * Extracts user information from the given request
     */
    User getUserFromToken(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(String.valueOf(SECRET))
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
        User user = new User();
        user.setEmail(claims.getSubject());
        return user;
    }
}
