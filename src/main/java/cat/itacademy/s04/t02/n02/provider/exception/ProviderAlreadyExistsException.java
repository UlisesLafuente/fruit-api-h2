package cat.itacademy.s04.t02.n02.provider.exception;

import cat.itacademy.s04.t02.n02.common.exception.DomainException;

/**
 * Exception thrown when attempting to create a provider with a name that already exists.
 * Returns HTTP 409 status code.
 */
public class ProviderAlreadyExistsException extends DomainException {
    public ProviderAlreadyExistsException(String message) {
        super(message);
    }
}