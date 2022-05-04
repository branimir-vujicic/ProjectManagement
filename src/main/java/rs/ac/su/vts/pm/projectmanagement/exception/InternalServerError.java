package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError
        extends ServiceException
{

    public InternalServerError(String message, Exception e)
    {
        super(ErrorCodes.INTERNAL_ERROR, message, e);
    }

    public InternalServerError(String message)
    {
        super(ErrorCodes.INTERNAL_ERROR, message);
    }
}
