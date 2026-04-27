package cat.itacademy.s04.t02.n02.common.exception;

import java.util.List;

public record ErrorResponse(int status, String message, List<String> errors) {
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }
}