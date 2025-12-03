package ru.ai.sin.exception;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.ai.sin.exception.models.ApiException;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Counter businessErrors = Metrics.counter("business_errors_total");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(
                "Method Argument Not Valid Exception: code={}, status={}, msg={}",
                ex.getBody().getTitle(), ex.getStatusCode(), ex.getMessage()
        );

        businessErrors.increment();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(ex.getBody().getTitle(), ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.error(
                "Bad Request Exception: code={}, status={}, msg={}",
                ex.getCode(), ex.getStatus(), ex.getMessage()
        );

        businessErrors.increment();

        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(ex.getCode(), ex.getCode()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex) {
        log.error(
                "Api error: code={}, status={}, msg={}",
                ex.getCode(), ex.getStatus(), ex.getMessage()
        );

        businessErrors.increment();

        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(ex.getCode(), ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("INTERNAL_ERROR", "Unexpected error occurred"));
    }
}
