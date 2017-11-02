package bd;

public class TransactionFactory {

    public static final class TransactionFactoryHolder {
       public static final TransactionFactory INSTANCE = new TransactionFactory();
    }

    public static TransactionFactory getInstance() {
        return TransactionFactoryHolder.INSTANCE;
    }

    public Transaction createTransaction() {
        return new Transaction();
    }
}
