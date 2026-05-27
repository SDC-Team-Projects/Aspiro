package com.capcom.aspiro.api.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return errors;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        Map<String, String> error = new HashMap<>();

        error.put("message", ex.getMessage());
        error.put("path", request.getRequestURI());

        return error;
    }
        @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public Map<String, String> handleAuthorizationDenied(
                org.springframework.security.authorization.AuthorizationDeniedException ex,
                HttpServletRequest request
) {
    Map<String, String> error = new HashMap<>();

    error.put("message", "Access denied");
    error.put("path", request.getRequestURI());

    return error;
}
}