package cat.itacademy.s04.t02.n02.provider.exception;

import cat.itacademy.s04.t02.n02.common.exception.DomainException;

/**
 * Exception thrown when attempting to delete a provider that has associated fruits.
 * Returns HTTP 400 status code.
 */
public class ProviderHasFruitsException extends DomainException {
    public ProviderHasFruitsException(String message) {
        super(message);
    }
}