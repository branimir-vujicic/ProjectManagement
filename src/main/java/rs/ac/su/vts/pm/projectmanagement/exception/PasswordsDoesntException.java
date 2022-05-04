package rs.ac.su.vts.pm.projectmanagement.exception;

public class PasswordsDoesntException
        extends ServiceException
{

    public PasswordsDoesntException(String message)
    {
        super(ErrorCodes.PASSWORD_DOES_NOT_MATCH, message);
    }

    @Override
    public ErrorType getErrorType()
    {
        return ErrorType.VALIDATION_ERROR;
    }
}
