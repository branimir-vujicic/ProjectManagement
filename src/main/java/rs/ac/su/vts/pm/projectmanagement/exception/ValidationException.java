package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException
        extends ServiceException
{

    public ValidationException(String message)
    {
        super(ErrorCodes.INVALID_ARGUMENT, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
