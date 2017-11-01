package rpc.error;

public class ActiveUserDeletedException extends RpcException {

    public ActiveUserDeletedException(String message) {
        super(message);
    }
}