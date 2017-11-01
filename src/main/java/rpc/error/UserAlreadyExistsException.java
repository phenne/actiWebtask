package rpc.error;

public class UserAlreadyExistsException extends RpcException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}