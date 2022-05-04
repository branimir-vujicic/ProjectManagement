package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException
        extends ServiceException
{

    public NotFoundException(String message)
    {
        super(ErrorCodes.NOT_FOUND, message);
    }

    public NotFoundException(String message, Throwable t)
    {
        super(ErrorCodes.NOT_FOUND, message, t);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.SERVICE_ERROR;
    }
}
