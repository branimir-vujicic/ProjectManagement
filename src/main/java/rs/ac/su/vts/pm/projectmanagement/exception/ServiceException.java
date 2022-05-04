package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;

public class ServiceException
        extends RuntimeException
{

    private final int errorCode;

    public ServiceException(int errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, String message, Throwable t)
    {
        super(message, t);
        this.errorCode = errorCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public ErrorType getErrorType()
    {
        return ErrorType.GENERAL_ERROR;
    }

    public ErrorResponse getResponse(HttpStatus status, String path)
    {
        return ErrorResponse.builder().type(getErrorType()).errorCode(getErrorCode()).status(status).path(path).message(getMessage()).build();
    }
}
