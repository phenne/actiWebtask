package rpc.error;

public class UnknownErrorException extends RpcException {

    public UnknownErrorException() {
        super(ErrorType.UNKNOWN_ERROR.name());
    }
}