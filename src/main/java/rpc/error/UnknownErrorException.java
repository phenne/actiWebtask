package rpc.error;

public class UnknownErrorException extends RpcException {

    public UnknownErrorException(String message) {
        super(message);
    }
}