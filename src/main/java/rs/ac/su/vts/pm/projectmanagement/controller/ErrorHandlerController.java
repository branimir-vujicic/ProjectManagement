package rs.ac.su.vts.pm.projectmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorResponse;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorType;

@RestController
@Slf4j
public class ErrorHandlerController
		implements ErrorController
{

	@RequestMapping("/error")
	@ResponseBody
	public String getErrorPath(HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error("ERROR controller");
		return new ResponseEntity<>(
				ErrorResponse.builder()
						.type(ErrorType.INTERNAL_SERVER_ERROR)
						.message("Internal Error")
						.path(((ServletWebRequest) request).getRequest().getRequestURI())
						.build()
				, HttpStatus.INTERNAL_SERVER_ERROR)
				.toString();
	}
}
