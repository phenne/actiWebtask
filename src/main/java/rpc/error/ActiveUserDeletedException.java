package rpc.error;

public class ActiveUserDeletedException extends RpcException {

    public ActiveUserDeletedException() {
        super(ErrorType.CURRENT_USER_DELETED.name());
    }
}