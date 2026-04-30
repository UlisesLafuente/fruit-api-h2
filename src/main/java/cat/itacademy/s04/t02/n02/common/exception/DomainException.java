package cat.itacademy.s04.t02.n02.common.exception;

/**
 * Base exception class for domain-specific exceptions.
 * All business logic exceptions should extend this class.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}