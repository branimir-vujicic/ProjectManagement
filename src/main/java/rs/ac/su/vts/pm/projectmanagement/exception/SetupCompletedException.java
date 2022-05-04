package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SetupCompletedException
        extends ServiceException
{

    public SetupCompletedException()
    {
        super(ErrorCodes.SETUP_ALREADY_COMPLETED, "Setup already completed !");
    }

    public SetupCompletedException(String message)
    {
        super(ErrorCodes.SETUP_ALREADY_COMPLETED, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
