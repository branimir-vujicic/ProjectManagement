package rs.ac.su.vts.pm.projectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectInUseException
        extends ServiceException
{

    private final Long id;

    public ObjectInUseException(String message, Long id)
    {
        super(ErrorCodes.IN_USE, message);
        this.id = id;
    }

    public ObjectInUseException(String message, Throwable t, Long id)
    {
        super(ErrorCodes.IN_USE, message, t);
        this.id = id;
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.SERVICE_ERROR;
    }

    public Long getId()
    {
        return id;
    }

    public ErrorResponse getResponse(HttpStatus status, String path)
    {
        return ErrorResponse.builder().type(getErrorType()).errorCode(getErrorCode()).status(status).path(path).message(getMessage()).id(getId()).build();
    }
}
