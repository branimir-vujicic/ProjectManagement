package rs.ac.su.vts.pm.projectmanagement.exception;

public enum ErrorType
{

    GENERAL_ERROR("10001", "Unclassified error"),
    INTERNAL_SERVER_ERROR("10002", "Internal server error"),
    VALIDATION_ERROR("10003", "Validation error"),
    BAD_REQ_INVALID_PASSWORD("10004", "Invalid auth"),
    BAD_REQ_INVALID_AUTHORIZATION("10005", "Invalid auth"),
    FORBIDDEN("10006", "Forbidden"),
    SERVICE_ERROR("10007", "Unclassified error"),
    UNSUPPORTED_MEDIA_TYPE_ERROR("10008", "Unsupported channels type error"),
    HTTP_MESSAGE_NOT_READABLE_ERROR("10009", "Http message not readable"),
    DUPLICATE_KEY("10010", "Duplicate DB key"),
    NOT_FOUND("10011", "Resource not found");

    private final String code;
    private final String type;

    ErrorType(String code, String type)
    {
        this.code = code;
        this.type = type;
    }

    public String code()
    {
        return code;
    }

    public String type()
    {
        return type;
    }
}
