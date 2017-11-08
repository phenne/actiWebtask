package rpc.error;

public class UserAlreadyExistsException extends RpcException {

    public UserAlreadyExistsException() {
        super(ErrorType.USER_ALREADY_EXISTS.name());
    }
}