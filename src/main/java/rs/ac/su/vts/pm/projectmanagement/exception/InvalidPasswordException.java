package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException
        extends ServiceException
{

    public InvalidPasswordException(String message)
    {
        super(ErrorCodes.INVALID_CREDENTIALS, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.BAD_REQ_INVALID_PASSWORD;
    }
}
