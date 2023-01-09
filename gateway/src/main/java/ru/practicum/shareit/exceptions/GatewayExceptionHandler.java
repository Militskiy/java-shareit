package ru.practicum.shareit.exceptions;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleFeignException(final FeignException e) {
        if (e.status() == 500) {
            log.error(e.getMessage());
        } else {
            log.warn(e.getMessage());
        }
        return ResponseEntity.status(e.status()).body(new JSONObject(e.contentUTF8()).toMap());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<String> errorList = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        final String errors = errorList.toString().substring(1, errorList.toString().length() - 1);
        log.warn(errors);
        return ResponseEntity.status(400).body(new ErrorResponse(errors));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e
    ) {
        final String error = "Unknown " + e.getName() + ": " + e.getValue();
        log.warn(error);
        return ResponseEntity.status(400).body(new ErrorResponse(error));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException e
    ) {
        final String error = "Missing request parameter: " + e.getParameterName();
        log.warn(error);
        return ResponseEntity.status(400).body(new ErrorResponse(error));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
    }
}
