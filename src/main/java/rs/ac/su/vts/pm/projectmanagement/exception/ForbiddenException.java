package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException
        extends ServiceException
{

    public ForbiddenException()
    {
        super(ErrorCodes.FORBIDDEN, "Forbidden");
    }

    public ForbiddenException(String message)
    {
        super(ErrorCodes.FORBIDDEN, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.FORBIDDEN;
    }
}
