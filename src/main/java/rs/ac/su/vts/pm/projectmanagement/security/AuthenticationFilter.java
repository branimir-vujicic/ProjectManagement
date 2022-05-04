package rs.ac.su.vts.pm.projectmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.GenericFilterBean;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorCodes;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorResponse;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorType;
import rs.ac.su.vts.pm.projectmanagement.exception.ServiceException;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;
import rs.ac.su.vts.pm.projectmanagement.services.auth.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

public class AuthenticationFilter
		extends GenericFilterBean
{

	public static final String HEADER_STRING = "Authorization";

	private final ObjectMapper mapper;
	private final AuthService authService;

	public AuthenticationFilter(ObjectMapper mapper, AuthService authService)
	{
		this.mapper = mapper;
		this.authService = authService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException
	{

		Authentication authentication;
		try {
			authentication = getAuthentication((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (ServiceException e) {
			String uri = ((HttpServletRequest) request).getRequestURI();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
			if (responseStatus != null) {
				status = responseStatus.code();
			}
			((HttpServletResponse) response).setStatus(status.value());
			response.getWriter().write(mapper.writeValueAsString(
					new ErrorResponse(e.getErrorCode(), status, e.getErrorType(), e.getMessage(), uri, null, Collections.emptyList()))
			);
		}
		catch (Exception e) {
			String uri = ((HttpServletRequest) request).getRequestURI();
			((HttpServletResponse) response).setStatus(500);
			response.getWriter().write(mapper.writeValueAsString(
					new ErrorResponse(ErrorCodes.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_SERVER_ERROR, e.getMessage(), uri, null, Collections.emptyList()))
			);
		}
		filterChain.doFilter(request, response);
	}

	Authentication getAuthentication(HttpServletRequest request)
	{
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			User user = authService.getUser(token);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}
