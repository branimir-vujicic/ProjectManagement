package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameExistsException
        extends ServiceException
{

    public UsernameExistsException()
    {
        super(ErrorCodes.USERNAME_EXISTS, "Username exists");
    }

    public UsernameExistsException(String message)
    {
        super(ErrorCodes.USERNAME_EXISTS, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
