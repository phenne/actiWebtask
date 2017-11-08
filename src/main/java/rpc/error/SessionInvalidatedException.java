package rpc.error;

public class SessionInvalidatedException extends RpcException {

    public SessionInvalidatedException() {
        super(ErrorType.SESSION_EXPIRED.name());
    }
}
