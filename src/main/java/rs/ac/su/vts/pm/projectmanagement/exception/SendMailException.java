package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SendMailException
        extends ServiceException
{

    public SendMailException()
    {
        super(ErrorCodes.PROJECT_EXISTS, "Error sending email");
    }

    public SendMailException(String message)
    {
        super(ErrorCodes.PROJECT_EXISTS, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
