package cat.itacademy.s04.t02.n02.common.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Returns HTTP 404 status code.
 */
public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }
}