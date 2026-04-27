package cat.itacademy.s04.t02.n02.provider.exception;

public class ProviderAlreadyExistsException extends RuntimeException {
    public ProviderAlreadyExistsException(String message) {
        super(message);
    }
}