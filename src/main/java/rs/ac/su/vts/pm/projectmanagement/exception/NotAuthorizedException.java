package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException
        extends ServiceException
{

    public NotAuthorizedException()
    {
        super(ErrorCodes.NOT_AUTHORIZED, "Authorization required");
    }

    public NotAuthorizedException(String message)
    {
        super(ErrorCodes.NOT_AUTHORIZED, message);
    }

    public NotAuthorizedException(String message, Throwable throwable)
    {
        super(ErrorCodes.NOT_AUTHORIZED, message, throwable);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.BAD_REQ_INVALID_AUTHORIZATION;
    }
}
