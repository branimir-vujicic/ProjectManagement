package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TagExistsException
        extends ServiceException
{

    public TagExistsException()
    {
        super(ErrorCodes.TAG_EXISTS, "Tag name exists");
    }

    public TagExistsException(String message)
    {
        super(ErrorCodes.TAG_EXISTS, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
