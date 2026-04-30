package cat.itacademy.s04.t02.n02.provider.exception;

import cat.itacademy.s04.t02.n02.common.exception.NotFoundException;

/**
 * Exception thrown when a requested provider is not found.
 * Returns HTTP 404 status code.
 */
public class ProviderNotFoundException extends NotFoundException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
}