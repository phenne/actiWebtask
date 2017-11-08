package rpc.error;

public class ManagerAccessException extends RpcException {

    public ManagerAccessException() {
        super(ErrorType.MANAGER_ACCESS_REQUIRED.name());
    }
}