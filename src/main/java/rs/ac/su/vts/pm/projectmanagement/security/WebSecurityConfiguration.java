package rs.ac.su.vts.pm.projectmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.auth.AuthService;

/**
 * WebSecurity configuration. Configures authentication manager and endpoints security rules
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration
        extends WebSecurityConfigurerAdapter
{

    private final ObjectMapper objectMapper;
    private final EntryPointUnauthorizedHandler unauthorizedHandler;
    private final AuthService authService;

    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://**");
        config.addAllowedOriginPattern("https://**");
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception
    {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/actuator/metrics").permitAll()
                .antMatchers("/actuator/info").permitAll()
                .antMatchers("/schedulers").permitAll()
                .antMatchers("/api/user/forgot-password/**").permitAll()
                .antMatchers("/api/user/create-password/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/refresh-token").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/v3/api-docs.yaml").permitAll()
                .antMatchers("/api/v3/api-docs/**").permitAll()
                .antMatchers("/docs").permitAll()
                .antMatchers("/setup").permitAll()
                .antMatchers("/setup/**").permitAll()

                .antMatchers("/test").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/api/admin").hasAnyAuthority(User.ROLE_ADMIN)
                .antMatchers("/api/user/me").authenticated()
                .antMatchers("/api/tag/**").hasAnyAuthority(User.ROLE_USER, User.ROLE_ADMIN)
                .antMatchers("/api/user/**").hasAnyAuthority(User.ROLE_USER, User.ROLE_ADMIN)
                .antMatchers("/api/project/**").hasAnyAuthority(User.ROLE_USER, User.ROLE_ADMIN)

                // static content
                .antMatchers("/**").permitAll()
                .antMatchers("/download/**").permitAll()
                .antMatchers("/projects/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/asset-manifest.json").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/logo192.png").permitAll()
                .antMatchers("/logo512.png").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/robots.txt").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/static/**").permitAll()

                .anyRequest().hasAuthority(User.ROLE_ADMIN)
                .and()
                .addFilterBefore(new LoginFilter("/login", authService, objectMapper),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokenFilter("/refresh-token", authService, objectMapper),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthenticationFilter(objectMapper, authService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web)
    {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/static/**",
                "/webjars/**");
    }
}
