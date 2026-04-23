package cat.itacademy.s04.t02.n01.fruit_api_h2.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}