package rs.ac.su.vts.pm.projectmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorCodes;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorResponse;
import rs.ac.su.vts.pm.projectmanagement.exception.ErrorType;
import rs.ac.su.vts.pm.projectmanagement.exception.ObjectInUseException;
import rs.ac.su.vts.pm.projectmanagement.exception.SendMailException;
import rs.ac.su.vts.pm.projectmanagement.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static rs.ac.su.vts.pm.projectmanagement.exception.ErrorCodes.ACCESS_DENIED;
import static rs.ac.su.vts.pm.projectmanagement.exception.ErrorCodes.INTERNAL_ERROR;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler
		extends ResponseEntityExceptionHandler
{

	private static final String ERROR_PROCESSING_REQUEST = "Error processing request:";
	private static final String ERROR_PROCESSING_REQUEST_TEMPLATE = "Error processing request method {}; error: [ {} ] :";

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		String fieldName = ((MethodArgumentTypeMismatchException) ex).getName();
		Class<?> requiredType = ex.getRequiredType();
		final Object value = ex.getValue();
		String message = "Invalid type field: " + fieldName + ",  requiredType : " + requiredType + ", actual : " + value;
		final HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
		ErrorResponse errorResponse = ErrorResponse.builder().type(ErrorType.VALIDATION_ERROR).message(message).path(servletRequest.getRequestURI()).build();
		return new ResponseEntity<>(errorResponse, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
		String error;
		for (FieldError fieldError : fieldErrors) {
			error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
			errors.add(error);
		}
		ErrorResponse errorResponse = ErrorResponse.builder().type(ErrorType.VALIDATION_ERROR).errors(errors).path(((ServletWebRequest) request).getRequest().getRequestURI()).build();
		return new ResponseEntity<>(errorResponse, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		String unsupported = "Unsupported content type: " + ex.getContentType();
		String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
		ErrorResponse errorResponse = ErrorResponse.builder().type(ErrorType.UNSUPPORTED_MEDIA_TYPE_ERROR).errors(List.of(unsupported, supported)).path(((ServletWebRequest) request).getRequest().getRequestURI()).build();
		return new ResponseEntity<>(errorResponse, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		Throwable mostSpecificCause = ex.getMostSpecificCause();
		ErrorResponse errorMessage;
		String exceptionName = mostSpecificCause.getClass().getName();
		String message = mostSpecificCause.getMessage();
		errorMessage = ErrorResponse.builder()
				.type(ErrorType.HTTP_MESSAGE_NOT_READABLE_ERROR)
				.message(exceptionName)
				.errors(List.of(message))
				.path(((ServletWebRequest) request).getRequest().getRequestURI())
				.build();
		return new ResponseEntity<>(errorMessage, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleNoHandlerFoundException", ex.getMessage());
		return new ResponseEntity<>(
				ErrorResponse.builder()
						.type(ErrorType.NOT_FOUND)
						.message(ex.getMessage())
						.status(HttpStatus.NOT_FOUND)
						.path(((ServletWebRequest) request).getRequest().getRequestURI())
						.build()
				, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus
			status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		return new ResponseEntity<>(
				ErrorResponse.builder().type(ErrorType.VALIDATION_ERROR).message(ex.getMessage()).path(((ServletWebRequest) request).getRequest().getRequestURI()).build()
				, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleServletRequestBindingException", ex.getClass());
		return new ResponseEntity<>(
				ErrorResponse.builder()
						.type(ErrorType.VALIDATION_ERROR)
						.message(ex.getMessage())
						.status(status)
						.path(((ServletWebRequest) request).getRequest().getRequestURI())
						.build()
				, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleBindException", ex.getClass());
		return new ResponseEntity<>(
				ErrorResponse.builder()
						.type(ErrorType.VALIDATION_ERROR)
						.message(ex.getMessage())
						.status(status)
						.path(((ServletWebRequest) request).getRequest().getRequestURI())
						.errors(ex.getBindingResult().getFieldErrors().stream()
								.map(e -> ("field [" + e.getField() + "] invalid value : " + e.getRejectedValue()))
								.collect(Collectors.toList())
						)
						.build()
				, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		return new ResponseEntity<>(
				ErrorResponse.builder()
						.type(ErrorType.INTERNAL_SERVER_ERROR)
						.message(ex.getMessage())
						.path(((ServletWebRequest) request).getRequest().getRequestURI())
						.build()
				, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CompletionException.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleCompletionExceptionException(HttpServletRequest request, CompletionException exception)
	{
		Throwable cause = exception.getCause();
		if (cause instanceof ServiceException) {
			return handleServiceException(request, (ServiceException) cause);
		}
		else if (cause instanceof DataAccessResourceFailureException
				|| cause instanceof RecoverableDataAccessException) {
			return handleDataAccessException((DataAccessException) cause);
		}
		else if (cause instanceof DuplicateKeyException) {
			return handleDuplicateKeyException((DuplicateKeyException) cause);
		}
		else {
			log.error(ERROR_PROCESSING_REQUEST, cause);
			return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.INTERNAL_SERVER_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	private HttpEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.DUPLICATE_KEY).message(ex.getCause().getMessage()).build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	private HttpEntity<ErrorResponse> handleDuplicateKeyException(DataIntegrityViolationException ex)
	{
		log.error(ERROR_PROCESSING_REQUEST, ex);
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.DUPLICATE_KEY).message(ex.getCause().getMessage()).build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({RecoverableDataAccessException.class, DataAccessResourceFailureException.class})
	@ResponseBody
	public HttpEntity<ErrorResponse> handleDataAccessException(DataAccessException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST, cause);
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.INTERNAL_SERVER_ERROR).message("Can't connect to DB at the moment.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<ErrorResponse> handleConflict(HttpServletRequest request, RuntimeException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleConflict", cause.getClass());

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.VALIDATION_ERROR).errorCode(ErrorCodes.INVALID_ARGUMENT).status(status).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}

	@ExceptionHandler(ObjectInUseException.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleObjectInUse(HttpServletRequest request, ObjectInUseException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleObjectInUse", cause.getClass(), cause);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(ErrorResponse.builder().type(cause.getErrorType()).errorCode(cause.getErrorCode()).status(status).id(cause.getId()).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, "handleServiceException", cause.getClass(), cause);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(cause.getErrorType()).errorCode(cause.getErrorCode()).status(status).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}

	@ExceptionHandler(SendMailException.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleSendMailException(HttpServletRequest request, ServiceException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST, cause);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(cause.getErrorType()).errorCode(cause.getErrorCode()).status(status).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}

	/*
	@ExceptionHandler( value = {
			InvalidDocumentTypeException.class,
			InvalidPasswordException.class,
			NotAuthorizedException.class,
			NotFoundException.class,
			ObjectInUseException.class,
			PasswordsDoesntException.class,
			ProjectExistsException.class,
			ServiceException.class,
			UsernameExistsException.class,
			ValidationException.class
	})
	*/

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException cause)
	{
		log.error(ERROR_PROCESSING_REQUEST, cause);

		HttpStatus status = HttpStatus.FORBIDDEN;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.FORBIDDEN).errorCode(ACCESS_DENIED).status(status).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleUnknownException(Exception cause)
	{
		log.error(ERROR_PROCESSING_REQUEST_TEMPLATE, cause.getClass(), cause);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(ErrorType.GENERAL_ERROR).errorCode(INTERNAL_ERROR).status(status).path("").message(cause.getMessage()).build(), status);
	}

/*
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public HttpEntity<ErrorResponse> handleUnknownException(HttpServletRequest request, ServiceException cause) {
		log.error(ERROR_PROCESSING_REQUEST, cause);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(cause.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			status = responseStatus.code();
		}
		return new ResponseEntity<>(ErrorResponse.builder().type(cause.getErrorType()).errorCode(cause.getErrorCode()).status(status).path(request.getRequestURI()).message(cause.getMessage()).build(), status);
	}
	*/
}
