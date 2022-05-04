package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectExistsException
        extends ServiceException
{

    public ProjectExistsException()
    {
        super(ErrorCodes.PROJECT_EXISTS, "Project title exists");
    }

    public ProjectExistsException(String message)
    {
        super(ErrorCodes.PROJECT_EXISTS, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
