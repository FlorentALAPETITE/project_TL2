package transactionalMemory;

public class AbortException extends Exception {
    public AbortException(String message) {
        super(message);
    }
}