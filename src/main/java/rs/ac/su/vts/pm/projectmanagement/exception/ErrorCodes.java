package rs.ac.su.vts.pm.projectmanagement.exception;

public class ErrorCodes
{

    public static final int GENERAL_ERROR = 10000;
    public static final int INTERNAL_ERROR = 10001;
    public static final int INVALID_ARGUMENT = 10002;
    public static final int INVALID_CREDENTIALS = 10003;
    public static final int ACCESS_DENIED = 10005;
    public static final int FORBIDDEN = 10006;
    public static final int NOT_FOUND = 10007;
    public static final int IN_USE = 10008;
    public static final int ERROR_SENDING_EMAIL = 10009;
    public static final int SETUP_ALREADY_COMPLETED = 10010;
    //
    public static final int ACCOUNT_DISABLED = 10101;
    public static final int INVALID_TOKEN = 10102;
    public static final int INVALID_RECOVERY_KEY = 10103;
    public static final int RECOVERY_KEY_EXPIRED = 10104;
    public static final int NOT_AUTHORIZED = 10105;
    public static final int UNKNOWN_USER = 10106;
    public static final int PASSWORD_DOES_NOT_MATCH = 10107;
    public static final int USERNAME_EXISTS = 10108;
    public static final int PROJECT_EXISTS = 10109;
    public static final int TAG_EXISTS = 10110;
    //
    public static final int INVALID_EMAIL = 10201;
    public static final int INVALID_URL = 10202;
    //
    public static final int UPLOAD_EXCEPTION = 10301;
    public static final int INVALID_DOCUMENT_TYPE = 10302;
}
