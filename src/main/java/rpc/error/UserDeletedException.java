package rpc.error;

public class UserDeletedException extends RpcException {

    public UserDeletedException() {
        super(ErrorType.USER_DELETED.name());
    }
}