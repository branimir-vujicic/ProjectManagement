package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDocumentTypeException
        extends ServiceException
{

    public InvalidDocumentTypeException(String message)
    {
        super(ErrorCodes.INVALID_DOCUMENT_TYPE, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.UNSUPPORTED_MEDIA_TYPE_ERROR;
    }
}
