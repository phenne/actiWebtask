package rpc.error;

public class SessionInvalidatedException extends RpcException {

    private String type;

    public SessionInvalidatedException(String message) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
