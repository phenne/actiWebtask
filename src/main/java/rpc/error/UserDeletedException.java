package rpc.error;

public class UserDeletedException extends RpcException {

    public UserDeletedException(String message) {
        super(message);
    }
}