package rpc.error;

public enum  ErrorType {

    SESSION_EXPIRED,
    CURRENT_USER_DELETED,
    MANAGER_ACCESS_REQUIRED,
    USER_ALREADY_EXISTS,
    UNKNOWN_ERROR,
    USER_DELETED;

    String type;

}
