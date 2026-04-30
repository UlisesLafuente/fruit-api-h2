package cat.itacademy.s04.t02.n02.common.exception;

import cat.itacademy.s04.t02.n02.provider.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderHasFruitsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler for the REST API.
 * Catches and formats all exceptions into standardized error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundException and its subclasses.
     * Returns HTTP 404 status code.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    /**
     * Handles ProviderAlreadyExistsException.
     * Returns HTTP 409 status code.
     */
    @ExceptionHandler(ProviderAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProviderAlreadyExists(ProviderAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, ex.getMessage()));
    }

    /**
     * Handles ProviderHasFruitsException.
     * Returns HTTP 400 status code.
     */
    @ExceptionHandler(ProviderHasFruitsException.class)
    public ResponseEntity<ErrorResponse> handleProviderHasFruits(ProviderHasFruitsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, ex.getMessage()));
    }

    /**
     * Handles validation errors from request body.
     * Returns HTTP 400 status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Validation failed", errors));
    }

    /**
     * Catches any unhandled domain exceptions.
     * Returns HTTP 400 status code.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, ex.getMessage()));
    }
}