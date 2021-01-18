package fixnumlock;

public class FixnumLockException extends RuntimeException {
    public FixnumLockException(String errorMessage) {
        super(errorMessage);
    }
}