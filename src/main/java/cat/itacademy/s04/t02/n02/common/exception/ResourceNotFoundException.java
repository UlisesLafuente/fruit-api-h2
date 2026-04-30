package cat.itacademy.s04.t02.n02.common.exception;

/**
 * Exception thrown when a requested resource (e.g., fruit) is not found.
 * Returns HTTP 404 status code.
 */
public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}